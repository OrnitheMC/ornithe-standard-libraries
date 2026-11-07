package net.ornithemc.osl.datagen.api.provider.tag;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tag.ItemTagsProvider;
import net.ornithemc.osl.datagen.api.PackGenerator;

/**
 * A DataProvider for generating item tags.
 */
public abstract class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(PackGenerator generator) {
        super((DataGenerator) generator);
    }

    @Override
    protected abstract void addTags();
}
