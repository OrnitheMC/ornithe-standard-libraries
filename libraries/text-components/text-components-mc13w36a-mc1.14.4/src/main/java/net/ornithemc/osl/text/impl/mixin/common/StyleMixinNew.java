package net.ornithemc.osl.text.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.text.Style;

import net.ornithemc.osl.text.impl.access.StyleAccess;

@Mixin(Style.class)
public abstract class StyleMixinNew implements StyleAccess {

	@Shadow
	private String insertion;

	@Override
	public String osl$text$insertion() {
		return this.insertion;
	}
}
