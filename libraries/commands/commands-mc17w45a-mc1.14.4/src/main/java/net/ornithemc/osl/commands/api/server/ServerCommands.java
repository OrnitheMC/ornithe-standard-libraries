package net.ornithemc.osl.commands.api.server;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;

import net.minecraft.server.command.source.CommandSourceStack;

import net.ornithemc.osl.commands.impl.server.ServerCommandsImpl;

/**
 * Utilities for registering server-side commands.
 */
public final class ServerCommands {

	/**
	 * @return the server command dispatcher.
	 */
	public static CommandDispatcher<CommandSourceStack> dispatcher() {
		return ServerCommandsImpl.dispatcher();
	}

	/**
	 * @return a literal command argument builder with the given name.
	 */
	public static LiteralArgumentBuilder<CommandSourceStack> literal(String name) {
		return ServerCommandsImpl.literal(name);
	}

	/**
	 * @return a required argument builder with the given name and the given type.
	 */
	public static <T> RequiredArgumentBuilder<CommandSourceStack, T> argument(String name, ArgumentType<T> type) {
		return ServerCommandsImpl.argument(name, type);
	}

	/**
	 * Details about the selection of commands that should be included.
	 */
	public enum Selection {

		ALL(true, true),
		INTEGRATED(true, false),
		DEDICATED(false, true);

		private final boolean includesIntegrated;
		private final boolean includesDedicated;

		private Selection(boolean integrated, boolean dedicated) {
			this.includesIntegrated = integrated;
			this.includesDedicated = dedicated;
		}

		/**
		 * @return whether commands for integrated server environments should be included.
		 */
		public boolean includesIntegrated() {
			return this.includesIntegrated;
		}

		/**
		 * @return whether commands for dedicated server environments should be included.
		 */
		public boolean includesDedicated() {
			return this.includesDedicated;
		}
	}

	/**
	 * Miscellaneous relevant context for command building and registration.
	 */
	public interface BuildContext {
	}
}
