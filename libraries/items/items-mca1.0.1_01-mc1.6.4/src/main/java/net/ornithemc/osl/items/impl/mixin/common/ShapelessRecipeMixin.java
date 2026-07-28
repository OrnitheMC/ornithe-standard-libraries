package net.ornithemc.osl.items.impl.mixin.common;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.crafting.recipe.ShapedRecipe;
import net.minecraft.item.ItemStack;

import net.ornithemc.osl.items.impl.item.FixableRecipe;

@Mixin(ShapedRecipe.class)
public class ShapelessRecipeMixin implements FixableRecipe {

	@Shadow
	private List<ItemStack> ingredients;
	@Shadow
	private ItemStack result;

	@Override
	public boolean osl$items$canFixRecipe(ItemMapper mapper) {
		for (ItemStack ingredient : this.ingredients) {
			if (!mapper.canFixItem(ingredient)) {
				return false;
			}
		}

		return mapper.canFixItem(this.result);
	}

	@Override
	public void osl$items$fixRecipe(ItemMapper mapper) {
		for (ItemStack ingredient : this.ingredients) {
			mapper.fixItem(ingredient);
		}

		mapper.fixItem(this.result);
	}
}
