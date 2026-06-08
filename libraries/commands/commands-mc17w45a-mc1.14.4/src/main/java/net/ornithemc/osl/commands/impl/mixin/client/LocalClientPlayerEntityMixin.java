package net.ornithemc.osl.commands.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.entity.living.player.LocalClientPlayerEntity;
import net.minecraft.client.network.handler.ClientPlayNetworkHandler;

import net.ornithemc.osl.commands.api.client.command.ClientCommandSource;
import net.ornithemc.osl.commands.impl.client.ClientCommandsImpl;

@Mixin(LocalClientPlayerEntity.class)
public class LocalClientPlayerEntityMixin {

	@Shadow
	private ClientPlayNetworkHandler networkHandler;

	@Inject(
		method = "sendChat",
		cancellable = true,
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$commands$runClientCommand(String message, CallbackInfo ci) {
		if (message.charAt(0) == '/') {
			ClientCommandSource source = (ClientCommandSource) this.networkHandler.getSuggestionProvider();
			Integer result = ClientCommandsImpl.run(source, message, false);

			if (result != null) {
				ci.cancel();
			}
		}
	}
}
