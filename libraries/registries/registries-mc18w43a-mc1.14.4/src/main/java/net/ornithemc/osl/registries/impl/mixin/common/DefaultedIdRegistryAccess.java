package net.ornithemc.osl.registries.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.resource.Identifier;
import net.minecraft.util.registry.DefaultedIdRegistry;

@Mixin(DefaultedIdRegistry.class)
public interface DefaultedIdRegistryAccess {

	@Accessor("defaultId")
	Identifier accessDefaultId();

}
