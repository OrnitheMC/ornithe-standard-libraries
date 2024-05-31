package net.ornithemc.osl.resource.loader.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.resource.SimpleResource;
import net.minecraft.resource.Identifier;

@Mixin(SimpleResource.class)
public interface SimpleResourceAccessor {

	// the SimpleResource#getLocation method only exists in 14w25a and above
	@Accessor("location")
	Identifier osl$resource_loader$getLocation();

}
