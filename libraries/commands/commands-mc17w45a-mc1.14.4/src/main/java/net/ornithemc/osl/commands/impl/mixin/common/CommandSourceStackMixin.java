package net.ornithemc.osl.commands.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.server.command.source.CommandSourceStack;
import net.minecraft.text.Text;

import net.ornithemc.osl.commands.api.server.command.CommandSourceStackExtension;
import net.ornithemc.osl.text.api.TextComponent;
import net.ornithemc.osl.text.api.TextComponents;

@Mixin(CommandSourceStack.class)
public class CommandSourceStackMixin implements CommandSourceStackExtension {

	@Shadow
	private void sendSuccess(Text message, boolean broadcastToOps) { }
	@Shadow
	private void sendFailure(Text message) { }

	@Override
	public void sendSuccess(TextComponent message, boolean broadcastToOps) {
		this.sendSuccess(TextComponents.convert(message, Text.class), broadcastToOps);
	}

	@Override
	public void sendFailure(TextComponent message) {
		this.sendFailure(TextComponents.convert(message, Text.class));
	}
}
