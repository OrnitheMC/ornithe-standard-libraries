package net.ornithemc.osl.items.impl.item;

import net.minecraft.item.Item;

import net.ornithemc.osl.registries.api.registry.sync.IdFixer;

public class ItemIdFixer implements IdFixer {

	@Override
	public void apply() {
		for (int id = 0; id < Item.BY_ID.length; id++) {
			Item item = Item.BY_ID[id];

			if (item != null) {
				item.id = id;
			}
		}
	}
}
