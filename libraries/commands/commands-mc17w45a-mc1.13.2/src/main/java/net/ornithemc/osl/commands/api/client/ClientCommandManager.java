package net.ornithemc.osl.commands.api.client;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;

import net.ornithemc.osl.commands.impl.client.ClientCommandManagerImpl;

public class ClientCommandManager {

	public static CommandDispatcher<ClientCommandSourceStack> getDispatcher() {
		return ClientCommandManagerImpl.getDispatcher();
	}

	public static LiteralArgumentBuilder<ClientCommandSourceStack> literal(String name) {
		return LiteralArgumentBuilder.literal(name);
	}

	public static <T> RequiredArgumentBuilder<ClientCommandSourceStack, T> argument(String name, ArgumentType<T> type) {
		return RequiredArgumentBuilder.argument(name, type);
	}
}
