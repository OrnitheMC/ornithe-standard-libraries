package net.ornithemc.osl.datagen.api.provider;

import com.google.gson.JsonElement;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.world.gen.loot.LootTable;
import net.minecraft.world.gen.loot.LootTables;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.datagen.api.PackGenerator;
import net.ornithemc.osl.resource.loader.api.resource.ResourcePath;
import net.ornithemc.osl.resource.loader.api.resource.ResourceType;

import java.nio.file.Path;
import java.util.function.BiConsumer;

public abstract class ModLootTablesProvider extends GsonPackProvider {
    protected ModLootTablesProvider(PackGenerator generator, ModContainer mod) {
        super(generator, mod);
    }

    @Override
    protected final void generate(BiConsumer<NamespacedIdentifier, JsonElement> consumer) {
        generateLootTables((id, lootTable) -> consumer.accept(id, LootTables.m_19064268(lootTable.m_74891066()).getAsJsonObject()));
    }

    public abstract void generateLootTables(BiConsumer<NamespacedIdentifier, LootTable.C_96647086> consumer);

    @Override
    protected Path getPath(NamespacedIdentifier id) {
        return generator.getOutputPath().resolve(ResourcePath.nameOf(ResourceType.SERVER_DATA, id.prefixed("loot_tables/").suffixed(".json")));
    }

    @Override
    public String getProviderName() {
        return "LootTables";
    }
}
