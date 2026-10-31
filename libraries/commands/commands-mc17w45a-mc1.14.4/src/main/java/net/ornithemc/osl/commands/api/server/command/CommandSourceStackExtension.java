package net.ornithemc.osl.commands.api.server.command;

import net.ornithemc.osl.text.api.TextComponent;

public interface CommandSourceStackExtension {

	void sendSuccess(TextComponent message, boolean broadcastToOps);

	void sendFailure(TextComponent message);

}
