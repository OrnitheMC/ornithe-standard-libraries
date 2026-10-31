package net.ornithemc.osl.blockstates.impl.block.state;

import net.minecraft.block.state.property.Property;

import net.ornithemc.osl.blockstates.api.block.state.StateDefinitionExtension;

public interface StateDefinitionExtensionImpl extends StateDefinitionExtension {

	@Override
	default Property<?> getProperty(String name) {
		throw new AbstractMethodError();
	}
}
