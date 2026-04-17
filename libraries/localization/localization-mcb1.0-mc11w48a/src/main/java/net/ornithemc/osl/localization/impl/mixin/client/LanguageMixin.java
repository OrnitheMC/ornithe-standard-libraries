package net.ornithemc.osl.localization.impl.mixin.client;

import java.io.IOException;
import java.util.Properties;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.locale.Language;

import net.ornithemc.osl.localization.impl.Localization;

@Mixin(Language.class)
public class LanguageMixin {

	@Shadow
	private Properties translations;

	@Inject(
		method = "<init>",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$localization$initLocalization(CallbackInfo ci) throws IOException {
		Localization.getLocale().wrap(this.translations);

		Localization.reloadLanguageManager();
		Localization.reloadLocale();
	}
}
