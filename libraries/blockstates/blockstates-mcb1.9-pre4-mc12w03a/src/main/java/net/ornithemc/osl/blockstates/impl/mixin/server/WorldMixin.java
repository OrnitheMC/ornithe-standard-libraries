package net.ornithemc.osl.blockstates.impl.mixin.server;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.block.Block;
import net.minecraft.world.World;

@Mixin(World.class)
public class WorldMixin {

	@Definition(
		id = "block",
		local = @Local(
			type = Block.class
		)
	)
	@Expression("block != null")
	@WrapOperation(
		method = "containsNonAir",
		at = @At(
			value = "MIXINEXTRAS:EXPRESSION"
		)
	)
	private boolean osl$blockstates$fixNotAirCheck(Object block, Object _null, Operation<Boolean> op) {
		return op.call(block, _null) && !((Block) block).isAir();
	}
}
