package net.ornithemc.osl.blocks.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import net.ornithemc.osl.blocks.api.block.Blocks;

@Mixin(World.class)
public class WorldMixin {

	@ModifyVariable(
		method = "canPlace",
		ordinal = 0,
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/block/Block;getCollisionShape(Lnet/minecraft/world/World;III)Lnet/minecraft/util/math/Box;"
		)
	)
	private Block osl$blocks$makeAirReplaceable(Block block) {
		return block == Blocks.AIR ? null : block;
	}
}
