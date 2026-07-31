package net.ornithemc.osl.blockentities.impl.mixin.common;

import java.util.LinkedHashSet;
import java.util.Set;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;

import net.ornithemc.osl.blockentities.impl.BlockEntityTypeRegistryImpl;
import net.ornithemc.osl.blockentities.impl.access.BlockEntityTypeAccess;

@Mixin(BlockEntityType.class)
public class BlockEntityTypeMixin implements BlockEntityTypeAccess {

	@Shadow @Final
	private Set<Block> f_11977594;

	@Inject(
		method = "<clinit>",
		at = @At(
			value = "HEAD"
		)
	)
	private static void osl$blockentities$unlockBlockEntityTypeRegistry(CallbackInfo ci) {
		BlockEntityTypeRegistryImpl.unlock();
	}

	@Inject(
		method = "<clinit>",
		at = @At(
			value = "TAIL"
		)
	)
	private static void osl$blockentities$registerBlockEntityTypes(CallbackInfo ci) {
		BlockEntityTypeRegistryImpl.registerBlockEntityTypes();
	}

	@ModifyVariable(
		method = "<init>",
		argsOnly = true,
		at = @At(
			value = "INVOKE",
			target = "Ljava/lang/Object;<init>()V",
			shift = Shift.AFTER
		)
	)
	private Set<Block> osl$blockentities$makeBlocksSetModifiable(Set<Block> blocks) {
		// Vanilla's Builder uses Google Commons' ImmutableSet.of(...) which preserves order!
		return new LinkedHashSet<>(blocks);
	}

	@Override
	public void osl$blockentities$addBlocks(Block... blocks) {
		for (Block block : blocks) {
			this.f_11977594.add(block);
		}
	}
}
