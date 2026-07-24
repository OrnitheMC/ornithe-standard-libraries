package net.ornithemc.osl.blocks.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.FireBlock;

import net.ornithemc.osl.blocks.api.BlockRegistry;
import net.ornithemc.osl.blocks.impl.block.BlockPostInit;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.api.registry.sync.DynamicArrays;
import net.ornithemc.osl.registries.api.registry.sync.IntArrayMapper;
import net.ornithemc.osl.registries.impl.registry.RegistriesImpl;

@Mixin(FireBlock.class)
public class FireBlockMixin implements BlockPostInit {

	@Shadow
	private int[] flammability;
	@Shadow
	private int[] burnChance;

	@Inject(
		method = "setFlammable",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$blocks$growArrays(int block, int flammability, int burnChance, CallbackInfo ci) {
		int capacity = block + 1;

		this.flammability = DynamicArrays.grow(this.flammability, capacity);
		this.burnChance = DynamicArrays.grow(this.burnChance, capacity);
	}

	@Override
	public void osl$blocks$postInit() {
		FireBlock block = (FireBlock) (Object) this;
		NamespacedIdentifier identifier = BlockRegistry.getIdentifier(block);

		if (identifier == null) {
			RegistriesImpl.LOGGER.warn("Unable to register FireBlock array mappers for unregistered block {} (ID {})", block, block.id);
		} else {
			SyncedRegistries.registerMapper(RegistryKeys.BLOCK, identifier.suffixed("/flammability"), IntArrayMapper.of(this.flammability));
			SyncedRegistries.registerMapper(RegistryKeys.BLOCK, identifier.suffixed("/burn_chance"), IntArrayMapper.of(this.burnChance));
		}
	}
}
