package net.ornithemc.osl.datagen.api.provider.tag;

import net.minecraft.data.DataGenerator;
import net.minecraft.unmapped.C_01952875;
import net.ornithemc.osl.datagen.api.PackGenerator;

/**
 * A DataProvider for generating entity type tags.
 */
public abstract class ModEntityTypeTagsProvider extends C_01952875 {
    public ModEntityTypeTagsProvider(PackGenerator generator) {
        super((DataGenerator) generator);
    }

    @Override
    protected abstract void addTags();
}
