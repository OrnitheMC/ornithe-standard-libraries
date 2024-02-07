package net.ornithemc.osl.resource.loader.impl.mixin.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.quiltmc.parsers.json.JsonReader;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.client.resource.Resource;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.client.resource.manager.ResourceManager;
import net.minecraft.resource.Identifier;

@Mixin(TranslationStorage.class)
public abstract class TranslationStorageMixin {

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
	private void osl$resource_loader$loadJsonFiles(ResourceManager manager, List<String> languageCodes, CallbackInfo ci, Iterator<String> it, String languageCode, String originalPath, String namespace) throws IOException {
		String[] paths = new String[]{originalPath, String.format("lang/%s.json", languageCode),
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
		if (resource.getLocation().getPath().endsWith(".json")){
			loadJson(is);
		} else {
			load(is); // load .lang
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
	private void loadJson(InputStream stream) {
		if (stream == null) {
			return;
		}
		JsonReader reader = JsonReader.json(new InputStreamReader(stream));

		Map<String, String> entries = new HashMap<>();
		try {
			reader.beginObject();
			while (reader.hasNext()) {
				entries.put(reader.nextName(), reader.nextString());
			}
			reader.endObject();
		} catch (IOException ignored) {
		}
		translations.putAll(entries);
	}
}
