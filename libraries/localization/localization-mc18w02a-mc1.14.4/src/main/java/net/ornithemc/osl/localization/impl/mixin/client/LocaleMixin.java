package net.ornithemc.osl.localization.impl.mixin.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.client.resource.language.Locale;
import net.minecraft.resource.Identifier;
import net.minecraft.resource.Resource;
import net.minecraft.resource.manager.ResourceManager;

import net.ornithemc.osl.localization.impl.Localization;

@Mixin(Locale.class)
public class LocaleMixin {

	@Final
	@Shadow
	private Map<String, String> translations;

	@Shadow
	private void load(List<Resource> resources) { }

	@Inject(
		method = "<init>",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$localization$initLocale(CallbackInfo ci) {
		// ensure OSL's Locale is backed by the same map
		// this way changes to one are reflected in the other
		Localization.getLocale().wrap(this.translations);
	}

	@Inject(
		method = "load(Lnet/minecraft/resource/manager/ResourceManager;Ljava/util/List;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/resource/manager/ResourceManager;getResources(Lnet/minecraft/resource/Identifier;)Ljava/util/List;"
		)
	)
	private void osl$localization$loadExtraTranslations(ResourceManager resourceManager, List<String> languageCodes, CallbackInfo ci,
														@Local(ordinal = 0) String languageCode, @Local(ordinal = 2) String namespace) {
		String path = String.format("lang/%s.lang", languageCode);

		try {
			load(resourceManager.getResources(new Identifier(namespace, path)));
		} catch (IOException ignored) {
		}
	}

	@Inject(
		method = "load(Lnet/minecraft/resource/manager/ResourceManager;Ljava/util/List;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/resource/manager/ResourceManager;getResources(Lnet/minecraft/resource/Identifier;)Ljava/util/List;"
		)
	)
	private void osl$localization$localeReloaded(CallbackInfo ci) {
		Localization.getLocale().setLastUpdateTime();
	}

	@WrapOperation(
		method = "load(Ljava/util/List;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/resource/language/Locale;load(Ljava/io/InputStream;)V"
		)
	)
	private void osl$localization$loadExtraTranslations(Locale instance, InputStream is, Operation<Void> original, @Local Resource resource) throws IOException{
		if (resource.getLocation().getPath().endsWith(".lang")){
			Localization.getLocale().loadFromLang(is);
		} else if (resource.getLocation().getPath().endsWith(".json")) {
			Localization.getLocale().loadFromJson(is);
		} else {
			original.call(instance, is);
		}
	}
}
