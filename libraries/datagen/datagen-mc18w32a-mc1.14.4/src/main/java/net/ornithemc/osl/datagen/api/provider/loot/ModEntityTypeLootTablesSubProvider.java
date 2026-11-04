package net.ornithemc.osl.datagen.api.provider.loot;

import net.minecraft.resource.Identifier;
import net.minecraft.unmapped.C_70034155;
import net.minecraft.unmapped.C_89378069;
import net.minecraft.world.gen.loot.LootTable;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

import java.util.function.BiConsumer;

/**
 * A sub-provider that generates loot tables for entity types.
 * <p>
 * Only available in 1.14 snapshots and later.
 */
public abstract class ModEntityTypeLootTablesSubProvider extends C_89378069 {
    @Override
    public void accept(BiConsumer<Identifier, LootTable.C_96647086> biConsumer) {
        add();

        f_30315066.values().forEach(table -> table.m_88411316(C_70034155.f_42410675));
        f_30315066.forEach(biConsumer);
    }

    public void put(NamespacedIdentifier identifier, LootTable.C_96647086 table) {
        f_30315066.put(identifier, table);
    }

    public abstract void add();
}
