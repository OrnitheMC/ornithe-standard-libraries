package net.ornithemc.osl.resource.loader.impl.adapter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import net.minecraft.resource.Identifier;
import net.minecraft.resource.pack.Pack;
import net.minecraft.resource.pack.PackType;
import net.minecraft.resource.pack.repository.UnopenedPack;
import net.minecraft.resource.pack.repository.UnopenedPack.Position;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.resource.loader.api.resource.Resource;
import net.ornithemc.osl.resource.loader.api.resource.ResourceType;
import net.ornithemc.osl.resource.loader.api.resource.pack.PackCompatibility;
import net.ornithemc.osl.resource.loader.api.resource.pack.PackPosition;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackSummary;

class Adapters {

	static Identifier identifier(NamespacedIdentifier id) {
		return id instanceof Identifier ? (Identifier) id : new Identifier(id.namespace(), id.identifier());
	}

	static PackType packType(ResourceType type) {
		switch (type) {
		case CLIENT_ASSETS:
			return PackType.CLIENT_RESOURCES;
		case SERVER_DATA:
			return PackType.SERVER_DATA;
		default:
			throw new IllegalStateException("unknown resource type " + type);
		}
	}

	static ResourceType resourceType(PackType type) {
		switch (type) {
		case CLIENT_RESOURCES:
			return ResourceType.CLIENT_ASSETS;
		case SERVER_DATA:
			return ResourceType.SERVER_DATA;
		default:
			throw new IllegalStateException("unknown pack type " + type);
		}
	}

	static net.minecraft.resource.Resource resource(Resource resource) throws IOException {
		return new ResourceAdapter(resource);
	}

	static net.minecraft.resource.Resource resource(NamespacedIdentifier location, Optional<Resource> resource) throws IOException {
		if (resource.isPresent()) {
			return resource(resource.get());
		}

		throw new FileNotFoundException(location.toString());
	}

	static List<net.minecraft.resource.Resource> resources(List<Resource> resources) throws IOException {
		List<net.minecraft.resource.Resource> rs = new ArrayList<>();

		for (Resource resource : resources) {
			rs.add(resource(resource));
		}

		return Collections.unmodifiableList(rs);
	}

	static Pack pack(ResourcePack pack) {
		if (pack instanceof WrappedPack) {
			return ((WrappedPack) pack).pack;
		} else {
			return new ResourcePackAdapter(pack);
		}
	}

	static ResourcePack resourcePack(Pack pack) {
		if (pack instanceof ResourcePackAdapter) {
			return ((ResourcePackAdapter) pack).pack;
		} else {
			return new WrappedPack(pack);
		}
	}

	static List<Pack> packs(List<ResourcePack> packs) {
		return packs.stream().map(Adapters::pack).collect(Collectors.toList());
	}

	static List<ResourcePack> resourcePacks(List<Pack> packs) {
		return packs.stream().map(Adapters::resourcePack).collect(Collectors.toList());
	}

	static Position position(PackPosition pos) {
		switch (pos) {
		case BOTTOM:
			return Position.BOTTOM;
		case TOP:
			return Position.TOP;
		default:
			throw new IllegalStateException("unknown pack position " + pos);
		}
	}

	static PackPosition packPosition(Position pos) {
		switch (pos) {
		case BOTTOM:
			return PackPosition.BOTTOM;
		case TOP:
			return PackPosition.TOP;
		default:
			throw new IllegalStateException("unknown unopened pack position " + pos);
		}
	}

	static net.minecraft.resource.pack.PackCompatibility packCompatibility(PackCompatibility pos) {
		switch (pos) {
		case COMPATIBLE:
			return net.minecraft.resource.pack.PackCompatibility.COMPATIBLE;
		case TOO_OLD:
			return net.minecraft.resource.pack.PackCompatibility.TOO_OLD;
		case TOO_NEW:
			return net.minecraft.resource.pack.PackCompatibility.TOO_NEW;
		default:
			throw new IllegalStateException("unknown pack compatibility " + pos);
		}
	}

	static PackCompatibility packCompatibility(net.minecraft.resource.pack.PackCompatibility pos) {
		switch (pos) {
		case COMPATIBLE:
			return PackCompatibility.COMPATIBLE;
		case TOO_OLD:
			return PackCompatibility.TOO_OLD;
		case TOO_NEW:
			return PackCompatibility.TOO_NEW;
		default:
			throw new IllegalStateException("unknown pack compatibility " + pos);
		}
	}

	static ResourcePackSummary resourcePackSummary(UnopenedPack unopenedPack) {
		return new WrappedUnopenedPack<>(unopenedPack);
	}

	static <T extends UnopenedPack> T unopenedPack(ResourcePackSummary summary, UnopenedPack.Factory<T> factory) {
		if (summary instanceof WrappedUnopenedPack) {
			return (T) ((WrappedUnopenedPack) summary).pack;
		} else {
			return UnopenedPack.create(
				summary.getId(),
				summary.isRequired(),
				() -> pack(summary.open()),
				factory,
				position(summary.getDefaultPosition())
			);
		}
	}
}
