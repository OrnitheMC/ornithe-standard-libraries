package net.ornithemc.osl.resource.loader.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.resource.pack.BuiltInPack;
import net.minecraft.resource.pack.Pack;
import net.minecraft.server.resource.pack.DataPacks;

import net.ornithemc.osl.resource.loader.impl.access.DataPacksAccess;

@Mixin(DataPacks.class)
public class DataPacksMixin implements DataPacksAccess {

	@Shadow
	private BuiltInPack defaultPack;

	@Override
	public Pack osl$resource_loader$getDefaultPack() {
		return this.defaultPack;
	}
}
