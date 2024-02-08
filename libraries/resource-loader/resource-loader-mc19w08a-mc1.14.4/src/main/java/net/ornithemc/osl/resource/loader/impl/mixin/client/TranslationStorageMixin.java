package net.ornithemc.osl.resource.loader.impl.mixin.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.client.resource.manager.ResourceManager;
import net.minecraft.resource.Identifier;
import net.minecraft.resource.Resource;

@Mixin(TranslationStorage.class)
public class TranslationStorageMixin {

	@Final
	@Shadow
	private Map<String, String> translations;

	@Shadow
	private void load(List<Resource> resources) throws IOException {
	}

	@Shadow
	private void load(InputStream is) throws IOException {
	}

	@Inject(
		method = "load(Lnet/minecraft/client/resource/manager/ResourceManager;Ljava/util/List;)V",
		locals = LocalCapture.CAPTURE_FAILHARD,
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/resource/language/TranslationStorage;load(Ljava/util/List;)V"
		)
	)
	private void osl$resource_loader$loadJsonFiles(ResourceManager manager, List<String> languageCodes, CallbackInfo ci, Iterator<String> languageCodesIt, String languageCode, String originalPath, Iterator<String> namespacesIt, String namespace) throws IOException{
		String[] paths = new String[]{originalPath, String.format("lang/%s.lang", languageCode),
			String.format("lang/%s.json", languageCode.toLowerCase(Locale.ROOT)),
			String.format("lang/%s.lang", languageCode.toLowerCase(Locale.ROOT))};
		for (String s : paths){
			load(manager.getResources(new Identifier(namespace, s)));
		}
	}

	@Redirect(
		method = "load(Lnet/minecraft/client/resource/manager/ResourceManager;Ljava/util/List;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/resource/language/TranslationStorage;load(Ljava/util/List;)V"
		)
	)
	private void osl$resource_loader$noOpOriginalLoadCall(TranslationStorage instance, List<Resource> resources) {
		// noop
	}

	@Inject(
		method = "load(Ljava/util/List;)V",
		locals = LocalCapture.CAPTURE_FAILHARD,
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/resource/language/TranslationStorage;load(Ljava/io/InputStream;)V"
		)
	)
	private void osl$resource_loader$loadJsonFiles$2(List<Resource> resources, CallbackInfo ci, Iterator<Resource> it, Resource resource, InputStream is) throws IOException{
		if (resource.getLocation().getPath().endsWith(".lang")){
			loadLang(is);
		} else {
			load(is); // load .json
		}
	}

	@Redirect(
		method = "load(Ljava/util/List;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/resource/language/TranslationStorage;load(Ljava/io/InputStream;)V"
		)
	)
	private void osl$resource_loader$noOpOriginalLoadCall$2(TranslationStorage instance, InputStream is){
		// noop
	}

	@Unique
	private void loadLang(InputStream stream) throws IOException {
		if (stream == null) {
			return;
		}

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
			reader.lines().filter(l -> !l.startsWith("#"))
				.map(line -> line.split("=", 2)).forEach(key -> translations.put(key[0], key[1]));
		}
	}
}
