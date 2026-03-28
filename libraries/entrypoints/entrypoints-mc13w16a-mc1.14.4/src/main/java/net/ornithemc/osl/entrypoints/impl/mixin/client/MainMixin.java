package net.ornithemc.osl.entrypoints.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.client.main.Main;

import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.entrypoints.api.launch.LaunchEvents;

@Mixin(Main.class)
public class MainMixin {

	@Unique
	private static boolean osl$entrypoints$alreadyAllowsUnrecognizedOptions;

	@Inject(
		method = "main",
		at = @At(
			value = "HEAD"
		)
	)
	private static void osl$entrypoints$init(CallbackInfo ci) {
		FabricLoader.getInstance().invokeEntrypoints(
			ModInitializer.ENTRYPOINT_KEY,
			ModInitializer.class,
			ModInitializer::init
		);
		FabricLoader.getInstance().invokeEntrypoints(
			ClientModInitializer.ENTRYPOINT_KEY,
			ClientModInitializer.class,
			ClientModInitializer::initClient
		);
	}

	@Inject(
		method = "main",
		require = 0, // this inject will fail in 13w23a and below
		at = @At(
			value = "INVOKE",
			target = "Ljoptsimple/OptionParser;allowsUnrecognizedOptions()V"
		)
	)
	private static void osl$entrypoints$allowsUnrecognizedOptions(CallbackInfo ci) {
		osl$entrypoints$alreadyAllowsUnrecognizedOptions = true;
	}

	@Inject(
		method = "main",
		at = @At(
			value = "INVOKE",
			target = "Ljoptsimple/OptionParser;parse([Ljava/lang/String;)Ljoptsimple/OptionSet;"
		)
	)
	private static void osl$entrypoints$defineOptions(String[] args, CallbackInfo ci, @Local OptionParser parser) {
		if (!osl$entrypoints$alreadyAllowsUnrecognizedOptions) {
			// fixes crashes because of, well, unrecognized options, in 13w23a and below
			// Minecraft launchers and Fabric/Quilt Loader itself add several
			parser.allowsUnrecognizedOptions();
		}

		LaunchEvents.PARSE_RUN_ARGS.invoker().defineOptions(parser);
	}

	@Inject(
		method = "main",
		at = @At(
			value = "INVOKE",
			target = "Ljoptsimple/OptionParser;parse([Ljava/lang/String;)Ljoptsimple/OptionSet;",
			shift = Shift.BY,
			by = 2
		)
	)
	private static void osl$entrypoints$acceptOptions(String[] args, CallbackInfo ci, @Local OptionSet options) {
		LaunchEvents.PARSE_RUN_ARGS.invoker().acceptOptions(options);
	}
}
