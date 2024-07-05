package net.ornithemc.osl.resource.loader.impl.mixin.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.quiltmc.parsers.json.JsonReader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.client.resource.Resource;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.client.resource.manager.ResourceManager;
import net.minecraft.resource.Identifier;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TranslationStorage.class)
public abstract class TranslationStorageMixin {

	@Final
	@Shadow
	private Map<String, String> translations;

	@Shadow
	protected abstract void load(List<Resource> resources);

	@Inject(
		method = "load(Lnet/minecraft/client/resource/manager/ResourceManager;Ljava/util/List;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/resource/manager/ResourceManager;getResources(Lnet/minecraft/resource/Identifier;)Ljava/util/List;"
		)
	)
	private void osl$resource_loader$loadTranslationFiles(CallbackInfo ci,
														  @Local(argsOnly = true) ResourceManager resourceManager,
														  @Local(ordinal = 0) String languageCode,
														  @Local(ordinal = 2) String namespace) {
		String[] paths = new String[] {
			String.format("lang/%s.json", languageCode),
			String.format("lang/%s.lang", languageCode.toLowerCase(Locale.ROOT)),
			String.format("lang/%s.json", languageCode.toLowerCase(Locale.ROOT))
		};
		for (String path : paths) {
			try {
				this.load(resourceManager.getResources(new Identifier(namespace, path)));
			} catch (IOException ignored) {
			}
		}
	}

	@WrapOperation(
		method = "load(Ljava/util/List;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/resource/language/TranslationStorage;load(Ljava/io/InputStream;)V"
		)
	)
	private void osl$resource_loader$loadTranslationFiles(TranslationStorage instance, InputStream is, Operation<Void> original, @Local Resource resource) throws IOException{
		if (resource.getLocation().getPath().endsWith(".lang")){
			original.call(instance, is); // load .lang
		} else {
			loadJson(is);
		}
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
