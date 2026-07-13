package net.ornithemc.osl.blocks.impl.block;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

import net.ornithemc.osl.blocks.impl.mixin.common.BlockItemAccess;
import net.ornithemc.osl.registries.api.registry.sync.IdMapper;
import net.ornithemc.osl.registries.api.registry.sync.RegistryMappings;

public class BlockItemsMapper implements IdMapper {

	public static BlockItemsMapper of(Item[] items) {
		return new BlockItemsMapper(items);
	}

	private final Item[] items;
	private final Set<Item> missing;

	private boolean applied;

	private BlockItemsMapper(Item[] items) {
		this.items = items;
		this.missing = Collections.newSetFromMap(new IdentityHashMap<>());
	}

	@Override
	public void apply(RegistryMappings mappings) {
		this.missing.clear();
		this.fixBlockItems(mappings::remap, true);

		this.applied = true;
	}

	@Override
	public void undo(RegistryMappings mappings) {
		if (this.applied) {
			this.fixBlockItems(mappings::unmap, false);
			this.missing.clear();
		}

		this.applied = false;
	}

	private void fixBlockItems(Int2IntFunction mapper, boolean storeMissing) {
		for (Item item : this.items) {
			if (item == null || !(item instanceof BlockItem)) {
				continue;
			}

			BlockItemAccess blockItem = (BlockItemAccess) item;

			if (this.missing.contains(item)) {
				continue;
			}

			int oldId = blockItem.accessBlock();
			int newId = mapper.applyAsInt(oldId);

			if (newId >= 0) {
				blockItem.setBlock(newId);
			} else if (storeMissing) {
				this.missing.add(item);
			}
		}
	}
}
