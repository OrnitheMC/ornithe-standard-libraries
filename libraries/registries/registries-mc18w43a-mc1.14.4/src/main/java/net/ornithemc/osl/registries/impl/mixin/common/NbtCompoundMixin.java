package net.ornithemc.osl.registries.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.registries.impl.RegistriesEntrypoint;
import net.ornithemc.osl.registries.impl.access.NbtCompoundAccess;

@Mixin(NbtCompound.class)
public class NbtCompoundMixin implements NbtCompoundAccess {

	@Conditional(minecraftVersion = @Version("<19w04a"))
	@Shadow
	private void put(String key, NbtElement elementn) { }

	@Conditional(minecraftVersion = @Version(">=19w04a"))
	@Shadow
	private NbtElement m_21770494(String key, NbtElement elementn) { return null; }

	@Override
	public void osl$registries$put(String key, NbtElement element) {
		if (RegistriesEntrypoint.OLD_WORLD_STORAGE) {
			this.put(key, element);
		} else {
			this.m_21770494(key, element);
		}
	}
}
