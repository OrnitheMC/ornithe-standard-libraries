package net.ornithemc.osl.resource.loader.impl.resource.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
	
import net.ornithemc.osl.core.impl.util.ModLoader;
import net.ornithemc.osl.resource.loader.api.resource.pack.PackPosition;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackRepository;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackSummary;
import net.ornithemc.osl.resource.loader.impl.resource.pack.BuiltInModResourcesPack;
import net.ornithemc.osl.resource.loader.impl.resource.pack.ModResourcePack;

public class BundledModResourcePacks implements ResourcePackRepository.Source {

	public static final String BUILT_IN_MOD_RESOURCES_ID = ModLoader.resolve().id() + "-mod-resources";

	private static final Map<String, CustomModResources> CUSTOM_MOD_RESOURCES = new LinkedHashMap<>();

	public static void registerBundledModResourcePack(String id, String name, ModContainer mod, String directory) {
		if (CUSTOM_MOD_RESOURCES.containsKey(id)) {
			throw new IllegalStateException("Duplicate bundled mod resources '" + id + "'");
		}

		CUSTOM_MOD_RESOURCES.put(id, new CustomModResources(id, name, mod, directory));
	}

	@Override
	public void loadResourcePacks(Consumer<ResourcePackSummary> consumer) {
		List<ResourcePack> packs = new ArrayList<>();

		for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
			if ("builtin".equals(mod.getMetadata().getType())) {
				continue;
			}

			packs.add(new ModResourcePack(mod));
		}

		ResourcePack compositePack = new BuiltInModResourcesPack(packs);
		ResourcePackSummary compositeSummary = ResourcePackSummary.create(
			compositePack,
			BUILT_IN_MOD_RESOURCES_ID,
			true,
			false,
			PackPosition.BOTTOM
		);

		consumer.accept(compositeSummary);

		for (CustomModResources customResources : CUSTOM_MOD_RESOURCES.values()) {
			ResourcePack pack = customResources.buildResourcePack();
			ResourcePackSummary summary = ResourcePackSummary.create(
				pack,
				customResources.id,
				true,
				false,
				PackPosition.BOTTOM
			);

			if (summary != null) {
				consumer.accept(summary);
			}
		}
	}

	private static class CustomModResources {

		final String id;
		final String name;
		final ModContainer mod;
		final String directory;

		CustomModResources(String id, String name, ModContainer mod, String directory) {
			this.id = id;
			this.name = name;
			this.mod = mod;
			this.directory = directory;
		}

		ResourcePack buildResourcePack() {
			return new ModResourcePack(this.mod, this.directory, this.name);
		}
	}
}
