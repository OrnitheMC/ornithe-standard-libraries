package net.ornithemc.osl.items.impl.item;

import net.minecraft.item.ItemStack;

public interface FixableRecipe {

	boolean osl$items$canFixRecipe(ItemMapper mapper);

	void osl$items$fixRecipe(ItemMapper mapper);

	interface ItemMapper {

		int mapItem(int item);

		default boolean canFixItem(int item) {
			return this.mapItem(item) >= 0;
		}

		default boolean canFixItem(ItemStack item) {
			return this.mapItem(item.id) >= 0;
		}

		default void fixItem(ItemStack item) {
			item.id = this.mapItem(item.id);
		}
	}
}
