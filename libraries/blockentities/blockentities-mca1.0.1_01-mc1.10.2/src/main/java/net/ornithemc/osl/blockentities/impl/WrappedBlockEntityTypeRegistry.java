package net.ornithemc.osl.blockentities.impl;

import java.util.Set;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.minecraft.block.entity.BlockEntity;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.registry.ResourceKey;
import net.ornithemc.osl.registries.api.registry.ResourceKeys;
import net.ornithemc.osl.registries.impl.registry.SimpleRegistry;

public class WrappedBlockEntityTypeRegistry extends SimpleRegistry<Class<? extends BlockEntity>> {

	private final BiMap<NamespacedIdentifier, String> legacyIds = HashBiMap.create();

	private boolean skipBlockEntityRegister;

	WrappedBlockEntityTypeRegistry(NamespacedIdentifier identifier) {
		super(identifier);
	}

	public void register(String legacyId, Class<? extends BlockEntity> type) {
		NamespacedIdentifier identifier = this.constructIdentifier(legacyId);
		ResourceKey<Class<? extends BlockEntity>> key = ResourceKeys.from(this.identifier(), identifier);

		this.skipBlockEntityRegister = true;
		this.register(key, type);
		this.skipBlockEntityRegister = false;
	}

	@Override
	public <V extends Class<? extends BlockEntity>> V register(int id, ResourceKey<Class<? extends BlockEntity>> key, V type) {
		type = super.register(id, key, type);

		if (!this.skipBlockEntityRegister) {
			NamespacedIdentifier identifier = key.identifier();
			String legacyId = this.constructLegacyId(identifier);

			if (BlockEntity.ID_TO_TYPE.containsKey(legacyId)) {
				throw new IllegalStateException("Duplicate legacy BlockEntity type ID " + legacyId);
			}

			BlockEntity.ID_TO_TYPE.put(legacyId, type);
			BlockEntity.TYPE_TO_ID.put(type, legacyId);
		}

		return type;
	}

	private String constructLegacyId(NamespacedIdentifier identifier) {
		return this.legacyIds.computeIfAbsent(identifier, key -> {
			if (identifier.namespace().equals(NamespacedIdentifiers.DEFAULT_NAMESPACE)) {
				String legacyId = VanillaBlockEntityTypes.IDENTIFIERS.inverse().get(identifier.identifier());

				if (legacyId != null) {
					return legacyId;
				}
			}

			return identifier.toString();
		});
	}

	private NamespacedIdentifier constructIdentifier(String legacyId) {
		return this.legacyIds.inverse().computeIfAbsent(legacyId, key -> {
			String identifier = VanillaBlockEntityTypes.IDENTIFIERS.get(legacyId);

			if (identifier != null) {
				return NamespacedIdentifiers.from(identifier);
			}

			StringBuilder sb = new StringBuilder();

			// convert PascalCase to snake_case
			for (int i = 0; i < legacyId.length(); i++) {
				char chr = legacyId.charAt(i);

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

	public Class<? extends BlockEntity> get(String legacyId) {
		NamespacedIdentifier identifier = this.legacyIds.inverse().get(legacyId);
		return identifier == null ? null : this.get(identifier);
	}

	public String getLegacyId(Class<? extends BlockEntity> type) {
		NamespacedIdentifier identifier = this.getIdentifier(type);
		return identifier == null ? null : this.legacyIds.get(identifier);
	}

	public Set<String> legacyIdSet() {
		return this.legacyIds.values();
	}

	@Override
	public void clear() {
		super.clear();

		BlockEntity.ID_TO_TYPE.clear();
		BlockEntity.TYPE_TO_ID.clear();
	}
}
