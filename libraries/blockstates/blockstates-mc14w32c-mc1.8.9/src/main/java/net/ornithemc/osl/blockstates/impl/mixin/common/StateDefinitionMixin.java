package net.ornithemc.osl.blockstates.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.state.StateDefinition;
import net.minecraft.block.state.property.Property;

import net.ornithemc.osl.blockstates.api.block.state.StateDefinitionExtension;

@Mixin(StateDefinition.class)
public class StateDefinitionMixin implements StateDefinitionExtension {

	@Shadow
	private ImmutableList<Property<?>> properties;

	@Override
	public Property<?> getProperty(String name) {
		return this.properties.stream()
			.filter(property -> property.getName().equals(name))
			.findAny().orElse(null);
	}
}
