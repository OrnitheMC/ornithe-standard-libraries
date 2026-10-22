package net.ornithemc.osl.text.impl;

import net.minecraft.text.Text;

import net.ornithemc.osl.entrypoints.api.ModInitializer;

public class TextResolversInit implements ModInitializer {

	@Override
	public void init() {
		TextResolvers.register(Text.class, new BaseTextResolver());
	}
}
