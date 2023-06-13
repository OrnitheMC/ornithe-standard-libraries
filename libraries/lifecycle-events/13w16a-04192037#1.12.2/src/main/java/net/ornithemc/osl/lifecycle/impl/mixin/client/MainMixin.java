package net.ornithemc.osl.lifecycle.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import joptsimple.OptionParser;

import net.minecraft.client.main.Main;

import net.ornithemc.osl.lifecycle.api.client.MinecraftClientEvents;

@Mixin(Main.class)
public class MainMixin {

	@Inject(
		method = "main",
		locals = LocalCapture.CAPTURE_FAILHARD,
		remap = false,
		at = @At(
			value = "INVOKE",
			target = "Ljoptsimple/OptionParser;nonOptions()Ljoptsimple/NonOptionArgumentSpec;"
		)
	)
	private static void osl$lifecycle$defineOptions(String[] args, CallbackInfo ci, OptionParser parser) {
		MinecraftClientEvents.PRE_START.invoker().defineOptions(parser);
	}

	@Inject(
		method = "main",
		locals = LocalCapture.CAPTURE_FAILHARD,
		at = @At(
			value = "INVOKE",
			target = "Ljava/lang/Runtime;addShutdownHook(Ljava/lang/Thread;)V"
		)
	)
	private static void osl$lifecycle$parseOptions(String[] args, CallbackInfo ci, OptionParser parser) {
		MinecraftClientEvents.PRE_START.invoker().parseOptions(parser.parse(args));
	}
}
