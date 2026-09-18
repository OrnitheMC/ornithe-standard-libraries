package net.ornithemc.osl.datagen.api;

import net.ornithemc.osl.datagen.api.provider.PackProvider;

import java.nio.file.Path;

public interface PackGenerator {
    void addProvider(PackProvider provider);
    Path getOutputPath();
}
