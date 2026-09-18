package net.ornithemc.osl.datagen.impl.mixin;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.ornithemc.osl.datagen.api.PackGenerator;
import net.ornithemc.osl.datagen.api.provider.PackProvider;
import net.ornithemc.osl.datagen.impl.wrapper.PackProviderWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.nio.file.Path;

@Mixin(DataGenerator.class)
public abstract class DataGeneratorMixin implements PackGenerator {
    @Shadow
    public abstract void addProvider(DataProvider provider);

    @Shadow
    public abstract Path getOutput();

    @Override
    public void addProvider(PackProvider provider) {
        if (provider instanceof DataProvider) {
            addProvider((DataProvider) provider);
        } else {
            addProvider(new PackProviderWrapper(provider));
        }
    }

    @Override
    public Path getOutputPath() {
        return this.getOutput();
    }
}
