package net.ornithemc.osl.text.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.server.command.source.CommandSource;
import net.minecraft.text.Text;

import net.ornithemc.osl.text.api.MessageConsumer;
import net.ornithemc.osl.text.api.TextComponent;
import net.ornithemc.osl.text.api.TextComponents;

@Mixin(CommandSource.class)
public interface CommandSourceMixin extends MessageConsumer {

	@Shadow
	void sendMessage(Text messae);

	@Override
	default void sendMessage(TextComponent message) {
		this.sendMessage(TextComponents.convert(message, Text.class));
	}
}
