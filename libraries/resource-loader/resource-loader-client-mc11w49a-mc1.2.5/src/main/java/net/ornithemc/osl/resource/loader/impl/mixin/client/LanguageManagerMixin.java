package net.ornithemc.osl.resource.loader.impl.mixin.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.quiltmc.parsers.json.JsonReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.loader.api.metadata.ModMetadata;

import net.minecraft.locale.LanguageManager;

import net.ornithemc.osl.resource.loader.api.ModTexturePack;
import net.ornithemc.osl.resource.loader.impl.ResourceLoader;

@Mixin(LanguageManager.class)
public class LanguageManagerMixin {

	@Shadow
	private Properties translations;

	@Inject(
		method = "loadTranslations(Ljava/util/Properties;Ljava/lang/String;)V",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$resource_loader$loadModTranslations(Properties translations, String lang, CallbackInfo ci) {
		String[] validFiles = new String[]{"/assets/%s/lang/%s.lang", "/assets/%s/lang/%s.json"};
		for (ModTexturePack pack : ResourceLoader.getDefaultModResourcePacks()) {
			ModMetadata mod = pack.getModMetadata();

			for (String path : validFiles){
				if (path.endsWith(".json")){
					addJsonFile(pack.getResource(String.format(path, mod.getId(), lang)));
					addJsonFile(pack.getResource(String.format(path, mod.getId(), lang.toLowerCase(Locale.ROOT))));
				} else if (path.endsWith(".lang")){
					addLangFile(pack.getResource(String.format(path, mod.getId(), lang)));
					addLangFile(pack.getResource(String.format(path, mod.getId(), lang.toLowerCase(Locale.ROOT))));
				}
			}
		}
	}

	@Unique
	private void addJsonFile(InputStream stream){
		if (stream == null){
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
		addLanguage(entries);
	}

	@Unique
	private void addLangFile(InputStream stream){
		if (stream == null){
			return;
		}
		addLanguage(new BufferedReader(new InputStreamReader(stream)).lines()
			.filter(line -> !line.startsWith("#"))
			.map(line -> line.split("="))
			.collect(Collectors.toMap(line -> line[0], line -> line[1])));
	}

	@Unique
	private void addLanguage(Map<String, String> translations){
		translations.forEach(this.translations::setProperty);
	}
}
