package net.ornithemc.osl.entities.impl;

import java.util.Set;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.minecraft.entity.Entities;
import net.minecraft.entity.Entity;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.registry.ResourceKey;
import net.ornithemc.osl.registries.api.registry.ResourceKeys;
import net.ornithemc.osl.registries.impl.registry.SimpleRegistry;

public class WrappedEntityTypeRegistry extends SimpleRegistry<Class<? extends Entity>> {

	private static final boolean VANILLA_TRACKS_IDS = EntitiesMixinPlugin.VANILLA_ENTITY_TYPES_HAVE_IDS;

	private final BiMap<NamespacedIdentifier, String> legacyKeys = HashBiMap.create();

	private boolean skipEntitiesRegister;

	WrappedEntityTypeRegistry(NamespacedIdentifier identifier) {
		super(identifier);
	}

	public void register(String legacyKey, Class<? extends Entity> type) {
		this.register(-1, legacyKey, type);
	}

	public void register(int id, String legacyKey, Class<? extends Entity> type) {
		NamespacedIdentifier identifier = this.constructIdentifier(legacyKey);
		ResourceKey<Class<? extends Entity>> key = ResourceKeys.from(this.identifier(), identifier);

		this.skipEntitiesRegister = true;

		if (id < 0) {
			this.register(key, type);
		} else {
			this.register(id, key, type);
		}

		this.skipEntitiesRegister = false;
	}

	@Override
	public <V extends Class<? extends Entity>> V register(int id, ResourceKey<Class<? extends Entity>> key, V type) {
		type = super.register(id, key, type);

		if (!this.skipEntitiesRegister) {
			NamespacedIdentifier identifier = key.identifier();
			String legacyKey = this.constructLegacyKey(identifier);

			if (Entities.KEY_TO_TYPE.containsKey(legacyKey)) {
				throw new IllegalStateException("Duplicate legacy Entity type key " + legacyKey);
			}

			Entities.KEY_TO_TYPE.put(legacyKey, type);
			Entities.TYPE_TO_KEY.put(type, legacyKey);
			if (VANILLA_TRACKS_IDS) {
				Entities.ID_TO_TYPE.put(id, type);
				Entities.TYPE_TO_ID.put(type, id);
			}
		}

		return type;
	}

	private String constructLegacyKey(NamespacedIdentifier identifier) {
		return this.legacyKeys.computeIfAbsent(identifier, key -> {
			if (identifier.namespace().equals(NamespacedIdentifiers.DEFAULT_NAMESPACE)) {
				String legacyKey = VanillaEntityTypes.IDENTIFIERS.inverse().get(identifier.identifier());

				if (legacyKey != null) {
					return legacyKey;
				}
			}

			return identifier.toString();
		});
	}

	private NamespacedIdentifier constructIdentifier(String legacyKey) {
		return this.legacyKeys.inverse().computeIfAbsent(legacyKey, key -> {
			String identifier = VanillaEntityTypes.IDENTIFIERS.get(legacyKey);

			if (identifier != null) {
				return NamespacedIdentifiers.from(identifier);
			}

			StringBuilder sb = new StringBuilder();

			// convert PascalCase to snake_case
			for (int i = 0; i < legacyKey.length(); i++) {
				char chr = legacyKey.charAt(i);

				if (Character.isUpperCase(chr)) {
					chr = Character.toLowerCase(chr);

					if (i != 0) {
						sb.append('_');
					}
				}

				sb.append(chr);
			}

			return NamespacedIdentifiers.from(sb.toString());
		});
	}

	public Class<? extends Entity> get(String legacyKey) {
		NamespacedIdentifier identifier = this.legacyKeys.inverse().get(legacyKey);
		return identifier == null ? null : this.get(identifier);
	}

	public String getLegacyKey(Class<? extends Entity> type) {
		NamespacedIdentifier identifier = this.getIdentifier(type);
		return identifier == null ? null : this.legacyKeys.get(identifier);
	}

	public Set<String> legacyKeySet() {
		return this.legacyKeys.values();
	}

	@Override
	public void clear() {
		super.clear();

		Entities.KEY_TO_TYPE.clear();
		Entities.TYPE_TO_KEY.clear();
		if (VANILLA_TRACKS_IDS) {
			Entities.ID_TO_TYPE.clear();
			Entities.TYPE_TO_ID.clear();
		}
	}
}
