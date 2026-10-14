package net.ornithemc.osl.datagen.api.provider;

import com.google.gson.JsonElement;
import net.fabricmc.loader.api.ModContainer;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.datagen.api.PackGenerator;
import net.ornithemc.osl.datagen.api.model.ModelGenerator;
import net.ornithemc.osl.datagen.impl.model.ModelGeneratorImpl;
import net.ornithemc.osl.resource.loader.api.resource.ResourcePath;
import net.ornithemc.osl.resource.loader.api.resource.ResourceType;

import java.nio.file.Path;
import java.util.function.BiConsumer;

/**
 * A PackProvider to generate block and item models.
 */
public abstract class ModModelProvider extends GsonDataProvider {
    protected ModModelProvider(PackGenerator generator, ModContainer mod) {
        super(generator, mod);
    }

    @Override
    protected final void generate(BiConsumer<NamespacedIdentifier, JsonElement> consumer) {
        ModelGenerator modelGenerator = new ModelGeneratorImpl(consumer);
        generateModels(modelGenerator);
    }

    /**
     * Generates block and item models.
     *
     * @param generator the generator to register the models
     */
    public abstract void generateModels(ModelGenerator generator);

    /**
     * Prefixes the given identifier with "block/".
     *
     * @param id the identifier to prefix
     * @return the prefixed identifier
     */
    public NamespacedIdentifier block(NamespacedIdentifier id) {
        return id.suffixed("block/");
    }

    /**
     * Prefixes the given path with "block/".
     *
     * @param path the path to prefix
     * @return the prefixed path as a NamespacedIdentifier
     */
    public NamespacedIdentifier block(String path) {
        return block(NamespacedIdentifiers.parse(path));
    }

    /**
     * Prefixes the given identifier with "item/".
     *
     * @param id the identifier to prefix
     * @return the prefixed identifier
     */
    public NamespacedIdentifier item(NamespacedIdentifier id) {
        return id.suffixed("item/");
    }

    /**
     * Prefixes the given path with "item/".
     *
     * @param path the path to prefix
     * @return the prefixed path as a NamespacedIdentifier
     */
    public NamespacedIdentifier item(String path) {
        return item(NamespacedIdentifiers.parse(path));
    }

    @Override
    protected Path getPath(NamespacedIdentifier id) {
        return generator.getOutputPath().resolve(ResourcePath.nameOf(ResourceType.CLIENT_ASSETS, id.suffixed(".json")));
    }

    @Override
    public String getProviderName() {
        return "Models";
    }
}
