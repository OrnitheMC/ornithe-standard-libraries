package net.ornithemc.osl.datagen.api;

import net.fabricmc.loader.api.ModContainer;
import net.ornithemc.osl.datagen.api.provider.ModDataProvider;

import java.nio.file.Path;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A generator for a pack of resources.
 */
public interface PackGenerator {
    /**
     * Adds a provider to the pack.
     */
    void addProvider(ComplexProviderFactory provider);
    /**
     * Adds a provider to the pack.
     */
    void addProvider(SimpleProviderFactory provider);
    /**
     * Returns the output path for the pack.
     */
    Path getOutputPath();

    @FunctionalInterface
    interface SimpleProviderFactory {
        ModDataProvider create(PackGenerator generator);
    }

    @FunctionalInterface
    interface ComplexProviderFactory {
        ModDataProvider create(PackGenerator generator, ModContainer container);
    }
}
