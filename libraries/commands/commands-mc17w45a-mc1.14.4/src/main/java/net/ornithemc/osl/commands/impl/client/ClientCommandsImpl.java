package net.ornithemc.osl.commands.impl.client;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;

import net.ornithemc.osl.commands.api.client.ClientCommandEvents;
import net.ornithemc.osl.commands.api.client.ClientCommands;
import net.ornithemc.osl.commands.api.client.command.ClientCommandSource;
import net.ornithemc.osl.commands.api.client.command.HelpCommand;
import net.ornithemc.osl.text.api.ClickEvent;
import net.ornithemc.osl.text.api.HoverEvent;
import net.ornithemc.osl.text.api.TextColor;
import net.ornithemc.osl.text.api.TextComponent;
import net.ornithemc.osl.text.api.TextComponents;

public final class ClientCommandsImpl {

	public static final Logger LOGGER = LogManager.getLogger("OSL|Client Commands");

	private static CommandDispatcher<ClientCommandSource> dispatcher;

	public static void init(ClientCommands.Selection selection) {
		ClientCommandsImpl.dispatcher = new CommandDispatcher<>();

		registerCommands(selection, new BuildContext());
	}

	private static void registerCommands(ClientCommands.Selection selection, ClientCommands.BuildContext context) {
		ClientCommandEvents.REGISTER_COMMANDS.invoker().registerCommands(dispatcher, selection, context);

		if (!dispatcher.getRoot().getChildren().isEmpty()) {
			HelpCommand.register(dispatcher);
		}

		dispatcher.findAmbiguities((root, node1, node2, inputs) -> LOGGER.warn("Ambiguity between arguments {} and {} with inputs: {}", dispatcher.getPath(node1), dispatcher.getPath(node2), inputs));
//		dispatcher.setConsumer((ctx, success, result) -> ctx.getSource().onCommandComplete(ctx, success, result));
	}

	public static void fillCommands(CommandDispatcher<ClientCommandSource> commands, ClientCommandSource source) {
		Map<CommandNode<ClientCommandSource>, CommandNode<ClientCommandSource>> nodes = new HashMap<>();
		nodes.put(dispatcher.getRoot(), commands.getRoot());
		fillCommands(dispatcher.getRoot(), commands.getRoot(), source, nodes);
	}

	private static void fillCommands(CommandNode<ClientCommandSource> sourceRoot, CommandNode<ClientCommandSource> targetRoot, ClientCommandSource source, Map<CommandNode<ClientCommandSource>, CommandNode<ClientCommandSource>> nodes) {
		for (CommandNode<ClientCommandSource> sourceChild : sourceRoot.getChildren()) {
			if (!sourceChild.canUse(source)) {
				continue;
			}

			ArgumentBuilder<ClientCommandSource, ?> builder = sourceChild.createBuilder();

			// covered by the canUse check
			builder.requires(s -> true);

			if (builder.getCommand() != null) {
				builder.executes(ctx -> 0);
			}

			if (builder.getRedirect() != null) {
				builder.redirect(nodes.get(builder.getRedirect()));
			}

			CommandNode<ClientCommandSource> targetChild = builder.build();

			nodes.put(sourceChild, targetChild);
			targetRoot.addChild(targetChild);

			if (!sourceChild.getChildren().isEmpty()) {
				fillCommands(sourceChild, targetChild, source, nodes);
			}
		}
	}

	public static CommandDispatcher<ClientCommandSource> dispatcher() {
		return dispatcher;
	}

	public static LiteralArgumentBuilder<ClientCommandSource> literal(String name) {
		return LiteralArgumentBuilder.literal(name);
	}

	public static <T> RequiredArgumentBuilder<ClientCommandSource, T> argument(String name, ArgumentType<T> type) {
		return RequiredArgumentBuilder.argument(name, type);
	}

	public static int run(ClientCommandSource source, String command) {
		return run(source, command, true);
	}

	public static Integer run(ClientCommandSource source, String command, boolean unknownCommandIsFailure) {
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
				.format(style ->
					style.withHoverEvent(HoverEvent.showText(description)));

			source.sendFailure(message);

			return 0;
		}
	}

	private static class BuildContext implements ClientCommands.BuildContext {
	}
}
