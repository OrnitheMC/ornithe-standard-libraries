package net.ornithemc.osl.resource.loader.impl.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.pack.ResourcePack;

@Mixin(Minecraft.class)
public interface MinecraftAccessor {

	@Accessor(
		value = "defaultResourcePacks"
	)
	List<ResourcePack> osl$resource_loader$defaultResourcePacks();

}
