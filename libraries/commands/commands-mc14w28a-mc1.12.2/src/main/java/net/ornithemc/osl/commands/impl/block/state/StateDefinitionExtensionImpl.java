package net.ornithemc.osl.commands.impl.block.state;

import net.minecraft.block.state.property.Property;

public interface StateDefinitionExtensionImpl {

	default Property<?> getProperty(String name) {
		throw new AbstractMethodError();
	}
}
