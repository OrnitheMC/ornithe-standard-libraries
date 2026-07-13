package net.ornithemc.osl.items.impl.item;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;

import net.minecraft.item.ItemStack;
import net.minecraft.world.gen.structure.LootEntry;

import net.ornithemc.osl.items.impl.ItemsMixinPlugin;
import net.ornithemc.osl.items.impl.LootEntryAccess;
import net.ornithemc.osl.registries.api.registry.sync.IdMapper;
import net.ornithemc.osl.registries.api.registry.sync.RegistryMappings;

public class LootTableMapper implements IdMapper {

	public static LootTableMapper of(LootEntry... lootTable) {
		return new LootTableMapper(lootTable);
	}

	private final LootEntry[] lootTable;
	private final Set<LootEntry> missing;

	private boolean applied;

	private LootTableMapper(LootEntry[] lootTable) {
		this.lootTable = lootTable;
		this.missing = Collections.newSetFromMap(new IdentityHashMap<>());
	}

	@Override
	public void apply(RegistryMappings mappings) {
		this.missing.clear();
		this.fixLootItems(mappings::remap, true);

		this.applied = true;
	}

	@Override
	public void undo(RegistryMappings mappings) {
		if (this.applied) {
			this.fixLootItems(mappings::unmap, false);
			this.missing.clear();
		}

		this.applied = false;
	}

	private void fixLootItems(Int2IntFunction mapper, boolean storeMissing) {
		for (LootEntry loot : this.lootTable) {
			if (this.missing.contains(loot)) {
				continue;
			}

			LootEntryAccess lootAccess = (LootEntryAccess) loot;

			if (ItemsMixinPlugin.LOOT_ENTRY_USES_ITEM_STACK) {
				ItemStack item = lootAccess.osl$items$getItemStack();

				int oldId = item.id;
				int newId = mapper.applyAsInt(oldId);

				if (newId >= 0) {
					item.id = newId;
				} else if (storeMissing) {
					this.missing.add(loot);
				}
			} else {
				int oldId = lootAccess.osl$items$getItem();
				int newId = mapper.applyAsInt(oldId);

				if (newId >= 0) {
					lootAccess.osl$items$setItem(newId);
				} else if (storeMissing) {
					this.missing.add(loot);
				}
			}
		}
	}
}
