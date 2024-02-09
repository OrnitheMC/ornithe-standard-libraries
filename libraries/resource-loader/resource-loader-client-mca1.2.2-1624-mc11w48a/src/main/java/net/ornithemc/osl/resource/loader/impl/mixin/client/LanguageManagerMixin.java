package net.ornithemc.osl.resource.loader.impl.mixin.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Properties;

import org.quiltmc.parsers.json.JsonReader;
import org.spongepowered.asm.mixin.Mixin;
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

	@Inject(
		method = "loadTranslations(Ljava/util/Properties;Ljava/lang/String;)V",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$resource_loader$loadModTranslations(Properties translations, String lang, CallbackInfo ci) throws IOException {
		for (ModTexturePack pack : ResourceLoader.getDefaultModResourcePacks()) {
			ModMetadata mod = pack.getModMetadata();
			String dir = String.format("/assets/%s/lang", mod.getId());

			loadTranslationsJson(translations, pack.getResource(String.format("/%s.json", dir, lang)));
			loadTranslationsJson(translations, pack.getResource(String.format("/%s.json", dir, lang.toLowerCase(Locale.ROOT))));
			loadTranslationsLang(translations, pack.getResource(String.format("/%s.lang", dir, lang)));
			loadTranslationsLang(translations, pack.getResource(String.format("/%s.lang", dir, lang.toLowerCase(Locale.ROOT))));
		}
	}

	@Unique
	private void loadTranslationsJson(Properties translations, InputStream is) throws IOException {
		if (is != null){
			try (JsonReader reader = JsonReader.json(new InputStreamReader(is))) {
				reader.beginObject();
				while (reader.hasNext()) {
					translations.setProperty(reader.nextName(), reader.nextString());
				}
				reader.endObject();
			}
		}
	}

	@Unique
	private void loadTranslationsLang(Properties translations, InputStream is) throws IOException {
		if (is != null){
			try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
				String line;
				while ((line = br.readLine()) != null) {
					line = line.trim();
					if (line.isEmpty() || line.startsWith("#")) {
						continue;
					}
					String[] args = line.split("=", 2);
					if (args.length != 2) {
						continue;
					}
					translations.setProperty(args[0], args[1]);
				}
			}
		}
	}
}
