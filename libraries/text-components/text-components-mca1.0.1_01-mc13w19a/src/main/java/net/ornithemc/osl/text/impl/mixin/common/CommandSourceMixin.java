package net.ornithemc.osl.text.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.server.command.source.CommandSource;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.GameSide;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.text.api.MessageConsumer;
import net.ornithemc.osl.text.api.TextComponent;

@Conditional(minecraftVersion = {
	@Version(value = ">=12w16a", gameSide = GameSide.CLIENT),
	@Version(value = ">=a0.1.2_01", gameSide = GameSide.SERVER)
})
@Mixin(CommandSource.class)
public interface CommandSourceMixin extends MessageConsumer {

	@Shadow
	void sendMessage(String messae);

	@Override
	default void sendMessage(TextComponent message) {
		this.sendMessage(message.buildFormattedString());
	}
}
