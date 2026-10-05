package net.ornithemc.osl.datagen.api;

import net.fabricmc.loader.api.ModContainer;
import net.ornithemc.osl.datagen.api.provider.PackProvider;

import java.nio.file.Path;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface PackGenerator {
    void addProvider(BiFunction<PackGenerator, ModContainer, PackProvider> provider);
    void addProvider(Function<PackGenerator, PackProvider> provider);
    Path getOutputPath();
}
