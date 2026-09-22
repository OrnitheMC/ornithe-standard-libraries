package net.ornithemc.osl.datagen.impl.model;

import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.state.property.SlabType;
import net.minecraft.block.state.property.StairHalf;
import net.minecraft.block.state.property.StairShape;
import net.minecraft.util.math.Direction;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.datagen.api.model.ModelBuilder;
import net.ornithemc.osl.datagen.api.model.ModelGenerator;
import net.ornithemc.osl.datagen.api.model.block.BlockModelDefinitionBuilder;
import net.ornithemc.osl.datagen.api.model.block.BlockModelTemplates;
import net.ornithemc.osl.datagen.api.model.block.VariantsBuilder;

public class BlockModelTemplatesImpl implements BlockModelTemplates {
    private final ModelGenerator modelGenerator;

    public BlockModelTemplatesImpl(ModelGenerator modelGenerator) {
        this.modelGenerator = modelGenerator;
    }

    @Override
    public BlockModelTemplates simpleBlock(Block block, NamespacedIdentifier texture) {
        NamespacedIdentifier blockModelId = modelGenerator.blockModel(block, BlockModelTemplates.cubeAll(texture));
        modelGenerator.itemModel(block, ModelBuilder.create(blockModelId));
        modelGenerator.blockstate(block, BlockModelDefinitionBuilder.createSimple(blockModelId));

        return this;
    }

    @Override
    public BlockModelTemplates simpleBlockWithoutItem(Block block, NamespacedIdentifier texture) {
        NamespacedIdentifier blockModelId = modelGenerator.blockModel(block, BlockModelTemplates.cubeAll(texture));
        modelGenerator.blockstate(block, BlockModelDefinitionBuilder.createSimple(blockModelId));

        return this;
    }

    @Override
    public BlockModelTemplates slab(Block block, NamespacedIdentifier doubleModelLocation, NamespacedIdentifier sideTexture, NamespacedIdentifier topTexture, NamespacedIdentifier bottomTexture) {
        NamespacedIdentifier bottomId = modelGenerator.blockModel(block, BlockModelTemplates.slabBottom(sideTexture, topTexture, bottomTexture));
        NamespacedIdentifier topId = modelGenerator.blockModel(block, "_top", BlockModelTemplates.slabTop(sideTexture, topTexture, bottomTexture));
        modelGenerator.itemModel(block, ModelBuilder.create(bottomId));
        modelGenerator.blockstate(
                block,
                BlockModelDefinitionBuilder.create()
                        .variant(BlockModelDefinitionBuilder.predicate(SlabBlock.HALF, SlabType.BOTTOM), bottomId)
                        .variant(BlockModelDefinitionBuilder.predicate(SlabBlock.HALF, SlabType.TOP), topId)
                        .variant(BlockModelDefinitionBuilder.predicate(SlabBlock.HALF, SlabType.DOUBLE), doubleModelLocation)
        );

        return this;
    }

    @Override
    public BlockModelTemplates simpleSlab(Block block, NamespacedIdentifier doubleModelLocation, NamespacedIdentifier texture) {
        return slab(block, doubleModelLocation, texture, texture, texture);
    }

