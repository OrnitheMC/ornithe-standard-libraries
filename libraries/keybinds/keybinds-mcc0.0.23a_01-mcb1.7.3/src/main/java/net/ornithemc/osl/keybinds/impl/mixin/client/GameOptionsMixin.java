package net.ornithemc.osl.keybinds.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.options.GameOptions;

import net.ornithemc.osl.keybinds.impl.KeybindRegistryImpl;

@Mixin(GameOptions.class)
public class GameOptionsMixin {

	@Inject(
		method = "<init>(Lnet/minecraft/client/Minecraft;Ljava/io/File;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/options/GameOptions;load()V"
		)
	)
	private void osl$keybinds$registerKeybinds(CallbackInfo ci) {
		KeybindRegistryImpl.init((GameOptions) (Object) this);
	}
}
