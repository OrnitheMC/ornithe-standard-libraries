package net.ornithemc.osl.resource.loader.impl;

import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import net.fabricmc.loader.api.FabricLoader;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.function.IOSupplier;
import net.ornithemc.osl.resource.loader.api.resource.ResourceType;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourceConsumer;
import net.ornithemc.osl.resource.loader.impl.resource.pack.ModResourcePack;

public class DefaultTexturePack extends ModResourcePack {

	public DefaultTexturePack() {
		super(FabricLoader.getInstance().getModContainer("minecraft").get(), ".", "Default", "Default");
	}

	@Override
	protected Map<ResourceType, Set<String>> findNamespaces() {
		return Collections.emptyMap();
	}

	@Override
	public boolean hasResource(ResourceType type, NamespacedIdentifier location) {
		return false;
	}

	@Override
	public IOSupplier<InputStream> getResource(ResourceType type, NamespacedIdentifier location) {
		return null;
	}

	@Override
	public void findResources(ResourceType type, String namespace, String directory, ResourceConsumer consumer) {
	}
}
