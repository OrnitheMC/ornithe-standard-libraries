package net.ornithemc.osl.resource.loader.impl.mixin.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.Map;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import net.minecraft.advancement.Advancement;
import net.minecraft.resource.Identifier;
import net.minecraft.server.AdvancementManager;
import net.minecraft.util.JsonUtils;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.resource.loader.api.resource.ResourcePath;
import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;
import net.ornithemc.osl.resource.loader.api.resource.reload.ResourceReloadListener;
import net.ornithemc.osl.resource.loader.impl.ResourceLoader;

@Conditional(minecraftVersion = @Version(">=17w13a"))
@Mixin(AdvancementManager.class)
public class AdvancementManagerMixin implements ResourceReloadListener {

	@Shadow @Final
	private static Gson GSON;

	private static final ModContainer MINECRAFT = FabricLoader.getInstance().getModContainer("minecraft").get();
	private static final String DIRECTORY = "advancements";
	private static final String EXTENSION = ".json";

	@Shadow
	private boolean error;

	@Unique
	private ResourceManager resourceManager;

	@Shadow
	private void reload() { }

	@Redirect(
		method = "<init>",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/AdvancementManager;reload()V"
		)
	)
	private void osl$resource_loader$cancelReloadOnInit(AdvancementManager advancements) {
		// we trigger this reload through the resource manager so it can be profiled
	}

	@WrapOperation(
		method = "reload",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/AdvancementManager;loadBuiltIn(Ljava/util/Map;)V"
		)
	)
	private void osl$resource_loader$loadFromResources(AdvancementManager manager, Map<Identifier, Advancement.Builder> advancements, Operation<Void> op) {
		if (this.resourceManager == null) {
			op.call(manager, advancements);
		} else {
			this.loadFromResources(advancements);
		}
	}

	@Redirect(
		method = "loadBuiltIn",
		at = @At(
			value = "INVOKE",
			target = "Ljava/nio/file/FileSystems;newFileSystem(Ljava/net/URI;Ljava/util/Map;)Ljava/nio/file/FileSystem;"
		)
	)
	private FileSystem osl$resource_loader$noNewFileSystem(URI uri, Map<String, ?> env) {
		// the mod container also opens file systems on the game jars
		// as the resource loader uses it to find and open resources
		// this call leads to FileSystemAlreadyExists exceptions so
		// we cancel it
		return null;
	}

	@Redirect(
		method = "loadBuiltIn",
		at = @At(
			value = "INVOKE",
			target = "Ljava/nio/file/FileSystem;getPath(Ljava/lang/String;[Ljava/lang/String;)Ljava/nio/file/Path;"
		)
	)
	private Path osl$resource_loader$getPathFromJar(FileSystem fs, String path, String... paths) {
		return MINECRAFT.findPath(ResourcePath.relative(path)).orElse(null);
	}

	@Override
	public void resourcesReloaded(ResourceManager manager) {
		this.resourceManager = manager;
		this.reload();
	}

	@Unique
	private void loadFromResources(Map<Identifier, Advancement.Builder> advancements) {
		this.resourceManager.findResources(DIRECTORY, location -> location.identifier().endsWith(EXTENSION)).forEach((location, resource) -> {
			Identifier identifier = new Identifier(
				location.namespace(),
				location.identifier().substring(DIRECTORY.length() + 1, location.identifier().length() - EXTENSION.length())
			);

			// prioritize advancements from local data/ directory
			if (advancements.containsKey(identifier)) {
				return;
			}

			try (BufferedReader br = resource.openAsReader()) {
				Advancement.Builder advancement = JsonUtils.fromJson(GSON, br, Advancement.Builder.class);

				if (advancement == null) {
					ResourceLoader.LOGGER.error("Couldn't load custom advancement {} from {} as it's empty or null", identifier, location);
				} else {
					advancements.put(identifier, advancement);
				}
			} catch (IllegalArgumentException | JsonParseException e) {
				ResourceLoader.LOGGER.error("Parsing error loading custom advancement {}: {}", identifier, e.getMessage());
				this.error = true;
			} catch (IOException e) {
				ResourceLoader.LOGGER.error("Couldn't read custom advancement {} from {}", identifier, location, e);
				this.error = true;
			}
		});
	}
}
