package net.ornithemc.osl.resource.loader.impl.mixin.client;

import java.io.PrintWriter;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.client.options.GameOptions;

import net.ornithemc.osl.resource.loader.impl.access.GameOptionsAccess;

@Mixin(GameOptions.class)
public class GameOptionsMixin implements GameOptionsAccess {

	private static final String SKIN_OPTION = "skin";
	private static final String SKIN_DEFAULT_VALUE = "Default";

	@Unique
	private String skin;

	@Inject(
		method = "<init>*",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/options/GameOptions;load()V"
		)
	)
	private void osl$resource_loader$initSkinOption(CallbackInfo ci) {
		this.skin = SKIN_DEFAULT_VALUE;
	}

	@Inject(
		method = "load",
		at = @At(
			value = "INVOKE",
			target = "Ljava/lang/String;split(Ljava/lang/String;)[Ljava/lang/String;",
			shift = Shift.BY,
			by = 2
		)
	)
	private void osl$resource_loader$loadSkinOption(CallbackInfo ci, @Local String[] option) {
		if (option[0].equals(SKIN_OPTION)) {
			this.skin = option[1];
		}
	}

	@Inject(
		method = "save",
		at = @At(
			value = "INVOKE",
			target = "Ljava/io/PrintWriter;println(Ljava/lang/String;)V",
			ordinal = 10, // after fancyGraphics option
			shift = Shift.AFTER
		)
	)
	private void osl$resource_loader$skinSkinOption(CallbackInfo ci, @Local PrintWriter writer) {
		writer.println(String.format("%s:%s", SKIN_OPTION, this.skin));
	}

	@Override
	public String osl$resource_loader$getSkin() {
		return this.skin;
	}

	@Override
	public void osl$resource_loader$setSkin(String skin) {
		this.skin = skin;
	}
}
