package net.ornithemc.osl.datagen.impl;

import net.minecraft.data.DataGenerator;
import net.ornithemc.osl.datagen.impl.provider.MCMetaProvider;

import java.nio.file.Path;
import java.util.Collection;

public class PackDataGeneratorImpl extends DataGenerator {
    public PackDataGeneratorImpl(Path output, Collection<Path> inputs, String description) {
        super(output, inputs);
        this.addProvider(new MCMetaProvider(this, description));
    }
}
