package net.ornithemc.osl.branding.impl.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import joptsimple.OptionParser;
import joptsimple.OptionSpec;

import net.minecraft.client.main.Main;
import net.ornithemc.osl.branding.impl.BrandingPatch;

@Mixin(Main.class)
public class MainMixin {

	private static OptionSpec<String> osl$branding$versionTypeSpec;

	@Inject(
		method = "main",
		locals = LocalCapture.CAPTURE_FAILHARD,
		remap = false,
		at = @At(
			value = "INVOKE",
			target = "Ljoptsimple/OptionParser;nonOptions()Ljoptsimple/NonOptionArgumentSpec;"
		)
	)
	private static void osl$branding$defineVersionType(String[] args, CallbackInfo ci, OptionParser parser) {
		// we define the option spec in the original parser
		// so that the arguments are not printed in the logs as 'ignored'
		osl$branding$versionTypeSpec = parser.accepts("versionType").withRequiredArg().defaultsTo("release");
	}

	@Inject(
		method = "main",
		locals = LocalCapture.CAPTURE_FAILHARD,
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/Minecraft;run()V"
		)
	)
	private static void osl$branding$parseVersionType(String[] args, CallbackInfo ci, OptionParser parser) {
		// capturing the parsed options is a PITA so we re-parse the arguments
		BrandingPatch.versionType = parser.parse(args).valueOf(osl$branding$versionTypeSpec);
	}
}
