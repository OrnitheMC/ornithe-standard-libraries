package net.ornithemc.osl.commands.impl.server;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.suggestion.Suggestions;

import net.minecraft.server.command.Command;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;

import net.ornithemc.osl.commands.api.command.SharedSuggestionProvider;
import net.ornithemc.osl.commands.api.server.ServerCommandEvents;
import net.ornithemc.osl.commands.api.server.ServerCommands;
import net.ornithemc.osl.commands.api.server.command.CommandSourceStack;
import net.ornithemc.osl.commands.impl.BrigadierCommandsImpl;
import net.ornithemc.osl.commands.impl.network.CommandsPayload;
import net.ornithemc.osl.networking.api.server.ServerPlayNetworking;

public final class ServerCommandsImpl {

	public static final Logger LOGGER = LogManager.getLogger("OSL|Server Commands");

	private static CommandDispatcher<CommandSourceStack> dispatcher;
	private static Consumer<Command> legacyCommands;

	public static void start(Consumer<Command> legacyCommands, ServerCommands.Selection selection) {
		ServerCommandsImpl.dispatcher = new CommandDispatcher<>();
		ServerCommandsImpl.legacyCommands = legacyCommands;

		ServerCommandsImpl.registerCommands(selection, new BuildContext());
	}

	private static void registerCommands(ServerCommands.Selection selection, ServerCommands.BuildContext context) {
		ServerCommandEvents.REGISTER_COMMANDS.invoker().registerCommands(dispatcher, selection, context);
		ServerCommandEvents.REGISTER_LEGACY_COMMANDS.invoker().registerLegacyCommands(legacyCommands, selection, context);

		dispatcher.findAmbiguities((root, node1, node2, inputs) -> LOGGER.warn("Ambiguity between arguments {} and {} with inputs: {}", dispatcher.getPath(node1), dispatcher.getPath(node2), inputs));
		dispatcher.setConsumer((ctx, success, result) -> ctx.getSource().onCommandComplete(ctx, success, result));
	}

	public static void sendCommands(ServerPlayerEntity player) {
		if (ServerPlayNetworking.isPlayReady(player, CommandsPayload.CHANNEL)) {
			CommandDispatcher<SharedSuggestionProvider> commands = new CommandDispatcher<>();
			CommandSourceStack source = player.createCommandSourceStack();

			fillCommands(commands, source);

			ServerPlayNetworking.send(player, CommandsPayload.CHANNEL, new CommandsPayload(commands.getRoot()));
		}
	}

	public static void fillCommands(CommandDispatcher<SharedSuggestionProvider> commands, CommandSourceStack source) {
		BrigadierCommandsImpl.fillCommands(dispatcher, commands, source);
	}

	public static CommandDispatcher<CommandSourceStack> dispatcher() {
		return dispatcher;
	}

	public static LiteralArgumentBuilder<CommandSourceStack> literal(String name) {
		return LiteralArgumentBuilder.literal(name);
	}

	public static <T> RequiredArgumentBuilder<CommandSourceStack, T> argument(String name, ArgumentType<T> type) {
		return RequiredArgumentBuilder.argument(name, type);
	}

	public static int run(CommandSourceStack source, String command) {
		return run(source, command, true);
	}

	public static Integer run(CommandSourceStack source, String command, boolean unknownCommandIsFailure) {
		return BrigadierCommandsImpl.run(dispatcher, source, command, unknownCommandIsFailure);
	}

	public static CompletableFuture<Suggestions> getSuggestions(CommandSourceStack source, String command) {
		return BrigadierCommandsImpl.getSuggestions(dispatcher, source, command);
	}

	public static CompletableFuture<List<String>> getLegacySuggestions(CommandSourceStack source, String command) {
		return BrigadierCommandsImpl.getLegacySuggestions(dispatcher, source, command);
	}

	private static class BuildContext implements ServerCommands.BuildContext {
	}
}
