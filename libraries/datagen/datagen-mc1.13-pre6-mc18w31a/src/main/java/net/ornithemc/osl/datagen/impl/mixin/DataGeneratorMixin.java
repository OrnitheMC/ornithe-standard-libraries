package net.ornithemc.osl.datagen.impl.mixin;

import net.minecraft.data.DataGenerator;
import net.ornithemc.osl.datagen.api.PackGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.nio.file.Path;

@Mixin(DataGenerator.class)
public abstract class DataGeneratorMixin implements PackGenerator {
    @Shadow
    public abstract Path getOutput();

    @Override
    public Path getOutputPath() {
        return this.getOutput();
    }
}
