package net.ornithemc.osl.commands.impl.mixin.common;

import java.util.Collection;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.google.common.base.Optional;

import net.minecraft.block.state.property.Property;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.commands.impl.block.state.PropertyExtensionImpl;

@Conditional(minecraftVersion = @Version("<15w31a"))
@Mixin(Property.class)
public interface PropertyMixin extends PropertyExtensionImpl {

	@Shadow
	Collection<Comparable<?>> values();

	@Shadow
	String getName(Comparable<?> value);

	@Override
	default Optional<Comparable<?>> getValue(String name) {
		return Optional.fromNullable(this.values().stream()
			.filter(value -> this.getName(value).equals(name))
			.findAny().orElse(null));
	}
}