    @Override
    public BlockModelTemplates stairs(Block block, NamespacedIdentifier sideTexture, NamespacedIdentifier topTexture, NamespacedIdentifier bottomTexture) {
        NamespacedIdentifier baseId = modelGenerator.blockModel(block, BlockModelTemplates.stairs(sideTexture, topTexture, bottomTexture));
        NamespacedIdentifier innerId = modelGenerator.blockModel(block, "_inner", BlockModelTemplates.stairsInner(sideTexture, topTexture, bottomTexture));
        NamespacedIdentifier outerId = modelGenerator.blockModel(block, "_outer", BlockModelTemplates.stairsOuter(sideTexture, topTexture, bottomTexture));
        modelGenerator.itemModel(block, ModelBuilder.create(baseId));

        modelGenerator.blockstate(
                block,
                BlockModelDefinitionBuilder.create()
                        // Straight Bottom
                        .variant(
                                BlockModelDefinitionBuilder.predicate(StairsBlock.FACING, Direction.EAST)
                                        .set(StairsBlock.HALF, StairHalf.BOTTOM)
                                        .set(StairsBlock.SHAPE, StairShape.STRAIGHT),
                                VariantsBuilder.of(baseId)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(StairsBlock.FACING, Direction.WEST)
                                        .set(StairsBlock.HALF, StairHalf.BOTTOM)
                                        .set(StairsBlock.SHAPE, StairShape.STRAIGHT),
                                VariantsBuilder.of(baseId).y(180).lockedUV(true)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(StairsBlock.FACING, Direction.SOUTH)
                                        .set(StairsBlock.HALF, StairHalf.BOTTOM)
                                        .set(StairsBlock.SHAPE, StairShape.STRAIGHT),
                                VariantsBuilder.of(baseId).y(90).lockedUV(true)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(StairsBlock.FACING, Direction.NORTH)
                                        .set(StairsBlock.HALF, StairHalf.BOTTOM)
                                        .set(StairsBlock.SHAPE, StairShape.STRAIGHT),
                                VariantsBuilder.of(baseId).y(270).lockedUV(true)
                        )
                        // Outer Right Bottom
                        .variant(
                                BlockModelDefinitionBuilder.predicate(StairsBlock.FACING, Direction.EAST)
                                        .set(StairsBlock.HALF, StairHalf.BOTTOM)
                                        .set(StairsBlock.SHAPE, StairShape.OUTER_RIGHT),
                                VariantsBuilder.of(outerId)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(StairsBlock.FACING, Direction.WEST)
                                        .set(StairsBlock.HALF, StairHalf.BOTTOM)
                                        .set(StairsBlock.SHAPE, StairShape.OUTER_RIGHT),
                                VariantsBuilder.of(outerId).y(180).lockedUV(true)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(StairsBlock.FACING, Direction.SOUTH)
                                        .set(StairsBlock.HALF, StairHalf.BOTTOM)
                                        .set(StairsBlock.SHAPE, StairShape.OUTER_RIGHT),
                                VariantsBuilder.of(outerId).y(90).lockedUV(true)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(StairsBlock.FACING, Direction.NORTH)
                                        .set(StairsBlock.HALF, StairHalf.BOTTOM)
                                        .set(StairsBlock.SHAPE, StairShape.OUTER_RIGHT),
                                VariantsBuilder.of(outerId).y(270).lockedUV(true)
                        )
                        // Outer Left Bottom
                        .variant(
                                BlockModelDefinitionBuilder.predicate(StairsBlock.FACING, Direction.EAST)
                                        .set(StairsBlock.HALF, StairHalf.BOTTOM)
                                        .set(StairsBlock.SHAPE, StairShape.OUTER_LEFT),
                                VariantsBuilder.of(outerId).y(270).lockedUV(true)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(StairsBlock.FACING, Direction.WEST)
                                        .set(StairsBlock.HALF, StairHalf.BOTTOM)
                                        .set(StairsBlock.SHAPE, StairShape.OUTER_LEFT),
                                VariantsBuilder.of(outerId).y(90).lockedUV(true)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(StairsBlock.FACING, Direction.SOUTH)
                                        .set(StairsBlock.HALF, StairHalf.BOTTOM)
                                        .set(StairsBlock.SHAPE, StairShape.OUTER_LEFT),
                                VariantsBuilder.of(outerId)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(StairsBlock.FACING, Direction.NORTH)
                                        .set(StairsBlock.HALF, StairHalf.BOTTOM)
                                        .set(StairsBlock.SHAPE, StairShape.OUTER_LEFT),
                                VariantsBuilder.of(outerId).y(180).lockedUV(true)
                        )
                        // Inner Right Bottom
                        .variant(
                                BlockModelDefinitionBuilder.predicate(StairsBlock.FACING, Direction.EAST)
                                        .set(StairsBlock.HALF, StairHalf.BOTTOM)
                                        .set(StairsBlock.SHAPE, StairShape.INNER_RIGHT),
                                VariantsBuilder.of(innerId)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(StairsBlock.FACING, Direction.WEST)
                                        .set(StairsBlock.HALF, StairHalf.BOTTOM)
                                        .set(StairsBlock.SHAPE, StairShape.INNER_RIGHT),
                                VariantsBuilder.of(innerId).y(180).lockedUV(true)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(StairsBlock.FACING, Direction.SOUTH)
                                        .set(StairsBlock.HALF, StairHalf.BOTTOM)
                                        .set(StairsBlock.SHAPE, StairShape.INNER_RIGHT),
                                VariantsBuilder.of(innerId).y(90).lockedUV(true)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(StairsBlock.FACING, Direction.NORTH)
                                        .set(StairsBlock.HALF, StairHalf.BOTTOM)
                                        .set(StairsBlock.SHAPE, StairShape.INNER_RIGHT),
                                VariantsBuilder.of(innerId).y(270).lockedUV(true)
                        )
                        // Inner Left Bottom
                        .variant(
                                BlockModelDefinitionBuilder.predicate(StairsBlock.FACING, Direction.EAST)
                                        .set(StairsBlock.HALF, StairHalf.BOTTOM)
                                        .set(StairsBlock.SHAPE, StairShape.INNER_LEFT),
                                VariantsBuilder.of(innerId).y(270).lockedUV(true)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(StairsBlock.FACING, Direction.WEST)
                                        .set(StairsBlock.HALF, StairHalf.BOTTOM)
                                        .set(StairsBlock.SHAPE, StairShape.INNER_LEFT),
                                VariantsBuilder.of(innerId).y(90).lockedUV(true)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(StairsBlock.FACING, Direction.SOUTH)
                                        .set(StairsBlock.HALF, StairHalf.BOTTOM)
                                        .set(StairsBlock.SHAPE, StairShape.INNER_LEFT),
                                VariantsBuilder.of(innerId)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(StairsBlock.FACING, Direction.NORTH)
                                        .set(StairsBlock.HALF, StairHalf.BOTTOM)
                                        .set(StairsBlock.SHAPE, StairShape.INNER_LEFT),
                                VariantsBuilder.of(innerId).y(180).lockedUV(true)
                        )

                        // Straight Top
                        .variant(
                                BlockModelDefinitionBuilder.predicate(StairsBlock.FACING, Direction.EAST)
                                        .set(StairsBlock.HALF, StairHalf.TOP)
                                        .set(StairsBlock.SHAPE, StairShape.STRAIGHT),
                                VariantsBuilder.of(baseId).x(180).lockedUV(true)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(StairsBlock.FACING, Direction.WEST)
                                        .set(StairsBlock.HALF, StairHalf.TOP)
                                        .set(StairsBlock.SHAPE, StairShape.STRAIGHT),
                                VariantsBuilder.of(baseId).x(180).y(180).lockedUV(true)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(StairsBlock.FACING, Direction.SOUTH)
                                        .set(StairsBlock.HALF, StairHalf.TOP)
                                        .set(StairsBlock.SHAPE, StairShape.STRAIGHT),
                                VariantsBuilder.of(baseId).x(180).y(90).lockedUV(true)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(StairsBlock.FACING, Direction.NORTH)
                                        .set(StairsBlock.HALF, StairHalf.TOP)
                                        .set(StairsBlock.SHAPE, StairShape.STRAIGHT),
                                VariantsBuilder.of(baseId).x(180).y(270).lockedUV(true)
                        )
                        // Outer Right Top
                        .variant(
                                BlockModelDefinitionBuilder.predicate(StairsBlock.FACING, Direction.EAST)
                                        .set(StairsBlock.HALF, StairHalf.TOP)
                                        .set(StairsBlock.SHAPE, StairShape.OUTER_RIGHT),
                                VariantsBuilder.of(outerId).x(180).lockedUV(true)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(StairsBlock.FACING, Direction.WEST)
                                        .set(StairsBlock.HALF, StairHalf.TOP)
                                        .set(StairsBlock.SHAPE, StairShape.OUTER_RIGHT),
                                VariantsBuilder.of(outerId).x(180).y(180).lockedUV(true)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(StairsBlock.FACING, Direction.SOUTH)
                                        .set(StairsBlock.HALF, StairHalf.TOP)
                                        .set(StairsBlock.SHAPE, StairShape.OUTER_RIGHT),
                                VariantsBuilder.of(outerId).x(180).y(90).lockedUV(true)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(StairsBlock.FACING, Direction.NORTH)
                                        .set(StairsBlock.HALF, StairHalf.TOP)
                                        .set(StairsBlock.SHAPE, StairShape.OUTER_RIGHT),
                                VariantsBuilder.of(outerId).x(180).y(270).lockedUV(true)
                        )
                        // Outer Left Top
                        .variant(
                                BlockModelDefinitionBuilder.predicate(StairsBlock.FACING, Direction.EAST)
                                        .set(StairsBlock.HALF, StairHalf.TOP)
                                        .set(StairsBlock.SHAPE, StairShape.OUTER_LEFT),
                                VariantsBuilder.of(outerId).x(180).y(270).lockedUV(true)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(StairsBlock.FACING, Direction.WEST)
                                        .set(StairsBlock.HALF, StairHalf.TOP)
                                        .set(StairsBlock.SHAPE, StairShape.OUTER_LEFT),
                                VariantsBuilder.of(outerId).x(180).y(90).lockedUV(true)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(StairsBlock.FACING, Direction.SOUTH)
                                        .set(StairsBlock.HALF, StairHalf.TOP)
                                        .set(StairsBlock.SHAPE, StairShape.OUTER_LEFT),
                                VariantsBuilder.of(outerId).x(180).lockedUV(true)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(StairsBlock.FACING, Direction.NORTH)
                                        .set(StairsBlock.HALF, StairHalf.TOP)
                                        .set(StairsBlock.SHAPE, StairShape.OUTER_LEFT),
                                VariantsBuilder.of(outerId).x(180).y(180).lockedUV(true)
                        )
                        // Inner Right Top
                        .variant(
                                BlockModelDefinitionBuilder.predicate(StairsBlock.FACING, Direction.EAST)
                                        .set(StairsBlock.HALF, StairHalf.TOP)
                                        .set(StairsBlock.SHAPE, StairShape.INNER_RIGHT),
                                VariantsBuilder.of(innerId).x(180).lockedUV(true)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(StairsBlock.FACING, Direction.WEST)
                                        .set(StairsBlock.HALF, StairHalf.TOP)
                                        .set(StairsBlock.SHAPE, StairShape.INNER_RIGHT),
                                VariantsBuilder.of(innerId).x(180).y(180).lockedUV(true)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(StairsBlock.FACING, Direction.SOUTH)
                                        .set(StairsBlock.HALF, StairHalf.TOP)
                                        .set(StairsBlock.SHAPE, StairShape.INNER_RIGHT),
                                VariantsBuilder.of(innerId).x(180).y(90).lockedUV(true)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(StairsBlock.FACING, Direction.NORTH)
                                        .set(StairsBlock.HALF, StairHalf.TOP)
                                        .set(StairsBlock.SHAPE, StairShape.INNER_RIGHT),
                                VariantsBuilder.of(innerId).x(180).y(270).lockedUV(true)
                        )
                        // Inner Left Top
                        .variant(
                                BlockModelDefinitionBuilder.predicate(StairsBlock.FACING, Direction.EAST)
                                        .set(StairsBlock.HALF, StairHalf.TOP)
                                        .set(StairsBlock.SHAPE, StairShape.INNER_LEFT),
                                VariantsBuilder.of(innerId).x(180).y(270).lockedUV(true)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(StairsBlock.FACING, Direction.WEST)
                                        .set(StairsBlock.HALF, StairHalf.TOP)
                                        .set(StairsBlock.SHAPE, StairShape.INNER_LEFT),
                                VariantsBuilder.of(innerId).x(180).y(90).lockedUV(true)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(StairsBlock.FACING, Direction.SOUTH)
                                        .set(StairsBlock.HALF, StairHalf.TOP)
                                        .set(StairsBlock.SHAPE, StairShape.INNER_LEFT),
                                VariantsBuilder.of(innerId).x(180).lockedUV(true)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(StairsBlock.FACING, Direction.NORTH)
                                        .set(StairsBlock.HALF, StairHalf.TOP)
                                        .set(StairsBlock.SHAPE, StairShape.INNER_LEFT),
                                VariantsBuilder.of(innerId).x(180).y(180).lockedUV(true)
                        )
        );

        return this;
    }

    @Override
    public BlockModelTemplates simpleStairs(Block block, NamespacedIdentifier texture) {
        return stairs(block, texture, texture, texture);
    }
}
