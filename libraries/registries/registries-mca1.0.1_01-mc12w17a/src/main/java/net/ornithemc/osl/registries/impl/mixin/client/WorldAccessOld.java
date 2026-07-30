package net.ornithemc.osl.registries.impl.mixin.client;

import java.io.File;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.World;

@Mixin(World.class)
public interface WorldAccessOld {

	@Accessor("f_14667040")
	File accessSaveDirectory();

}
