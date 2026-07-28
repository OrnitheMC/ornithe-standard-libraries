package net.ornithemc.osl.items.impl.item;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;

import net.minecraft.item.ItemStack;

import net.ornithemc.osl.registries.api.registry.sync.IdMapper;
import net.ornithemc.osl.registries.api.registry.sync.IntegerMapMapper;
import net.ornithemc.osl.registries.api.registry.sync.RegistryMappings;

public class SmeltingRecipesMapper implements IdMapper {

	public static SmeltingRecipesMapper of(Map<Integer, ItemStack> recipes) {
		return new SmeltingRecipesMapper(recipes);
	}

	private final IntegerMapMapper registryMapper;

	private final Map<Integer, ItemStack> recipes;
	private final Map<Integer, ItemStack> missing;

	private boolean applied;

	private SmeltingRecipesMapper(Map<Integer, ItemStack> recipes) {
		this.registryMapper = IntegerMapMapper.of(recipes);

		this.recipes = recipes;
		this.missing = new HashMap<>();
	}

	@Override
	public void apply(RegistryMappings mappings) {
		this.missing.clear();
		this.fixRecipes(mappings::remap, true);

		this.registryMapper.apply(mappings);

		this.applied = true;
	}

	@Override
	public void undo(RegistryMappings mappings) {
		this.registryMapper.undo(mappings);

		if (this.applied) {
			this.fixRecipes(mappings::unmap, false);
			this.recipes.putAll(this.missing);
		}

		this.applied = false;
	}

	private void fixRecipes(Int2IntFunction mapper, boolean storeMissing) {
		Iterator<Map.Entry<Integer, ItemStack>> it = this.recipes.entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry<Integer, ItemStack> recipe = it.next();

			int ingredient = recipe.getKey();
			ItemStack result = recipe.getValue();

			int oldId = result.id;
			int newId = mapper.applyAsInt(oldId);

			if (newId >= 0) {
				result.id = newId;
			} else {
				it.remove();

				if (storeMissing) {
					this.missing.put(ingredient, result);
				}
			}
		}
	}
}
