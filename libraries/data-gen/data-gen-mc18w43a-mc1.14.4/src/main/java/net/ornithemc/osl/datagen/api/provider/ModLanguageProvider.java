package net.ornithemc.osl.datagen.api.provider;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.resource.Identifier;
import net.minecraft.world.biome.Biome;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.datagen.api.PackGenerator;
import net.ornithemc.osl.resource.loader.api.resource.ResourcePath;
import net.ornithemc.osl.resource.loader.api.resource.ResourceType;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public abstract class ModLanguageProvider extends GsonPackProvider {
    private final String languageCode;

    protected ModLanguageProvider(PackGenerator generator, ModContainer mod, String languageCode) {
        super(generator, mod);
        this.languageCode = languageCode;
    }

    protected ModLanguageProvider(PackGenerator generator, ModContainer mod) {
        this(generator, mod, "en_us");
    }

    @Override
    protected final void generate(BiConsumer<NamespacedIdentifier, JsonElement> consumer) {
        Map<String, String> translations = new HashMap<>();

        generateTranslations(translations::put);

        JsonObject jsonObject = new JsonObject();
        translations.forEach(jsonObject::addProperty);

        consumer.accept(new Identifier(mod.getMetadata().getId(), this.languageCode), jsonObject);
    }

    protected abstract void generateTranslations(TranslationBuilder builder);

    @Override
    protected Path getPath(NamespacedIdentifier id) {
        return this.generator.getOutputPath().resolve(ResourcePath.nameOf(ResourceType.CLIENT_ASSETS, id.prefixed("lang").suffixed(".json")));
    }

    @Override
    public String getProviderName() {
        return String.format("Language (%s)", this.languageCode);
    }

    public interface TranslationBuilder {
        void add(String key, String value);

        default void add(Block block, String value) {
            this.add(block.getTranslationKey(), value);
        }

        default void add(Item item, String value) {
            this.add(item.getTranslationKey(), value);
        }

        default void add(EntityType<?> entityType, String value) {
            this.add(entityType.getTranslationKey(), value);
        }

        default void add(Biome biome, String value) {
            this.add(biome.getTranslationKey(), value);
        }
    }
}
