package net.ornithemc.osl.localization.impl.mixin.client;

import java.util.Properties;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.locale.Language;

import net.ornithemc.osl.localization.impl.Localization;
import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;

@Mixin(Language.class)
public class LanguageMixin {

	@Shadow
	private static Language INSTANCE;

	@Shadow
	private Properties translations;
	@Shadow
	private String currentCode;

	@Inject(
		method = "<clinit>",
		at = @At(
			value = "TAIL"
		)
	)
	private static void osl$localization$initLocale(CallbackInfo ci) {
		Localization.getLocale().wrap(((LanguageAccess) INSTANCE).accessTranslations());
	}

	@Inject(
		method = "load",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$localization$reloadLanguageManager(CallbackInfo ci) {
		// each ServerPlayerEntity also holds an instance of this class
		if ((Language) (Object) this == INSTANCE) {
			Localization.reloadLanguageManager();
		}
	}

	@Inject(
		method = "loadLanguage",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$localization$setLanguage(CallbackInfo ci) {
		// each ServerPlayerEntity also holds an instance of this class
		if ((Language) (Object) this == INSTANCE) {
			Localization.getLanguageManager().setSelectedLanguage(this.currentCode);
		}
	}

	@Inject(
		method = "loadLanguage",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/locale/Language;loadTranslations(Ljava/util/Properties;Ljava/lang/String;)V"
		)
	)
	private void osl$localization$wrapTranslations(CallbackInfo ci, @Local String language, @Local Properties translations) {
		// each ServerPlayerEntity also holds an instance of this class
		if ((Language) (Object) this == INSTANCE) {
			// the translations map is replaced with each reload
			Localization.getLocale().wrap(translations);
		}
	}

	@WrapOperation(
		method = "loadLanguage",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/locale/Language;loadTranslations(Ljava/util/Properties;Ljava/lang/String;)V"
		)
	)
	private void osl$localization$loadExtraTranslations(Language self, Properties translations, String language, Operation<Void> op) {
		// each ServerPlayerEntity also holds an instance of this class
		if ((Language) (Object) this == INSTANCE) {
			Localization.getLocale().loadLanguage(ResourceManager.client(), language);
		}

		// no need to run the original operation
		// op.call(self, translations, language);
	}

	@Inject(
		method = "loadLanguage",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$localization$localeReloaded(CallbackInfo ci) {
		// each ServerPlayerEntity also holds an instance of this class
		if ((Language) (Object) this == INSTANCE) {
			Localization.getLocale().setLastUpdateTime();
		}
	}
}
