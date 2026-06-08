package net.ornithemc.osl.commands.impl.server.command;

import net.ornithemc.osl.commands.api.server.command.CommandSourceStackExtension;
import net.ornithemc.osl.text.api.TextComponent;

public interface CommandSourceStackExtensionImpl extends CommandSourceStackExtension {

	@Override
	default void sendSuccess(TextComponent message, boolean broadcastToOps) {
		throw new AbstractMethodError();
	}

	@Override
	default void sendFailure(TextComponent message) {
		throw new AbstractMethodError();
	}
}
