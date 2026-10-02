package net.ornithemc.osl.blockstates.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.state.FluidState;

import net.ornithemc.osl.blockstates.api.fluid.state.FluidStateExtension;

@Mixin(FluidState.class)
public interface FluidStateMixin extends FluidStateExtension {

	@Shadow
	Fluid getFluid();

	@Override
	default boolean is(Fluid fluid) {
		return this.getFluid().is(fluid);
	}
}
