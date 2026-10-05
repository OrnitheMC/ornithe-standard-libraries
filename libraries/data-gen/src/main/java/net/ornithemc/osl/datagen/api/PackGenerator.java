package net.ornithemc.osl.datagen.api;

import net.fabricmc.loader.api.ModContainer;
import net.ornithemc.osl.datagen.api.provider.PackProvider;

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
    void addProvider(BiFunction<PackGenerator, ModContainer, PackProvider> provider);
    /**
     * Adds a provider to the pack.
     */
    void addProvider(Function<PackGenerator, PackProvider> provider);
    /**
     * Returns the output path for the pack.
     */
    Path getOutputPath();
}
