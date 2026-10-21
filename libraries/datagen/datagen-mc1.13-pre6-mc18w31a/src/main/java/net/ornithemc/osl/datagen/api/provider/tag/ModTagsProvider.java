package net.ornithemc.osl.datagen.api.provider.tag;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tag.TagsProvider;
import net.minecraft.resource.Identifier;
import net.minecraft.util.registry.IdRegistry;
import net.minecraft.util.registry.Registry;
import net.ornithemc.osl.datagen.api.PackGenerator;

/**
 * A base DataProvider for generating tags.
 */
public abstract class ModTagsProvider<T> extends TagsProvider<T> {
    protected ModTagsProvider(PackGenerator generator, IdRegistry<Identifier, T> registry) {
        super((DataGenerator) generator, registry);
    }
}
