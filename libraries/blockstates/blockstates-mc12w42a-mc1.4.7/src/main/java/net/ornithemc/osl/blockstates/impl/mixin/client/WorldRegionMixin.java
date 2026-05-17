package net.ornithemc.osl.blockstates.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.block.Block;
import net.minecraft.world.WorldRegion;

@Mixin(WorldRegion.class)
public class WorldRegionMixin {

	@Definition(
		id = "block",
		local = @Local(
			type = Block.class
		)
	)
	@Expression("block != null")
	@WrapOperation(
		method = "isAir",
		at = @At(
			value = "MIXINEXTRAS:EXPRESSION"
		)
	)
	private boolean osl$blockstates$fixNotAirCheck(Object block, Object _null, Operation<Boolean> op) {
		return op.call(block, _null) && !((Block) block).isAir();
	}
}
