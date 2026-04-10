package net.ornithemc.osl.text.impl;

import net.ornithemc.osl.text.api.TextComponent;

public interface TextResolver<T> {

	TextComponent resolve(T o);

}
