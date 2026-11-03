package net.ornithemc.osl.text.impl;

import com.mojang.brigadier.Message;

import net.ornithemc.osl.text.api.TextComponent;
import net.ornithemc.osl.text.api.TextComponents;

public class MessageResolver implements TextResolver<Message> {

	@Override
	public TextComponent resolve(Message message) {
		if (message instanceof TextComponent) {
			return (TextComponent) message;
		} else {
			return TextComponents.literal(message.getString());
		}
	}
}
