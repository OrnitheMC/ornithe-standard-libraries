package net.ornithemc.osl.resource.loader.impl.resource.pack;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import net.ornithemc.osl.core.api.util.function.IOSupplier;
import net.ornithemc.osl.resource.loader.api.resource.Resource;

public abstract class ModContainerResourcePack extends PathResourcePack {

	protected final ModContainer mod;
	protected final String directory;

	private final String name;

	private final IOSupplier<InputStream> metadata;
	private final IOSupplier<InputStream> icon;

	public ModContainerResourcePack(ModContainer mod, String directory, String name) {
		this.mod = mod;
		this.directory = directory;

		this.name = name;

		this.metadata = this.generateMetadataFile();
		this.icon = this.generateIconFile();
	}

	protected abstract IOSupplier<InputStream> generateMetadataFile();

	protected abstract IOSupplier<InputStream> generateIconFile();

	protected static IOSupplier<InputStream> getIcon(String modId) {
		Optional<ModContainer> mod = FabricLoader.getInstance().getModContainer(modId);

		if (mod.isPresent()) {
			return getIcon(mod.get());
		} else {
			throw new IllegalArgumentException("Mod '" + modId + "' is not loaded!");
		}
	}

	protected static IOSupplier<InputStream> getIcon(ModContainer mod) {
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
	public String getName() {
		return this.name;
	}

	@Override
	public InputStream getResource(String path) throws IOException {
		try {
			return super.getResource(path);
		} catch (FileNotFoundException e) {
			if (METADATA_FILE.equals(path)) {
				return this.metadata.get();
			}
			if (ICON_FILE.equals(path)) {
				return this.icon.get();
			}

			throw e;
		}
	}
}
