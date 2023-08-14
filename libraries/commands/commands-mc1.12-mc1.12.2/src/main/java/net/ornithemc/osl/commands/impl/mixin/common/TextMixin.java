package net.ornithemc.osl.commands.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.mojang.brigadier.Message;

import net.minecraft.text.Text;

@Mixin(Text.class)
public interface TextMixin extends Message {

	@Shadow String buildString();

	@Override
	default String getString() {
		return buildString();
	}
}
