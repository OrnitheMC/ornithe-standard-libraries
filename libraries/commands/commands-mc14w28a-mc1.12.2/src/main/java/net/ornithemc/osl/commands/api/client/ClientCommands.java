package net.ornithemc.osl.commands.api.client;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;

import net.ornithemc.osl.commands.api.client.command.ClientCommandSource;
import net.ornithemc.osl.commands.impl.client.ClientCommandsImpl;

/**
 * Utilities for registering client-side commands.
 */
public final class ClientCommands {

	/**
	 * @return the client command dispatcher.
	 */
	public static CommandDispatcher<ClientCommandSource> dispatcher() {
		return ClientCommandsImpl.dispatcher();
	}

	/**
	 * @return a literal command argument builder with the given name.
	 */
	public static LiteralArgumentBuilder<ClientCommandSource> literal(String name) {
		return ClientCommandsImpl.literal(name);
	}

	/**
	 * @return a required argument builder with the given name and the given type.
	 */
	public static <T> RequiredArgumentBuilder<ClientCommandSource, T> argument(String name, ArgumentType<T> type) {
		return ClientCommandsImpl.argument(name, type);
	}

	/**
	 * Details about the selection of commands that should be included.
	 */
	public enum Selection {

		ALL(true, true),
		SINGLEPLAYER(true, false),
		MULTIPLAYER(false, true);

		private final boolean includesSingleplayer;
		private final boolean includesMultiplayer;

		private Selection(boolean singleplayer, boolean multiplayer) {
			this.includesSingleplayer = singleplayer;
			this.includesMultiplayer = multiplayer;
		}

		/**
		 * @return whether commands for singleplayer games should be included.
		 */
		public boolean includesSingleplayer() {
			return this.includesSingleplayer;
		}

		/**
		 * @return whether commands for multiplayer games should be included.
		 */
		public boolean includesMultiplayer() {
			return this.includesMultiplayer;
		}
	}

	/**
	 * Miscellaneous relevant context for command building and registration.
	 */
	public interface BuildContext {
	}
}
