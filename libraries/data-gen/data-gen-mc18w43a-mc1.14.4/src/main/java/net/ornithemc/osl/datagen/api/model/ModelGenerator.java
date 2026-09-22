package net.ornithemc.osl.datagen.api.model;

import net.minecraft.block.Block;
import net.minecraft.item.ItemLike;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.datagen.api.model.block.BlockModelDefinitionBuilder;
import net.ornithemc.osl.datagen.api.model.block.BlockModelTemplates;

public interface ModelGenerator {
    void blockstate(Block block, BlockModelDefinitionBuilder builder);
    void itemModel(ItemLike item, ModelBuilder builder);
    NamespacedIdentifier blockModel(NamespacedIdentifier identifier, ModelBuilder builder);
    NamespacedIdentifier blockModel(Block block, ModelBuilder builder);
    NamespacedIdentifier blockModel(Block block, String suffix, ModelBuilder builder);

    BlockModelTemplates blockTemplates();
}
