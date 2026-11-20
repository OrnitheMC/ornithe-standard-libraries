package net.ornithemc.osl.resource.loader.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.resource.loader.impl.nbt.NbtCompoundExtensionImpl;

@Conditional(minecraftVersion = @Version("<=1.6.4"))
@Mixin(NbtCompound.class)
public class NbtCompoundMixin implements NbtCompoundExtensionImpl {

	@Shadow
	private NbtList m_95654166(String key) { return null; }

	@Override
	public NbtList getList(String key, int type) {
		return this.m_95654166(key);
	}
}
