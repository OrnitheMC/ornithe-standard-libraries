package net.ornithemc.osl.localization.impl.mixin.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.client.resource.Resource;
import net.minecraft.client.resource.language.Locale;
import net.minecraft.client.resource.manager.ResourceManager;
import net.minecraft.resource.Identifier;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.localization.impl.Localization;
import net.ornithemc.osl.resource.loader.impl.resource.ResourceLocationAccess;
import net.ornithemc.osl.resource.loader.impl.resource.pack.ResourcePacks;

@Mixin(Locale.class)
public class LocaleMixin {

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
		Localization.getLocale().wrap(this.translations);
	}

	@Inject(
		method = "load(Lnet/minecraft/client/resource/manager/ResourceManager;Ljava/util/List;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/resource/language/Locale;load(Ljava/util/List;)V",
			shift = Shift.AFTER // ensure consistent loading order (first .lang, then .json)
		)
	)
	private void osl$localization$loadExtraTranslations(ResourceManager resourceManager, List<String> languageCodes, CallbackInfo ci,
														@Local(ordinal = 0) String languageCode, @Local(ordinal = 2) String namespace) {
		String pathFormat = "lang/%s%s";
		String[] paths;

		if (ResourcePacks.getSupportedFormat() < 3) {
			paths = new String[] {
				String.format(pathFormat, languageCode, ".json"),
				String.format(pathFormat, languageCode.toLowerCase(java.util.Locale.ROOT), ".lang"),
				String.format(pathFormat, languageCode.toLowerCase(java.util.Locale.ROOT), ".json")
			};
		} else {
			paths = new String[] {
				String.format(pathFormat, languageCode, ".json")
			};
		}
			

		for (String path : paths) {
			try {
				load(resourceManager.getResources(new Identifier(namespace, path)));
			} catch (IOException ignored) {
			}
		}
	}

	@Inject(
		method = "load(Lnet/minecraft/client/resource/manager/ResourceManager;Ljava/util/List;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/resource/manager/ResourceManager;getResources(Lnet/minecraft/resource/Identifier;)Ljava/util/List;"
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
		NamespacedIdentifier location = this.resourceLocation(resource);

		if (location.identifier().endsWith(".lang")){
			Localization.getLocale().loadFromLang(is);
		} else if (location.identifier().endsWith(".json")) {
			Localization.getLocale().loadFromJson(is);
		} else {
			original.call(instance, is);
		}
	}

	@Unique
	private NamespacedIdentifier resourceLocation(Resource resource) throws IOException {
		// in 14w21b and earlier, there is no accessor method in Vanilla!
		if (resource instanceof ResourceLocationAccess) {
			return ((ResourceLocationAccess) resource).resourceLocation();
		} else {
			return resource.getLocation();
		}
	}
}
