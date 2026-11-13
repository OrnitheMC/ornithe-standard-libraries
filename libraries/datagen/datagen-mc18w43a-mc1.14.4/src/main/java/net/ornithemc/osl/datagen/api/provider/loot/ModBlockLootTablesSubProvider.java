package net.ornithemc.osl.datagen.api.provider.loot;

import net.minecraft.resource.Identifier;
import net.minecraft.unmapped.C_63583284;
import net.minecraft.unmapped.C_70034155;
import net.minecraft.world.gen.loot.LootTable;

import java.util.function.BiConsumer;

/**
 * A sub-provider that generates loot tables for blocks.
 */
public abstract class ModBlockLootTablesSubProvider extends C_63583284 {
    @Override
    public void accept(BiConsumer<Identifier, LootTable.C_96647086> biConsumer) {
        add();

        f_14537958.values().forEach(table -> table.m_88411316(C_70034155.f_02220120));
        f_14537958.forEach(biConsumer);
    }

    public abstract void add();
}
