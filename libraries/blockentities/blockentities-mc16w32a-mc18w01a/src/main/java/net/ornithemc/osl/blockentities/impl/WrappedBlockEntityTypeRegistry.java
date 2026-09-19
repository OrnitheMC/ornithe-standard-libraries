package net.ornithemc.osl.blockentities.impl;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.resource.Identifier;

import net.ornithemc.osl.blockentities.api.blockentity.BlockEntityType;
import net.ornithemc.osl.blockentities.impl.blockentity.BlockEntityTypeImpl;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.registries.api.registry.ResourceKey;
import net.ornithemc.osl.registries.api.registry.ResourceKeys;
import net.ornithemc.osl.registries.impl.access.Clearable;
import net.ornithemc.osl.registries.impl.registry.SimpleRegistry;

public class WrappedBlockEntityTypeRegistry extends SimpleRegistry<BlockEntityType<?>> {

	private boolean skipBlockEntityRegister;

	WrappedBlockEntityTypeRegistry(NamespacedIdentifier identifier) {
		super(identifier);
	}

	public void register(Identifier identifier, Class<? extends BlockEntity> classType) {
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
			Identifier identifier = new Identifier(key.identifier().namespace(), key.identifier().identifier());
			Class<? extends BlockEntity> classType = type.getType();

			BlockEntity.REGISTRY.register(id, identifier, classType);
		}

		return type;
	}

	@Override
	public void clear() {
		super.clear();

		Clearable.clear(BlockEntity.REGISTRY);
	}
}
