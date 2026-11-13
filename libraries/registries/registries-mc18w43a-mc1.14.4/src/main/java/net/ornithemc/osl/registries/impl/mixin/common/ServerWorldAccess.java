package net.ornithemc.osl.registries.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.storage.AlphaWorldStorage;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

@Conditional(minecraftVersion = @Version(">=19w04a"))
@Mixin(ServerWorld.class)
public interface ServerWorldAccess {

	@Accessor("f_63102719")
	AlphaWorldStorage accessStorage();

}
