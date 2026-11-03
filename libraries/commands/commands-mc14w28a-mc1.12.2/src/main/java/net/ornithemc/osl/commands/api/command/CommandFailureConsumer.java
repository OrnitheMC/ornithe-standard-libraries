package net.ornithemc.osl.commands.api.command;

import net.ornithemc.osl.text.api.TextComponent;

public interface CommandFailureConsumer {

	void sendFailure(TextComponent message);

}
