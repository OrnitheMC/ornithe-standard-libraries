package net.ornithemc.osl.commands.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.entity.living.player.LocalClientPlayerEntity;

import net.ornithemc.osl.commands.impl.client.ClientCommandManagerImpl;

@Mixin(LocalClientPlayerEntity.class)
public class LocalClientPlayerEntityMixin {

	@Inject(
		method = "sendChat",
		cancellable = true,
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$commands$runClientCommand(String message, CallbackInfo ci) {
		if (ClientCommandManagerImpl.run(message)) {
			ci.cancel();
		}
	}
}
