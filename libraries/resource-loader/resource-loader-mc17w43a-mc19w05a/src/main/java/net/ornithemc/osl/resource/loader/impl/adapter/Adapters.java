package net.ornithemc.osl.resource.loader.impl.adapter;

import net.minecraft.resource.Identifier;
import net.minecraft.resource.pack.PackType;
import net.minecraft.resource.pack.repository.UnopenedPack.Position;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.resource.loader.api.resource.ResourceType;
import net.ornithemc.osl.resource.loader.api.resource.pack.PackCompatibility;
import net.ornithemc.osl.resource.loader.api.resource.pack.PackPosition;

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
}
