package net.ornithemc.osl.commands.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.tree.CommandNode;

import net.ornithemc.osl.commands.api.command.CommandFailureConsumer;
import net.ornithemc.osl.commands.api.command.SharedSuggestionProvider;
import net.ornithemc.osl.commands.api.command.SuggestionProviders;
import net.ornithemc.osl.commands.api.server.command.CommandSourceStack;
import net.ornithemc.osl.commands.impl.network.CommandSuggestionsPayload;
import net.ornithemc.osl.commands.impl.network.CommandsPayload;
import net.ornithemc.osl.commands.impl.network.RequestCommandSuggestionsPayload;
import net.ornithemc.osl.commands.impl.server.ServerCommandsImpl;
import net.ornithemc.osl.networking.api.client.ClientPlayNetworking;
import net.ornithemc.osl.networking.api.server.ServerPlayNetworking;
import net.ornithemc.osl.text.api.ClickEvent;
import net.ornithemc.osl.text.api.HoverEvent;
import net.ornithemc.osl.text.api.TextColor;
import net.ornithemc.osl.text.api.TextComponent;
import net.ornithemc.osl.text.api.TextComponents;

public final class BrigadierCommandsImpl {

	public static final Logger LOGGER = LogManager.getLogger("OSL|Brigadier Commands");

	public static void init() {
		ServerPlayNetworking.registerListener(RequestCommandSuggestionsPayload.CHANNEL, RequestCommandSuggestionsPayload::new, (context, payload) -> {
			context.ensureOnMainThread();

			CommandSourceStack source = context.player().createCommandSourceStack();
			int id = payload.getId();
			String command = payload.getCommand();

			ServerCommandsImpl.getSuggestions(source, command).thenAccept(suggestions -> {
				ClientPlayNetworking.send(CommandSuggestionsPayload.CHANNEL, new CommandSuggestionsPayload(id, suggestions));
			});
		});
	}

	public static void initClient() {
		ClientPlayNetworking.registerListener(CommandsPayload.CHANNEL, CommandsPayload::new, (context, payload) -> {
			context.ensureOnMainThread();
			context.networkHandler().handleCommands(payload);
		});
		ClientPlayNetworking.registerListener(CommandSuggestionsPayload.CHANNEL, CommandSuggestionsPayload::new, (context, payload) -> {
			context.ensureOnMainThread();
			context.networkHandler().handleCommandSuggestions(payload);
		});
	}

	public static <S extends SharedSuggestionProvider> void fillCommands(CommandDispatcher<S> sourceDispatcher, CommandDispatcher<SharedSuggestionProvider> targetDispatcher, S source) {
		Map<CommandNode<S>, CommandNode<SharedSuggestionProvider>> nodes = new HashMap<>();
		nodes.put(sourceDispatcher.getRoot(), targetDispatcher.getRoot());
		fillCommands(sourceDispatcher.getRoot(), targetDispatcher.getRoot(), source, nodes);
	}

	private static <S extends SharedSuggestionProvider> void fillCommands(CommandNode<S> sourceRoot, CommandNode<SharedSuggestionProvider> targetRoot, S source, Map<CommandNode<S>, CommandNode<SharedSuggestionProvider>> nodes) {
		for (CommandNode<S> sourceChild : sourceRoot.getChildren()) {
			if (!sourceChild.canUse(source)) {
				continue;
			}

			@SuppressWarnings("unchecked")
			ArgumentBuilder<SharedSuggestionProvider, ?> builder = (ArgumentBuilder<SharedSuggestionProvider, ?>) sourceChild.createBuilder();

			// covered by the canUse check
			builder.requires(s -> true);

			if (builder.getCommand() != null) {
				builder.executes(ctx -> 0);
			}

			if (builder.getRedirect() != null) {
				builder.redirect(nodes.get(builder.getRedirect()));
			}

			if (builder instanceof RequiredArgumentBuilder) {
				@SuppressWarnings("unchecked")
				RequiredArgumentBuilder<SharedSuggestionProvider, ?> argumentBuilder = ((RequiredArgumentBuilder<SharedSuggestionProvider, ?>) builder);

				if (argumentBuilder.getSuggestionsProvider() != null) {
					argumentBuilder.suggests(SuggestionProviders.getIdentifiable(argumentBuilder.getSuggestionsProvider()));
				}
			}

			CommandNode<SharedSuggestionProvider> targetChild = builder.build();

			nodes.put(sourceChild, targetChild);
			targetRoot.addChild(targetChild);

			if (!sourceChild.getChildren().isEmpty()) {
				fillCommands(sourceChild, targetChild, source, nodes);
			}
		}
	}

