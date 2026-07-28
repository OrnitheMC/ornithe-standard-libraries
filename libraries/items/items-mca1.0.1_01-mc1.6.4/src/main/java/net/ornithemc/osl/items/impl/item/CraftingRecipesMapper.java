package net.ornithemc.osl.items.impl.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.ornithemc.osl.items.impl.item.FixableRecipe.ItemMapper;
import net.ornithemc.osl.registries.api.registry.sync.IdMapper;
import net.ornithemc.osl.registries.api.registry.sync.RegistryMappings;

public class CraftingRecipesMapper implements IdMapper {

	public static CraftingRecipesMapper of(List<?> recipes, Comparator<?> sorter) {
		return new CraftingRecipesMapper(recipes, sorter);
	}

	private final List<Object> recipes;
	private final List<Object> missing;
	private final Comparator<Object> sorter;

	private boolean applied;

	@SuppressWarnings("unchecked")
	private CraftingRecipesMapper(List<?> recipes, Comparator<?> sorter) {
		this.recipes = (List<Object>) recipes;
		this.missing = new ArrayList<>();
		this.sorter = (Comparator<Object>) sorter;
	}

	@Override
	public void apply(RegistryMappings mappings) {
		this.missing.clear();
		this.fixRecipes(mappings::remap, true);

		Collections.sort(this.recipes, this.sorter);

		this.applied = true;
	}

	@Override
	public void undo(RegistryMappings mappings) {
		if (this.applied) {
			this.fixRecipes(mappings::unmap, false);
			this.recipes.addAll(missing);

			Collections.sort(this.recipes, this.sorter);
		}

		this.applied = false;
	}

	private void fixRecipes(ItemMapper mapper, boolean storeMissing) {
		Iterator<Object> it = this.recipes.iterator();

		while (it.hasNext()) {
			Object recipe = it.next();

			if (this.canFixRecipe(mapper, recipe)) {
				this.fixRecipe(mapper, recipe);
			} else {
				it.remove();

				if (storeMissing) {
					this.missing.add(recipe);
				}
			}
		}
	}

	private boolean canFixRecipe(ItemMapper mapper, Object recipe) {
		return recipe instanceof FixableRecipe && ((FixableRecipe) recipe).osl$items$canFixRecipe(mapper);
	}

	private void fixRecipe(ItemMapper mapper, Object recipe) {
		((FixableRecipe) recipe).osl$items$fixRecipe(mapper);
	}
}
