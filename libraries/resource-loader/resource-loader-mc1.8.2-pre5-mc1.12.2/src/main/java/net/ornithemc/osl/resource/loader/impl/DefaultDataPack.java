package net.ornithemc.osl.resource.loader.impl;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.fabricmc.loader.api.FabricLoader;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.function.IOSupplier;
import net.ornithemc.osl.core.impl.util.MinecraftVersion;
import net.ornithemc.osl.resource.loader.api.resource.ResourceType;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourceConsumer;
import net.ornithemc.osl.resource.loader.impl.resource.pack.ModContainerResourcePack;
import net.ornithemc.osl.resource.loader.impl.resource.pack.ResourcePacks;
import net.ornithemc.osl.text.api.TextComponents;

public class DefaultDataPack extends ModContainerResourcePack {

	private static final boolean LOOT_TABLES_EXIST = MinecraftVersion.resolve().compareTo("15w43a") >= 0;
	private static final boolean FUNCTIONS_EXIST = MinecraftVersion.resolve().compareTo("1.12-pre1") >= 0;
	private static final boolean ADVANCEMENTS_EXIST = MinecraftVersion.resolve().compareTo("17w13a") >= 0;

	private final Set<String> directoriesToRedirect = new HashSet<>();
	private final boolean shouldRedirectSomeDirectories;

	public DefaultDataPack() {
		super(FabricLoader.getInstance().getModContainer("minecraft").get(), ".", "Default");

//		this.directoriesToRedirect.add("recipes"); // TODO: support re-loading crafting recipes
		if (LOOT_TABLES_EXIST) {
			this.directoriesToRedirect.add("loot_tables");
		}
		if (FUNCTIONS_EXIST) {
			this.directoriesToRedirect.add("advancements");
		}
		if (ADVANCEMENTS_EXIST) {
			this.directoriesToRedirect.add("structures");
		}

		this.shouldRedirectSomeDirectories = !this.directoriesToRedirect.isEmpty();
	}

	@Override
	protected IOSupplier<InputStream> generateMetadataFile() {
		return ResourcePacks.generateMetadataFile(TextComponents.literal("The default behavior of Minecraft"));
	}

	@Override
	protected IOSupplier<InputStream> generateIconFile() {
		return ModContainerResourcePack.getIcon(this.mod);
	}

	@Override
	protected Map<ResourceType, Set<String>> findNamespaces() {
		return Collections.singletonMap(ResourceType.SERVER_DATA, Collections.singleton("minecraft"));
	}

	private boolean shouldRedirectToAssets(String path) {
		return this.directoriesToRedirect.stream().anyMatch(dir -> path.equals(dir) || path.startsWith(dir + "/"));
	}

	private ResourceType updateResourceType(ResourceType type, NamespacedIdentifier location) {
		return this.updateResourceType(type, location.identifier());
	}

	private ResourceType updateResourceType(ResourceType type, String path) {
		if (this.shouldRedirectSomeDirectories && this.shouldRedirectToAssets(path)) {
			type = ResourceType.CLIENT_ASSETS;
		}

		return type;
	}

	@Override
	public boolean hasResource(ResourceType type, NamespacedIdentifier location) {
		return super.hasResource(this.updateResourceType(type, location), location);
	}

	@Override
	public IOSupplier<InputStream> getResource(ResourceType type, NamespacedIdentifier location) {
		return super.getResource(this.updateResourceType(type, location), location);
	}

	@Override
	public void findResources(ResourceType type, String namespace, String directory, ResourceConsumer consumer) {
		super.findResources(this.updateResourceType(type, directory), namespace, directory, consumer);
	}
}
