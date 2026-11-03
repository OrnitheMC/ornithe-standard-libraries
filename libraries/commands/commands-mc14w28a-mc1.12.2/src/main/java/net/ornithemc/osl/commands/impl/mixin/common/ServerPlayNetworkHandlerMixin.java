package net.ornithemc.osl.commands.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.CommandSuggestionsC2SPacket;
import net.minecraft.network.packet.s2c.play.CommandSuggestionsS2CPacket;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.server.network.handler.ServerPlayNetworkHandler;

import net.ornithemc.osl.commands.api.server.command.CommandSourceStack;
import net.ornithemc.osl.commands.impl.server.ServerCommandsImpl;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {

	@Shadow
	private ServerPlayerEntity player;

	@WrapOperation(
		method = "handleCommandSuggestions",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/network/handler/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V"
		)
	)
	private void osl$commands$insertBrigadierCommandSuggestions(ServerPlayNetworkHandler n, Packet<?> p, Operation<Void> op, @Local CommandSuggestionsC2SPacket packet) {
		CommandSourceStack source = this.player.createCommandSourceStack();
		String command = packet.getCommand();

		ServerCommandsImpl.getLegacySuggestions(source, command).thenAccept(brigadierSuggestions -> {
			String[] legacySuggestions = ((CommandSuggestionsS2CPacketAccess) p).accessSuggestions();
			String[] suggestions = new String[legacySuggestions.length + brigadierSuggestions.size()];

			System.arraycopy(legacySuggestions, 0, suggestions, 0, legacySuggestions.length);

			for (int i = 0; i < brigadierSuggestions.size(); i++) {
				suggestions[legacySuggestions.length + i] = brigadierSuggestions.get(i);
			}

			this.player.networkHandler.sendPacket(new CommandSuggestionsS2CPacket(suggestions));
		});
	}
}
