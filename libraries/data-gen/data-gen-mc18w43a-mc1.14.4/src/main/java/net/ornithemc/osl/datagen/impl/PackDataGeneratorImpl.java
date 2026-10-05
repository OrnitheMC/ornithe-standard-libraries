package net.ornithemc.osl.datagen.impl;

import net.fabricmc.loader.api.ModContainer;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.ornithemc.osl.datagen.api.PackGenerator;
import net.ornithemc.osl.datagen.api.provider.PackProvider;
import net.ornithemc.osl.datagen.impl.provider.MCMetaProvider;
import net.ornithemc.osl.datagen.impl.wrapper.PackProviderWrapper;

import java.nio.file.Path;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;

public class PackDataGeneratorImpl extends DataGenerator {
    private final ModContainer mod;

    public PackDataGeneratorImpl(Path output, Collection<Path> inputs, String description, ModContainer mod) {
        super(output, inputs);
        this.mod = mod;
        this.addProvider((p, m) -> new MCMetaProvider(p, description));
    }

    @Override
    public void addProvider(BiFunction<PackGenerator, ModContainer, PackProvider> providerFunction) {
        PackProvider provider = providerFunction.apply(this, this.mod);

        if (provider instanceof DataProvider) {
            addProvider((DataProvider) provider);
        } else {
            addProvider(new PackProviderWrapper(provider));
        }
    }

    @Override
    public void addProvider(Function<PackGenerator, PackProvider> providerFunction) {
        PackProvider provider = providerFunction.apply(this);

        if (provider instanceof DataProvider) {
            addProvider((DataProvider) provider);
        } else {
            addProvider(new PackProviderWrapper(provider));
        }
    }
}
