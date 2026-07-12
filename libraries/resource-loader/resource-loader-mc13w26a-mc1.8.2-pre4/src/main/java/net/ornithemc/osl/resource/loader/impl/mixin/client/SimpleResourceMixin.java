package net.ornithemc.osl.resource.loader.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.resource.SimpleResource;
import net.minecraft.resource.Identifier;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.resource.loader.impl.resource.ResourceLocationAccess;

@Mixin(SimpleResource.class)
public class SimpleResourceMixin implements ResourceLocationAccess {

	@Shadow
	private Identifier location;

	@Override
	public NamespacedIdentifier osl$resource_loader$resourceLocation() {
		return location;
	}
}
