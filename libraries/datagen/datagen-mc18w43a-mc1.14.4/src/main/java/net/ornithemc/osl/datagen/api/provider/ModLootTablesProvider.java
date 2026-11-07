package net.ornithemc.osl.datagen.api.provider;

import com.google.gson.JsonElement;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.resource.Identifier;
import net.minecraft.world.gen.loot.LootTable;
import net.minecraft.world.gen.loot.LootTables;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.datagen.api.PackGenerator;
import net.ornithemc.osl.datagen.api.provider.loot.LootTablesSubProvider;
import net.ornithemc.osl.resource.loader.api.resource.ResourcePath;
import net.ornithemc.osl.resource.loader.api.resource.ResourceType;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * A DataProvider to generate loot tables.
 */
public abstract class ModLootTablesProvider extends GsonDataProvider {
    private final Set<Object> subProviders;

    protected ModLootTablesProvider(PackGenerator generator, ModContainer mod) {
        super(generator, mod);
        subProviders = new HashSet<>();
    }

    @Override
    protected final void generate(BiConsumer<NamespacedIdentifier, JsonElement> consumer) {
        generateLootTables();

        subProviders.forEach(subProvider -> {
            if (subProvider instanceof Consumer) {
                ((Consumer<BiConsumer<Identifier, LootTable.C_96647086>>) subProvider).accept((id, lootTable) -> consumer.accept(id, LootTables.m_19064268(lootTable.m_74891066()).getAsJsonObject()));
            } else if (subProvider instanceof LootTablesSubProvider) {
                ((LootTablesSubProvider) subProvider).accept((id, lootTable) -> consumer.accept(id, LootTables.m_19064268(lootTable.m_74891066()).getAsJsonObject()));
            }
        });
    }

    /**
     * Adds a sub-provider that generates loot tables.
     */
    public void addSubProvider(Consumer<BiConsumer<Identifier, LootTable.C_96647086>> subProvider) {
        subProviders.add(subProvider);
    }

    /**
     * Adds a sub-provider that generates loot tables.
     */
    public void addSubProvider(LootTablesSubProvider subProvider) {
        subProviders.add(subProvider);
    }

    public abstract void generateLootTables();

    @Override
    protected Path getPath(NamespacedIdentifier id) {
        return generator.getOutputPath().resolve(ResourcePath.nameOf(ResourceType.SERVER_DATA, id.prefixed("loot_tables/").suffixed(".json")));
    }

    @Override
    public String getProviderName() {
        return "LootTables";
    }
}
