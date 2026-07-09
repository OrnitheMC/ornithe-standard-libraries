package net.ornithemc.osl.networking.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.client.server.LocalServer;

import net.ornithemc.osl.networking.impl.access.LocalServerAccess;

@Mixin(LocalServer.class)
public class LocalServerMixin implements LocalServerAccess {

	@Unique
	private String worldSaveName;

	@Inject(
		method = "start",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$networking$start(CallbackInfo ci, @Local String worldSaveName) {
		this.worldSaveName = worldSaveName;
	}

	@Override
	public String osl$networking$worldSaveName() {
		return this.worldSaveName;
	}
}
