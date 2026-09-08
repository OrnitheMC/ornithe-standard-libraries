package net.ornithemc.osl.items.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.LootEntry;

@Mixin(World.class)
public interface WorldAccess {

	@Accessor("f_27471400")
	static LootEntry[] osl$items$getBonusChestLootEntries() {
		throw new UnsupportedOperationException();
	}
}
