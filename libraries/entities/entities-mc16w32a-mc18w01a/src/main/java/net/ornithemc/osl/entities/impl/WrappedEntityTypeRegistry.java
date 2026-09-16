package net.ornithemc.osl.entities.impl;

import java.util.Set;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.minecraft.entity.Entities;
import net.minecraft.entity.Entity;
import net.minecraft.resource.Identifier;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.entities.api.entity.EntityType;
import net.ornithemc.osl.entities.impl.entity.EntityTypeImpl;
import net.ornithemc.osl.registries.api.registry.LegacyStringIds;
import net.ornithemc.osl.registries.api.registry.ResourceKey;
import net.ornithemc.osl.registries.api.registry.ResourceKeys;
import net.ornithemc.osl.registries.impl.access.Clearable;
import net.ornithemc.osl.registries.impl.registry.SimpleRegistry;

public class WrappedEntityTypeRegistry extends SimpleRegistry<EntityType<?>> {

	private final BiMap<Identifier, String> legacyIds = HashBiMap.create();

	private boolean skipEntitiesRegister;

	WrappedEntityTypeRegistry(NamespacedIdentifier identifier) {
		super(identifier);
	}

	public void register(int id, String legacyId, Identifier identifier, Class<? extends Entity> classType) {
		this.legacyIds.put(identifier, legacyId);

		ResourceKey<EntityType<?>> key = ResourceKeys.from(this.identifier(), identifier);
		EntityType<?> type = EntityTypeImpl.Builder.of(classType).build();

		this.skipEntitiesRegister = true;
		this.register(id, key, type);
		this.skipEntitiesRegister = false;
	}

	@Override
	public <V extends EntityType<?>> V register(int id, ResourceKey<EntityType<?>> key, V type) {
		type = super.register(id, key, type);

		if (!this.skipEntitiesRegister) {
			boolean namesAreKnown = this.legacyIds.containsKey(key.identifier());

			Identifier identifier = new Identifier(key.identifier().namespace(), key.identifier().identifier());
			String legacyId = this.constructLegacyId(identifier);
			Class<? extends Entity> classType = type.getType();

			Entities.REGISTRY.register(id, identifier, classType);

			if (!namesAreKnown) {
				while (Entities.NAMES.size() <= id) {
					Entities.NAMES.add(null);
				}

				String registeredName = Entities.NAMES.get(id);

				if (registeredName != null) {
					throw new IllegalStateException("Name for entity type " + identifier + " was already set (" + registeredName + ")!");
				} else {
					Entities.NAMES.set(id, legacyId);
				}
			}
		}

		return type;
	}

	private String constructLegacyId(Identifier identifier) {
		return this.legacyIds.computeIfAbsent(identifier, LegacyStringIds::fromIdentifier);
	}

	public Class<? extends Entity> get(String legacyId) {
		Identifier identifier = this.legacyIds.inverse().get(legacyId);
		return identifier == null ? null : Entities.REGISTRY.get(identifier);
	}

	public String getLegacyId(Class<? extends Entity> type) {
		Identifier identifier = Entities.REGISTRY.getKey(type);
		return identifier == null ? null : this.legacyIds.get(identifier);
	}

	public Set<String> legacyIdSet() {
		return this.legacyIds.values();
	}

	@Override
	public void clear() {
		super.clear();

		Clearable.clear(Entities.REGISTRY);
	}
}
