package net.ornithemc.osl.commands.impl.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;

import net.minecraft.server.command.source.CommandSourceStack;

import net.ornithemc.osl.commands.api.server.ServerCommandEvents;
import net.ornithemc.osl.commands.api.server.ServerCommands;

public final class ServerCommandsImpl {

	public static final Logger LOGGER = LogManager.getLogger("OSL|Server Commands");

	private static CommandDispatcher<CommandSourceStack> dispatcher;

	public static void init(CommandDispatcher<CommandSourceStack> dispatcher, ServerCommands.Selection selection) {
		ServerCommandsImpl.dispatcher = dispatcher;

		registerCommands(selection, new BuildContext());
	}

	private static void registerCommands(ServerCommands.Selection selection, ServerCommands.BuildContext context) {
		ServerCommandEvents.REGISTER_COMMANDS.invoker().registerCommands(dispatcher, selection, context);
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

	private static class BuildContext implements ServerCommands.BuildContext {
	}
}
