package net.ornithemc.osl.resource.loader.impl.mixin.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.client.resource.language.Locale;
import net.minecraft.client.resource.manager.ResourceManager;
import net.minecraft.resource.Identifier;
import net.minecraft.resource.Resource;

@Mixin(Locale.class)
public abstract class LocaleMixin {

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
			String.format("lang/%s.lang", languageCode),
			String.format("lang/%s.json", languageCode.toLowerCase(java.util.Locale.ROOT)),
			String.format("lang/%s.lang", languageCode.toLowerCase(java.util.Locale.ROOT))
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
			target = "Lnet/minecraft/client/resource/language/Locale;load(Ljava/io/InputStream;)V"
		)
	)
	private void osl$resource_loader$loadTranslationFiles(Locale instance, InputStream is, Operation<Void> original, @Local Resource resource) throws IOException{
		if (resource.getLocation().getPath().endsWith(".lang")){
			loadLang(is);
		} else {
			original.call(instance, is); // load .json
		}
	}

	@Unique
	private void loadLang(InputStream stream) throws IOException {
		if (stream == null) {
			return;
		}

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
			reader
				.lines()
				.filter(l -> !l.startsWith("#"))
				.map(line -> line.split("=", 2))
				.forEach(key -> translations.put(key[0], key[1]));
		}
	}
}
