package net.ornithemc.osl.datagen.api.provider.loot;

import com.google.common.collect.Maps;
import net.minecraft.world.gen.loot.LootContextParameterSet;
import net.minecraft.world.gen.loot.LootTable;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

import java.util.Map;
import java.util.function.BiConsumer;

/**
 * A sub-provider that generates loot tables.
 */
public abstract class ModLootTablesSubProvider implements LootTablesSubProvider {
    private final LootContextParameterSet parameterSet;

    private final Map<NamespacedIdentifier, LootTable.C_96647086> lootTables = Maps.newHashMap();

    public ModLootTablesSubProvider(LootContextParameterSet parameterSet) {
        this.parameterSet = parameterSet;
    }

    @Override
    public void accept(BiConsumer<NamespacedIdentifier, LootTable.C_96647086> consumer) {
        add();

        lootTables.values().forEach(table -> table.m_88411316(parameterSet));
        lootTables.forEach(consumer);
    }

    protected void put(NamespacedIdentifier id, LootTable.C_96647086 table) {
        lootTables.put(id, table);
    }

    public abstract void add();
}
