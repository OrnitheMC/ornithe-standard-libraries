package net.ornithemc.osl.resource.loader.impl.adapter;

import net.minecraft.client.resource.Identifier;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

class Adapters {

	static Identifier identifier(NamespacedIdentifier id) {
		return id instanceof Identifier ? (Identifier) id : new Identifier(id.namespace(), id.identifier());
	}
}
