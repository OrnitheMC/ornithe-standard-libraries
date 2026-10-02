package net.ornithemc.osl.resource.loader.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.resource.pack.LegacyResourcePack;
import net.minecraft.client.resource.pack.ResourcePack;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

@Conditional(minecraftVersion = @Version(">=16w32a"))
@Mixin(LegacyResourcePack.class)
public interface LegacyResourcePackAccess {

	@Accessor("pack")
	ResourcePack accessPack();

}
