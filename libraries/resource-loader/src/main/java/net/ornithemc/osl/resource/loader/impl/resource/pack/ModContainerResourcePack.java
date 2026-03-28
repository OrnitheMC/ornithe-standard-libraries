package net.ornithemc.osl.resource.loader.impl.resource.pack;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import net.ornithemc.osl.core.api.util.function.IOSupplier;
import net.ornithemc.osl.resource.loader.api.resource.Resource;

class ModContainerResourcePack extends PathResourcePack {

	final ModContainer mod;
	final String directory;

	private final String id;
	private final String name;

	ModContainerResourcePack(ModContainer mod) {
		this(mod, ".", mod.getMetadata().getId(), mod.getMetadata().getName());
	}

	ModContainerResourcePack(ModContainer mod, String directory, String id, String name) {
		this.mod = mod;
		this.directory = directory;

		this.id = id;
		this.name = name;
	}

	static IOSupplier<InputStream> getIcon(String modId) {
		Optional<ModContainer> mod = FabricLoader.getInstance().getModContainer(modId);

		if (mod.isPresent()) {
			return getIcon(mod.get());
		} else {
			throw new IllegalArgumentException("Mod '" + modId + "' is not loaded!");
		}
	}

	static IOSupplier<InputStream> getIcon(ModContainer mod) {
		Optional<String> pathName = mod.getMetadata().getIconPath(128);
		Optional<Path> path = pathName.flatMap(mod::findPath);

		if (path.isPresent()) {
			return Resource.supplier(path.get());
		} else {
			return () -> {
				throw new FileNotFoundException("Mod '" + mod.getMetadata().getId() + "' has no icon!");
			};
		}
	}

	@Override
	protected List<Path> findRootPaths() {
		return ResourcePacks.getRootPaths(this.mod.getRootPaths(), this.directory);
	}

	@Override
	public String getId() {
		return "mod/" + this.id;
	}

	@Override
	public String getName() {
		return this.name;
	}
}
