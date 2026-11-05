package net.ornithemc.osl.registries.impl.mixin.common;

import java.io.File;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.world.World;
import net.minecraft.world.storage.WorldStorage;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

@Mixin(World.class)
public interface WorldAccess {

	@Conditional(minecraftVersion = @Version("<b1.3"))
	@Accessor("f_14667040")
	File accessSaveDirectory();

	@Conditional(minecraftVersion = @Version(">=b1.3"))
	@Environment(EnvType.CLIENT)
	@Accessor("storage")
	WorldStorage accessStorage();

}
