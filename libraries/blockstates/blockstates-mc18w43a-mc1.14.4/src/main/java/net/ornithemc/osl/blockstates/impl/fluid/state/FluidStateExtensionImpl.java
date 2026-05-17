package net.ornithemc.osl.blockstates.impl.fluid.state;

import net.minecraft.fluid.Fluid;

import net.ornithemc.osl.blockstates.api.fluid.state.FluidStateExtension;

public interface FluidStateExtensionImpl extends FluidStateExtension {

	@Override
	default boolean is(Fluid fluid) {
		throw new AbstractMethodError();
	}
}
