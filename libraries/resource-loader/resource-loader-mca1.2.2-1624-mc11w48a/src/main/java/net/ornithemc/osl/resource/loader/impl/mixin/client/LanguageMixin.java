package net.ornithemc.osl.resource.loader.impl.mixin.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Properties;

import org.quiltmc.parsers.json.JsonReader;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.loader.api.metadata.ModMetadata;

import net.minecraft.locale.Language;

import net.ornithemc.osl.resource.loader.api.ModTexturePack;
import net.ornithemc.osl.resource.loader.impl.ResourceLoader;

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
	private void osl$resource_loader$loadModTranslations(CallbackInfo ci) throws IOException {
		String lang = "en_US";
		String path = "/assets/%s/lang/%s.%s";

		for (ModTexturePack pack : ResourceLoader.getDefaultModResourcePacks()) {
			ModMetadata mod = pack.getModMetadata();

			loadTranslationsJson(translations, pack.getResource(String.format(path, mod.getId(), lang, "json")));
			loadTranslationsJson(translations, pack.getResource(String.format(path, mod.getId(), lang.toLowerCase(Locale.ROOT), "json")));
			loadTranslationsLang(translations, pack.getResource(String.format(path, mod.getId(), lang, "lang")));
			loadTranslationsLang(translations, pack.getResource(String.format(path, mod.getId(), lang.toLowerCase(Locale.ROOT), "lang")));
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
