package net.ornithemc.osl.resource.loader.impl.mixin.client;

import java.io.InputStream;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.At.Shift;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.client.resource.pack.BuiltInResourcePack;
import net.minecraft.resource.Identifier;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.resource.loader.impl.adapter.FilePathIdentifier;

@Mixin(BuiltInResourcePack.class)
public class BuiltInResourcePackMixin {

	@Conditional(minecraftVersion = @Version("<16w32b"))
	@WrapOperation(
		method = "openResource",
		at = @At(
			value = "INVOKE",
			target = "Ljava/lang/Class;getResourceAsStream(Ljava/lang/String;)Ljava/io/InputStream;"
		)
	)
	private InputStream osl$resource_loader$getResourceFromFilePathIdentifier(Class<?> cls, String path, Operation<InputStream> op, @Local Identifier location) {
		if (location instanceof FilePathIdentifier) {
			path = "/" + location.toString();
		}

		return op.call(cls, path);
	}

	@Conditional(minecraftVersion = @Version(">=16w32b"))
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
