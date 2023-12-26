package net.ornithemc.osl.resource.loader.impl.mixin.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

import org.spongepowered.asm.mixin.Mixin;
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
	private void osl$resource_loader$loadModTranslations(Properties translations, String lang, CallbackInfo ci) {
		for (ModTexturePack pack : ResourceLoader.getDefaultModResourcePacks()) {
			ModMetadata mod = pack.getModMetadata();

			try (BufferedReader br = new BufferedReader(new InputStreamReader(pack.getResource("/assets/" + mod.getId() + "/lang/" + lang + ".lang")))) {
				String line;
				while ((line = br.readLine()) != null) {
					line = line.trim();
					if (!line.startsWith("#")) {
						String[] parts = line.split("=");
						if (parts != null && parts.length == 2) {
							translations.setProperty(parts[0], parts[1]);
						}
					}
				}
			} catch (Exception e) {
			}
		}
	}
}
