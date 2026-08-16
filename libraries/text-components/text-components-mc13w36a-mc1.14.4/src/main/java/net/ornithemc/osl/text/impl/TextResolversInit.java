package net.ornithemc.osl.text.impl;

import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.text.api.TextComponents;

public class TextResolversInit implements ModInitializer {

	@Override
	public void init() {
		TextResolvers.register(LiteralText.class, new BaseTextResolver<>(t -> TextComponents.literal(t.getRawString())));
		TextResolvers.register(TranslatableText.class, new BaseTextResolver<>(t -> TextComponents.translatable(t.getKey(), t.getArgs())));
	}
}
