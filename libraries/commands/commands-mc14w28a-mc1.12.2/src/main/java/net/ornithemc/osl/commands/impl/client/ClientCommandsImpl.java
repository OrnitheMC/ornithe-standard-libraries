package net.ornithemc.osl.commands.impl.client;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.suggestion.Suggestions;

import net.ornithemc.osl.commands.api.client.ClientCommandEvents;
import net.ornithemc.osl.commands.api.client.ClientCommands;
import net.ornithemc.osl.commands.api.client.command.ClientCommandSource;
import net.ornithemc.osl.commands.api.client.command.HelpCommand;
import net.ornithemc.osl.commands.api.command.SharedSuggestionProvider;
import net.ornithemc.osl.commands.impl.BrigadierCommandsImpl;

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

	public static void fillCommands(CommandDispatcher<SharedSuggestionProvider> commands, ClientCommandSource source) {
		BrigadierCommandsImpl.fillCommands(dispatcher, commands, source);
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
		return BrigadierCommandsImpl.run(dispatcher, source, command, unknownCommandIsFailure);
	}

	public static CompletableFuture<Suggestions> getSuggestions(ClientCommandSource source, String command) {
		return BrigadierCommandsImpl.getSuggestions(dispatcher, source, command);
	}

	public static CompletableFuture<List<String>> getLegacySuggestions(ClientCommandSource source, String command) {
		return BrigadierCommandsImpl.getLegacySuggestions(dispatcher, source, command);
	}

	private static class BuildContext implements ClientCommands.BuildContext {
	}
}
