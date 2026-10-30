package net.ornithemc.osl.datagen.api.provider.tag;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tag.FluidTagsProvider;
import net.ornithemc.osl.datagen.api.PackGenerator;

/**
 * A DataProvider for generating fluid tags.
 */
public abstract class ModFluidTagsProvider extends FluidTagsProvider {
    public ModFluidTagsProvider(PackGenerator generator) {
        super((DataGenerator) generator);
    }

    @Override
    protected abstract void addTags();
}
