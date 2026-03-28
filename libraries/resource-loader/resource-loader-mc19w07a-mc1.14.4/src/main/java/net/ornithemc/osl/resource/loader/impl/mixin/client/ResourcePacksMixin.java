package net.ornithemc.osl.resource.loader.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.resource.pack.UnopenedResourcePack;
import net.minecraft.resource.pack.Pack;

import net.ornithemc.osl.resource.loader.impl.access.ResourcePacksAccess;

@Mixin(net.minecraft.client.resource.pack.ResourcePacks.class)
public class ResourcePacksMixin implements ResourcePacksAccess {

	@Shadow
	private UnopenedResourcePack serverPack;

	@Override
	public Pack osl$resource_loader$getServerPack() {
		return this.serverPack == null ? null : this.serverPack.build();
	}
}
