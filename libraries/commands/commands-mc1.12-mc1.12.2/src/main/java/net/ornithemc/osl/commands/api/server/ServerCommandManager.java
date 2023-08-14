package net.ornithemc.osl.commands.api.server;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;

import net.ornithemc.osl.commands.impl.server.ServerCommandManagerImpl;

public class ServerCommandManager {

	public static CommandDispatcher<CommandSourceStack> getDispatcher() {
		return ServerCommandManagerImpl.getDispatcher();
	}

	public static LiteralArgumentBuilder<CommandSourceStack> literal(String name) {
		return LiteralArgumentBuilder.literal(name);
	}

	public static <T> RequiredArgumentBuilder<CommandSourceStack, T> argument(String name, ArgumentType<T> type) {
		return RequiredArgumentBuilder.argument(name, type);
	}
}
