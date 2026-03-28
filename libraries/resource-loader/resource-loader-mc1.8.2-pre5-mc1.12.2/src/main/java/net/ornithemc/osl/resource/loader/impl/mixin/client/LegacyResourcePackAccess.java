package net.ornithemc.osl.resource.loader.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.resource.pack.LegacyResourcePack;
import net.minecraft.client.resource.pack.ResourcePack;

@Pseudo // class only exists in 15w31a+
@Mixin(LegacyResourcePack.class)
public interface LegacyResourcePackAccess {

	@Accessor("pack")
	ResourcePack accessPack();

}
