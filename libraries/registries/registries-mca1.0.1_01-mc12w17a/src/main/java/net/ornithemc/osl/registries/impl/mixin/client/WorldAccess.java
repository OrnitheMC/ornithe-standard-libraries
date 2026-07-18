package net.ornithemc.osl.registries.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.World;
import net.minecraft.world.storage.WorldStorage;

@Mixin(World.class)
public interface WorldAccess {

	@Accessor("storage")
	WorldStorage accessStorage();

}
