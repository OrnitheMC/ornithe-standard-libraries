package net.ornithemc.osl.blocks.impl.mixin.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.world.color.BlockColor;
import net.minecraft.client.world.color.BlockColors;
import net.minecraft.util.Id2ObjectBiMap;

import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.api.registry.sync.Id2ObjectBiMapMapper;

@Mixin(BlockColors.class)
public class BlockColorsMixin {

	@Shadow @Final
	private Id2ObjectBiMap<BlockColor> registry;

	@Inject(
		method = "<init>",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$items$registerModelRegistryMapper(CallbackInfo ci) {
		SyncedRegistries.registerMapper(RegistryKeys.BLOCK, NamespacedIdentifiers.from("block_color"), Id2ObjectBiMapMapper.of(this.registry));
	}
}
