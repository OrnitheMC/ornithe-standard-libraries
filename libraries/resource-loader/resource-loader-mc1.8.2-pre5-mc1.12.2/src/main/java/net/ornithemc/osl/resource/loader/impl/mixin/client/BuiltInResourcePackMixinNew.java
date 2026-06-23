package net.ornithemc.osl.resource.loader.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.client.resource.pack.BuiltInResourcePack;
import net.minecraft.resource.Identifier;

import net.ornithemc.osl.resource.loader.impl.adapter.FilePathIdentifier;

@Mixin(BuiltInResourcePack.class)
public class BuiltInResourcePackMixinNew {

	@ModifyVariable(
		method = "openResource",
		at = @At(
			value = "INVOKE",
			target = "Ljava/lang/Class;getResource(Ljava/lang/String;)Ljava/net/URL;",
			shift = Shift.BEFORE // shift to before the try { } block
		)
	)
	private String osl$resource_loader$getResourceFromFilePathIdentifier(String path, @Local Identifier location) {
		if (location instanceof FilePathIdentifier) {
			path = "/" + location.toString();
		}

		return path;
	}
}
