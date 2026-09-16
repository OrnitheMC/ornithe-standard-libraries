package net.ornithemc.osl.entities.impl;

import java.util.Set;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.minecraft.entity.Entities;
import net.minecraft.entity.Entity;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.core.impl.util.MinecraftVersion;
import net.ornithemc.osl.entities.api.entity.EntityType;
import net.ornithemc.osl.entities.impl.entity.EntityTypeImpl;
import net.ornithemc.osl.registries.api.registry.LegacyStringIds;
import net.ornithemc.osl.registries.api.registry.ResourceKey;
import net.ornithemc.osl.registries.api.registry.ResourceKeys;
import net.ornithemc.osl.registries.impl.registry.SimpleRegistry;

public class WrappedEntityTypeRegistry extends SimpleRegistry<EntityType<?>> {

	private static final boolean VANILLA_TRACKS_KEY_TO_ID = MinecraftVersion.resolve().compareTo("12w06a") >= 0;

	private final BiMap<NamespacedIdentifier, String> legacyIds = HashBiMap.create();

	private boolean skipEntitiesRegister;

	WrappedEntityTypeRegistry(NamespacedIdentifier identifier) {
		super(identifier);
	}

	public void register(int id, String legacyId, Class<? extends Entity> classType) {
		NamespacedIdentifier identifier = this.constructIdentifier(legacyId);
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
			NamespacedIdentifier identifier = key.identifier();
			String legacyId = this.constructLegacyId(identifier);
			Class<? extends Entity> classType = type.getType();

			if (Entities.KEY_TO_TYPE.containsKey(legacyId)) {
				throw new IllegalStateException("Duplicate legacy Entity type ID " + legacyId);
			}

			Entities.KEY_TO_TYPE.put(legacyId, classType);
			Entities.TYPE_TO_KEY.put(classType, legacyId);
			Entities.ID_TO_TYPE.put(id, classType);
			Entities.TYPE_TO_ID.put(classType, id);
			if (VANILLA_TRACKS_KEY_TO_ID) {
				Entities.KEY_TO_ID.put(legacyId, id);
			}
		}

		return type;
	}

	private String constructLegacyId(NamespacedIdentifier identifier) {
		return this.legacyIds.computeIfAbsent(identifier, key -> {
			if (identifier.namespace().equals(NamespacedIdentifiers.MINECRAFT_NAMESPACE)) {
				String legacyId = VanillaEntityTypes.IDENTIFIERS.inverse().get(identifier.identifier());

				if (legacyId != null) {
					return legacyId;
				}
			}

			return LegacyStringIds.fromIdentifier(identifier);
		});
	}

	private NamespacedIdentifier constructIdentifier(String legacyId) {
		return this.legacyIds.inverse().computeIfAbsent(legacyId, key -> {
			String identifier = VanillaEntityTypes.IDENTIFIERS.get(legacyId);

			if (identifier != null) {
				return NamespacedIdentifiers.from(NamespacedIdentifiers.MINECRAFT_NAMESPACE, identifier);
			}

			return LegacyStringIds.toIdentifier(legacyId);
		});
	}

	public Class<? extends Entity> get(String legacyId) {
		return Entities.KEY_TO_TYPE.get(legacyId);
	}

	public String getLegacyId(Class<? extends Entity> type) {
		return Entities.TYPE_TO_KEY.get(type);
	}

	public Set<String> legacyIdSet() {
		return this.legacyIds.values();
	}

	@Override
	public void clear() {
		super.clear();

		Entities.KEY_TO_TYPE.clear();
		Entities.TYPE_TO_KEY.clear();
		Entities.ID_TO_TYPE.clear();
		Entities.TYPE_TO_ID.clear();
		if (VANILLA_TRACKS_KEY_TO_ID) {
			Entities.KEY_TO_ID.clear();
		}
	}
}