	public static <S extends CommandFailureConsumer> Integer run(CommandDispatcher<S> dispatcher, S source, String command, boolean unknownCommandIsFailure) {
		if (dispatcher == null) {
			throw new IllegalStateException("cannot run client commands because the command dispatcher is not set up!");
		}

		StringReader reader = new StringReader(command);
		if (reader.canRead() && reader.peek() == '/') {
			reader.skip();
		}

		try {
			return dispatcher.execute(reader, source);
		} catch (CommandSyntaxException e) {
			if (!unknownCommandIsFailure && e.getType() == CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand()) {
				return null;
			}

			source.sendFailure(TextComponents.resolve(e.getRawMessage()));

			if (e.getInput() != null && e.getCursor() >= 0) {
				int cursor = Math.min(e.getInput().length(), e.getCursor());

				TextComponent message = TextComponents.literal("")
					.format(style -> style
						.withColor(TextColor.GRAY)
						.withClickEvent(ClickEvent.suggestCommand(command)));

				if (cursor > 10) {
					message.append("...");
				}

				message.append(e.getInput().substring(Math.max(0, cursor - 10), cursor));

				if (cursor < e.getInput().length()) {
					message.append(TextComponents.literal(e.getInput().substring(cursor)).format(style -> style.withColor(TextColor.RED).withUnderlined(true)));
				}

				message.append(TextComponents.translatable("command.context.here").format(style -> style.withColor(TextColor.RED).withItalic(true)));

				source.sendFailure(message);
			}

			return 0;
		} catch (Exception e) {
			TextComponent description = TextComponents.literal(e.getMessage() == null ? e.getClass().getName() : e.getMessage());

			if (LOGGER.isDebugEnabled()) {
				StackTraceElement[] stackTrace = e.getStackTrace();

				for (StackTraceElement element : stackTrace) {
					description
						.append("\n\n")
						.append(element.getMethodName())
						.append("\n ")
						.append(element.getFileName())
						.append(":")
						.append(String.valueOf(element.getLineNumber()));
				}
			}

			TextComponent message = TextComponents.translatable("command.failed")
				.format(style -> style
					.withHoverEvent(HoverEvent.showText(description)));

			source.sendFailure(message);

			return 0;
		}
	}

	public static <S> CompletableFuture<Suggestions> getSuggestions(CommandDispatcher<S> dispatcher, S source, String command) {
		StringReader reader = new StringReader(command);

		if (reader.canRead() && reader.peek() == '/') {
			reader.skip();
		}

		ParseResults<S> results = dispatcher.parse(reader, source);
		return dispatcher.getCompletionSuggestions(results);
	}

	public static <S> CompletableFuture<List<String>> getLegacySuggestions(CommandDispatcher<S> dispatcher, S source, String command) {
		return getSuggestions(dispatcher, source, command).thenApply(suggestions -> convertSuggestionsToLegacyFormat(command, suggestions));
	}

	public static List<String> convertSuggestionsToLegacyFormat(String command, Suggestions suggestions) {
		List<String> legacySuggestions = new ArrayList<>();
		boolean commandIsSingleWord = command.indexOf(' ') < 0;

		for (Suggestion suggestion : suggestions.getList()) {
			if (commandIsSingleWord) {
				legacySuggestions.add("/" + suggestion.getText());
			} else {
				boolean suggestionIsSingleWord = suggestion.getText().indexOf(' ') < 0;

				StringRange range = suggestion.getRange();
				int start = range.getStart();
				int end = range.getEnd();

				if (suggestionIsSingleWord) {
					int lastSpace = command.lastIndexOf(' ') + 1;

					if (lastSpace == start) {
						legacySuggestions.add(suggestion.getText());
					} else {
						// suggestion must be the completed word!
						legacySuggestions.add(command.substring(lastSpace, start) + suggestion.getText());
					}
				} else {
					int prefixLength = end - start;

					if (suggestion.getText().charAt(prefixLength) == ' ') {
						// legacy suggestions do not complete subsequent words until you add a space!
					} else {
						legacySuggestions.add(suggestion.getText().substring(prefixLength));
					}
				}
			}
		}

		return legacySuggestions;
	}
}
