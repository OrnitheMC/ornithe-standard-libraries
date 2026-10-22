package net.ornithemc.osl.text.impl;

import net.ornithemc.osl.text.api.MessageConsumer;
import net.ornithemc.osl.text.api.TextComponent;

public interface MessageConsumerImpl extends MessageConsumer {

	@Override
	default void sendMessage(TextComponent message) {
		throw new AbstractMethodError();
	}
}
