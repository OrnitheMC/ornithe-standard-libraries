package net.ornithemc.osl.entrypoints.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

import joptsimple.OptionParser;

import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.client.main.Main;

import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.entrypoints.api.client.ClientLaunchEvents;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;

@Mixin(Main.class)
public class MainMixin {

	@Inject(
		method = "main",
		at = @At(
			value = "HEAD"
		)
	)
	private static void osl$entrypoints$init(CallbackInfo ci) {
		FabricLoader.getInstance().invokeEntrypoints(
			ClientModInitializer.ENTRYPOINT_KEY,
			ClientModInitializer.class,
			ClientModInitializer::initClient
		);
		FabricLoader.getInstance().invokeEntrypoints(
			ModInitializer.ENTRYPOINT_KEY,
			ModInitializer.class,
			ModInitializer::init
		);
	}

	@Inject(
		method = "main",
		at = @At(
			value = "INVOKE",
			target = "Ljoptsimple/OptionParser;nonOptions()Ljoptsimple/NonOptionArgumentSpec;"
		)
	)
	private static void osl$entrypoints$defineOptions(String[] args, CallbackInfo ci, @Local OptionParser parser) {
		ClientLaunchEvents.PARSE_RUN_ARGS.invoker().defineOptions(parser);
	}

	@Inject(
		method = "main",
		at = @At(
			value = "INVOKE",
			target = "Ljava/lang/Runtime;addShutdownHook(Ljava/lang/Thread;)V"
		)
	)
	private static void osl$entrypoints$parseOptions(String[] args, CallbackInfo ci, @Local OptionParser parser) {
		ClientLaunchEvents.PARSE_RUN_ARGS.invoker().parseOptions(parser.parse(args));
	}
}
