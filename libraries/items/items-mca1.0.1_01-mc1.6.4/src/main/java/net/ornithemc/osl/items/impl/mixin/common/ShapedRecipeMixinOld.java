package net.ornithemc.osl.items.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.crafting.recipe.ShapedRecipe;
import net.minecraft.item.ItemStack;

import net.ornithemc.osl.items.impl.item.FixableRecipe;

@Mixin(ShapedRecipe.class)
public class ShapedRecipeMixinOld implements FixableRecipe {

	@Shadow
	private int[] f_56782558;
	@Shadow
	private ItemStack result;
	@Shadow @Mutable
	private int resultItem;

	@Override
	public boolean osl$items$canFixRecipe(ItemMapper mapper) {
		for (int ingredient : this.f_56782558) {
			if (ingredient != -1 && !mapper.canFixItem(ingredient)) {
				return false;
			}
		}

		return mapper.canFixItem(this.result);
	}

	@Override
	public void osl$items$fixRecipe(ItemMapper mapper) {
		for (int i = 0; i < this.f_56782558.length; i++) {
			int ingredient = this.f_56782558[i];

			if (ingredient != -1) {
				this.f_56782558[i] = mapper.mapItem(ingredient);
			}
		}

		mapper.fixItem(this.result);

		if (this.resultItem >= 0) {
			this.resultItem = mapper.mapItem(this.resultItem);
		}
	}
}
