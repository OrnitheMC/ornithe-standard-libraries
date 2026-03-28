package net.ornithemc.osl.resource.loader.impl.mixin.common;

import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import net.minecraft.server.AdvancementManager;

import net.ornithemc.osl.resource.loader.api.resource.ResourcePath;

@Pseudo // 17w13a+
@Mixin(AdvancementManager.class)
public class AdvancementManagerMixin {

	private static final ModContainer MINECRAFT = FabricLoader.getInstance().getModContainer("minecraft").get();

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
		return MINECRAFT.findPath(ResourcePath.nameOf(path)).orElse(null);
	}
}
