package net.ornithemc.osl.datagen.api.provider.loot;

import net.minecraft.world.gen.loot.LootTable;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

import java.util.function.BiConsumer;

/**
 * A sub-provider that generates loot tables.
 * <p>
 * Only available in 1.14 snapshots and later.
 */
public interface LootTablesSubProvider {
    void accept(BiConsumer<NamespacedIdentifier, LootTable.C_96647086> consumer);
}
