package net.ornithemc.osl.lifecycle.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.Bootstrap;

import net.ornithemc.osl.lifecycle.api.LifecycleEvents;

@Mixin(Bootstrap.class)
public class BootstrapMixin {

	@Unique
	private static boolean osl$lifecycle$bootstrapStarted;
	@Unique
	private static boolean osl$lifecycle$bootstrapEnded;

	@Inject(
		method = "init",
		at = @At(
			value = "HEAD"
		)
	)
	private static void osl$lifecycle$bootstrapStart(CallbackInfo ci) {
		if (!osl$lifecycle$bootstrapStarted) {
			osl$lifecycle$bootstrapStarted = true;
			LifecycleEvents.BOOTSTRAP_START.invoker().run();
		}
	}

	@Inject(
		method = "init",
		at = @At(
			value = "TAIL"
		)
	)
	private static void osl$lifecycle$bootstrapEnd(CallbackInfo ci) {
		if (!osl$lifecycle$bootstrapEnded) {
			osl$lifecycle$bootstrapEnded = true;
			LifecycleEvents.BOOTSTRAP_END.invoker().run();
		}
	}
}
