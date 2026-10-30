package net.ornithemc.osl.text.impl;

import net.ornithemc.osl.text.api.TextComponent;

public interface TextConverter<T> {

	T convert(TextComponent text);

}
