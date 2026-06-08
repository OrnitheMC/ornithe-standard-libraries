package net.ornithemc.osl.commands.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.state.StateDefinition;
import net.minecraft.block.state.property.Property;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.commands.impl.block.state.StateDefinitionExtensionImpl;

@Conditional(minecraftVersion = @Version("<15w31a"))
@Mixin(StateDefinition.class)
public class StateDefinitionMixin implements StateDefinitionExtensionImpl {

	@Shadow
	private ImmutableList<Property<?>> f_02084018;

	@Override
	public Property<?> getProperty(String name) {
		return this.f_02084018.stream()
			.filter(property -> property.getName().equals(name))
			.findAny().orElse(null);
	}
}
