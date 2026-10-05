package net.ornithemc.osl.datagen.api.model;

import net.minecraft.block.Block;
import net.minecraft.item.ItemLike;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.datagen.api.model.block.BlockModelDefinitionBuilder;
import net.ornithemc.osl.datagen.api.model.block.BlockModelTemplates;

/**
 * Provides methods for generating model resources.
 */
public interface ModelGenerator {
    /**
     * Generates a blockstate file for the given block.
     *
     * @param block the block to generate the blockstate for
     * @param builder the builder to use for generating the blockstate
     */
    void blockstate(Block block, BlockModelDefinitionBuilder builder);

    /**
     * Generates an item model file for the given item or block item.
     *
     * @param item the item to generate the item model for
     * @param builder the builder to use for generating the item model
     */
    void itemModel(ItemLike item, ModelBuilder builder);

    /**
     * Generates a block model file for the given identifier.
     *
     * @param identifier the identifier to generate the block model for
     * @param builder the builder to use for generating the block model
     * @return the generated block model identifier
     */
    NamespacedIdentifier blockModel(NamespacedIdentifier identifier, ModelBuilder builder);

    /**
     * Generates a block model file for the given block.
     *
     * @param block the block to generate the block model for
     * @param builder the builder to use for generating the block model
     * @return the generated block model identifier
     */
    NamespacedIdentifier blockModel(Block block, ModelBuilder builder);

    /**
     * Generates a block model file for the given block with the given suffix.
     *
     * @param block the block to generate the block model for
     * @param suffix the suffix to append to the block model identifier
     * @param builder the builder to use for generating the block model
     * @return the generated block model identifier
     */
    NamespacedIdentifier blockModel(Block block, String suffix, ModelBuilder builder);

    /**
     * Returns the block model templates.
     *
     * @return the block model templates
     */
    BlockModelTemplates blockTemplates();
}
