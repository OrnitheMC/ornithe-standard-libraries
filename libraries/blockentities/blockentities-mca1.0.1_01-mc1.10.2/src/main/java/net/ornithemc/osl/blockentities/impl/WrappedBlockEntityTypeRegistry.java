package net.ornithemc.osl.blockentities.impl;

import java.util.Set;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.minecraft.block.entity.BlockEntity;

import net.ornithemc.osl.blockentities.api.blockentity.BlockEntityType;
import net.ornithemc.osl.blockentities.impl.blockentity.BlockEntityTypeImpl;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.registry.LegacyStringIds;
import net.ornithemc.osl.registries.api.registry.ResourceKey;
import net.ornithemc.osl.registries.api.registry.ResourceKeys;
import net.ornithemc.osl.registries.impl.registry.SimpleRegistry;

public class WrappedBlockEntityTypeRegistry extends SimpleRegistry<BlockEntityType<?>> {

	private final BiMap<NamespacedIdentifier, String> legacyIds = HashBiMap.create();

	private boolean skipBlockEntityRegister;

	WrappedBlockEntityTypeRegistry(NamespacedIdentifier identifier) {
		super(identifier);
	}

	public void register(String legacyId, Class<? extends BlockEntity> classType) {
		NamespacedIdentifier identifier = this.constructIdentifier(legacyId);
		ResourceKey<BlockEntityType<?>> key = ResourceKeys.from(this.identifier(), identifier);
		BlockEntityType<?> type = BlockEntityTypeImpl.Builder.of(classType).build();

		this.skipBlockEntityRegister = true;
		this.register(key, type);
		this.skipBlockEntityRegister = false;
	}

	@Override
	public <V extends BlockEntityType<?>> V register(int id, ResourceKey<BlockEntityType<?>> key, V type) {
		type = super.register(id, key, type);

		if (!this.skipBlockEntityRegister) {
			NamespacedIdentifier identifier = key.identifier();
			String legacyId = this.constructLegacyId(identifier);
			Class<? extends BlockEntity> classType = type.getType();

			if (BlockEntity.ID_TO_TYPE.containsKey(legacyId)) {
				throw new IllegalStateException("Duplicate legacy BlockEntity type ID " + legacyId);
			}

			BlockEntity.ID_TO_TYPE.put(legacyId, classType);
			BlockEntity.TYPE_TO_ID.put(classType, legacyId);
		}

		return type;
	}

	private String constructLegacyId(NamespacedIdentifier identifier) {
		return this.legacyIds.computeIfAbsent(identifier, key -> {
			if (identifier.namespace().equals(NamespacedIdentifiers.MINECRAFT_NAMESPACE)) {
				String legacyId = VanillaBlockEntityTypes.IDENTIFIERS.inverse().get(identifier.identifier());

				if (legacyId != null) {
					return legacyId;
				}
			}

			return LegacyStringIds.fromIdentifier(identifier);
		});
	}

	private NamespacedIdentifier constructIdentifier(String legacyId) {
		return this.legacyIds.inverse().computeIfAbsent(legacyId, key -> {
			String identifier = VanillaBlockEntityTypes.IDENTIFIERS.get(legacyId);

			if (identifier != null) {
				return NamespacedIdentifiers.from(NamespacedIdentifiers.MINECRAFT_NAMESPACE, identifier);
			}

			return LegacyStringIds.toIdentifier(legacyId);
		});
	}

	public Class<? extends BlockEntity> get(String legacyId) {
		return BlockEntity.ID_TO_TYPE.get(legacyId);
	}

	public String getLegacyId(Class<? extends BlockEntity> type) {
		return BlockEntity.TYPE_TO_ID.get(type);
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
