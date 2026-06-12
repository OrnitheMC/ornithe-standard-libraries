package net.ornithemc.osl.localization.impl.mixin.client;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.resource.language.LanguageManager;

import net.ornithemc.osl.localization.api.language.Language;
import net.ornithemc.osl.localization.impl.Localization;

@Mixin(LanguageManager.class)
public class LanguageManagerMixin {

	@Shadow
	private Map<String, Language> languages;

	@Shadow
	private String currentCode;

	@Inject(
		method = "<init>",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$localization$initLanguageManager(CallbackInfo ci) {
		Localization.getLanguageManager().wrap(this.languages, net.minecraft.client.resource.language.Language::new);
	}

	@Inject(
		method = "setLanguage",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$localization$setLanguage(CallbackInfo ci) {
		Localization.getLanguageManager().setSelectedLanguage(this.currentCode);
	}
}
