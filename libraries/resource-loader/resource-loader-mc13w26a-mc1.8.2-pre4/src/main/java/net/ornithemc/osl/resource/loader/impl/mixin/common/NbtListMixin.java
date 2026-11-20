package net.ornithemc.osl.resource.loader.impl.mixin.common;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.resource.loader.impl.nbt.NbtListExtensionImpl;

@Conditional(minecraftVersion = @Version("<=1.6.4"))
@Mixin(NbtList.class)
public class NbtListMixin implements NbtListExtensionImpl {

	@Shadow
	private List<? extends NbtElement> elements;

	@Override
	public String getString(int index) {
		return this.elements.get(index).toString();
	}
}
