package net.ornithemc.osl.resource.loader.impl.adapter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import net.minecraft.client.resource.metadata.ResourceMetadataSerializerRegistry;
import net.minecraft.resource.Identifier;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.resource.loader.api.resource.Resource;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;

class Adapters {

	static Identifier identifier(NamespacedIdentifier id) {
		return id instanceof Identifier ? (Identifier) id : new Identifier(id.namespace(), id.identifier());
	}

	static net.minecraft.client.resource.Resource resource(ResourceMetadataSerializerRegistry metadataSerializers, Resource resource) throws IOException {
		return new ResourceAdapter(metadataSerializers, resource);
	}

	static net.minecraft.client.resource.Resource resource(ResourceMetadataSerializerRegistry metadataSerializers, NamespacedIdentifier location, Optional<Resource> resource) throws IOException {
		if (resource.isPresent()) {
			return resource(metadataSerializers, resource.get());
		}

		throw new FileNotFoundException(location.toString());
	}

	static List<net.minecraft.client.resource.Resource> resources(ResourceMetadataSerializerRegistry metadataSerializers, List<Resource> resources) throws IOException {
		List<net.minecraft.client.resource.Resource> rs = new ArrayList<>();

		for (Resource resource : resources) {
			rs.add(resource(metadataSerializers, resource));
		}

		return Collections.unmodifiableList(rs);
	}

	static net.minecraft.client.resource.pack.ResourcePack resourcePack(ResourcePack pack) {
		if (pack instanceof WrappedResourcePack) {
			return ((WrappedResourcePack) pack).pack;
		} else {
			return new ResourcePackAdapter(pack);
		}
	}

	static ResourcePack resourcePack(net.minecraft.client.resource.pack.ResourcePack pack) {
		if (pack instanceof ResourcePackAdapter) {
			return ((ResourcePackAdapter) pack).pack;
		} else {
			return new WrappedResourcePack(pack);
		}
	}

	static List<net.minecraft.client.resource.pack.ResourcePack> packs(List<ResourcePack> packs) {
		return packs.stream().map(Adapters::resourcePack).collect(Collectors.toList());
	}

	static List<ResourcePack> resourcePacks(List<net.minecraft.client.resource.pack.ResourcePack> packs) {
		return packs.stream().map(Adapters::resourcePack).collect(Collectors.toList());
	}
}
