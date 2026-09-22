package net.ornithemc.osl.datagen.impl.model;

import com.google.gson.JsonElement;
import net.minecraft.block.Block;
import net.minecraft.item.ItemLike;
import net.ornithemc.osl.blocks.api.BlockRegistry;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.datagen.api.model.ModelBuilder;
import net.ornithemc.osl.datagen.api.model.ModelGenerator;
import net.ornithemc.osl.datagen.api.model.block.BlockModelDefinitionBuilder;
import net.ornithemc.osl.datagen.api.model.block.BlockModelTemplates;
import net.ornithemc.osl.items.api.ItemRegistry;

import java.util.function.BiConsumer;

public class ModelGeneratorImpl implements ModelGenerator {
    private final BiConsumer<NamespacedIdentifier, JsonElement> writer;
    private final BlockModelTemplates templates;

    public ModelGeneratorImpl(BiConsumer<NamespacedIdentifier, JsonElement> writer) {
        this.writer = writer;
        this.templates = new BlockModelTemplatesImpl(this);
    }

    @Override
    public void blockstate(Block block, BlockModelDefinitionBuilder builder) {
        NamespacedIdentifier blockId = BlockRegistry.getIdentifier(block);
        writer.accept(blockId.prefixed("blockstates/"), builder.build());
    }

    @Override
    public void itemModel(ItemLike item, ModelBuilder builder) {
        NamespacedIdentifier itemId = ItemRegistry.getIdentifier(item.asItem());
        writer.accept(itemId.prefixed("models/item/"), builder.build());
    }

    @Override
    public NamespacedIdentifier blockModel(NamespacedIdentifier identifier, ModelBuilder builder) {
        writer.accept(identifier.prefixed("models/block/"), builder.build());
        return identifier.prefixed("block/");
    }

    @Override
    public NamespacedIdentifier blockModel(Block block, ModelBuilder builder) {
        NamespacedIdentifier blockId = BlockRegistry.getIdentifier(block);
        return blockModel(blockId, builder);
    }

    @Override
    public NamespacedIdentifier blockModel(Block block, String suffix, ModelBuilder builder) {
        NamespacedIdentifier blockId = BlockRegistry.getIdentifier(block);
        return blockModel(blockId.suffixed(suffix), builder);
    }

    @Override
    public BlockModelTemplates blockTemplates() {
        return templates;
    }
}
