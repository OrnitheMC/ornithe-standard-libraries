package net.ornithemc.osl.items.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.crafting.recipe.ShapedRecipe;
import net.minecraft.item.ItemStack;

import net.ornithemc.osl.items.impl.item.FixableRecipe;

@Mixin(ShapedRecipe.class)
public class ShapedRecipeMixinNew implements FixableRecipe {

	@Shadow
	private ItemStack[] ingredients;
	@Shadow
	private ItemStack result;
	@Shadow @Mutable
	private int resultItem;

	@Override
	public boolean osl$items$canFixRecipe(ItemMapper mapper) {
		for (ItemStack ingredient : this.ingredients) {
			if (ingredient != null && !mapper.canFixItem(ingredient)) {
				return false;
			}
		}

		return mapper.canFixItem(this.result);
	}

	@Override
	public void osl$items$fixRecipe(ItemMapper mapper) {
		for (ItemStack ingredient : this.ingredients) {
			if (ingredient != null) {
				mapper.fixItem(ingredient);
			}
		}

		mapper.fixItem(this.result);

		if (this.resultItem >= 0) {
			this.resultItem = mapper.mapItem(this.resultItem);
		}
	}
}
