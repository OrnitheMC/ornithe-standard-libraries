package net.ornithemc.osl.resource.loader.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.unmapped.C_70716992;

@Mixin(C_70716992.class)
public interface VillagerProfessionLayerAccess {

	@Accessor("f_82523592")
	String osl$resource_loader$getIdentifier();

}
