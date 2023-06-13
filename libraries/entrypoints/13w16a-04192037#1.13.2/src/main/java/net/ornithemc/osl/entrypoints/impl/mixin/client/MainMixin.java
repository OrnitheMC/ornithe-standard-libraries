package net.ornithemc.osl.entrypoints.impl.mixin.client;

import org.quiltmc.loader.api.entrypoint.EntrypointUtil;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import joptsimple.OptionParser;

import net.minecraft.client.main.Main;

import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.entrypoints.api.client.ClientLaunchEvents;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;

@Mixin(Main.class)
public class MainMixin {

	@Inject(
		method = "main",
		remap = false,
		at = @At(
			value = "HEAD"
		)
	)
	private static void osl$entrypoints$init(CallbackInfo ci) {
		EntrypointUtil.invoke(
			ClientModInitializer.ENTRYPOINT_KEY,
			ClientModInitializer.class,
			ClientModInitializer::initClient
		);
		EntrypointUtil.invoke(
			ModInitializer.ENTRYPOINT_KEY,
			ModInitializer.class,
			ModInitializer::init
		);
	}

	@Inject(
		method = "main",
		remap = false,
		locals = LocalCapture.CAPTURE_FAILHARD,
		at = @At(
			value = "INVOKE",
			remap = false,
			target = "Ljoptsimple/OptionParser;nonOptions()Ljoptsimple/NonOptionArgumentSpec;"
		)
	)
	private static void osl$entrypoints$defineOptions(String[] args, CallbackInfo ci, OptionParser parser) {
		ClientLaunchEvents.PARSE_RUN_ARGS.invoker().defineOptions(parser);
	}

	@Inject(
		method = "main",
		remap = false,
		locals = LocalCapture.CAPTURE_FAILHARD,
		at = @At(
			value = "INVOKE",
			remap = false,
			target = "Ljava/lang/Runtime;addShutdownHook(Ljava/lang/Thread;)V"
		)
	)
	private static void osl$entrypoints$parseOptions(String[] args, CallbackInfo ci, OptionParser parser) {
		ClientLaunchEvents.PARSE_RUN_ARGS.invoker().parseOptions(parser.parse(args));
	}
}
