package net.ornithemc.osl.datagen.api.provider.tag;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tag.BlockTagsProvider;
import net.ornithemc.osl.datagen.api.PackGenerator;

/**
 * A DataProvider for generating block tags.
 */
public abstract class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(PackGenerator generator) {
        super((DataGenerator) generator);
    }

    @Override
    protected abstract void addTags();
}
