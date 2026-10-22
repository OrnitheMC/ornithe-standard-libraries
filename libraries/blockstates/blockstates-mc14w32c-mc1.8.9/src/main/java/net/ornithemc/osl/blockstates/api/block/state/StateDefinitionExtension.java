package net.ornithemc.osl.blockstates.api.block.state;

import net.minecraft.block.state.property.Property;

public interface StateDefinitionExtension {

	Property<?> getProperty(String name);

}
