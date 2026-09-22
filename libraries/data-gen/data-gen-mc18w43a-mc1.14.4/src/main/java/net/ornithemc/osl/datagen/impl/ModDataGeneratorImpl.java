package net.ornithemc.osl.datagen.impl;

import net.fabricmc.loader.api.ModContainer;
import net.minecraft.data.DataGenerator;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.datagen.api.ModDataGenerator;
import net.ornithemc.osl.datagen.api.PackGenerator;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModDataGeneratorImpl implements ModDataGenerator {
    private final Path basePath;
    private final ModContainer modContainer;
    private final List<DataGenerator> generators;

    public ModDataGeneratorImpl(Path basePath, ModContainer modContainer) {
        this.basePath = basePath;
        this.modContainer = modContainer;
        generators = new ArrayList<>();
    }

    @Override
    public PackGenerator createPack() {
        DataGenerator generator = new PackDataGeneratorImpl(basePath, Collections.emptySet(), modContainer.getMetadata().getDescription());
        generators.add(generator);
        return generator;
    }

    @Override
    public PackGenerator createBuiltinResourcePack(NamespacedIdentifier id) {
        DataGenerator generator = new PackDataGeneratorImpl(basePath.resolve(id.prefixed("resourcepacks/").identifier()), Collections.emptySet(), id.toString());
        generators.add(generator);
        return generator;
    }

    @Override
    public ModContainer getMod() {
        return modContainer;
    }

    public void run() {
        for (DataGenerator generator : generators) {
            try {
                generator.run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
