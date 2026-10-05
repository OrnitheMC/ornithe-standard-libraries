package net.ornithemc.osl.datagen.impl.model;

import net.minecraft.block.*;
import net.minecraft.block.state.property.*;
import net.minecraft.util.math.Direction;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.datagen.api.model.ModelBuilder;
import net.ornithemc.osl.datagen.api.model.ModelGenerator;
import net.ornithemc.osl.datagen.api.model.block.BlockModelDefinitionBuilder;
import net.ornithemc.osl.datagen.api.model.block.BlockModelTemplates;
import net.ornithemc.osl.datagen.api.model.block.ConditionsBuilder;
import net.ornithemc.osl.datagen.api.model.block.VariantsBuilder;
import net.ornithemc.osl.datagen.api.model.item.ItemModelTemplates;

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

    @Override
    public BlockModelTemplates wall(Block block, NamespacedIdentifier texture) {
        NamespacedIdentifier inventoryId = modelGenerator.blockModel(block, "_inventory", BlockModelTemplates.wallInventory(texture));
        NamespacedIdentifier postId = modelGenerator.blockModel(block, "_post", BlockModelTemplates.wallPost(texture));
        NamespacedIdentifier sideId = modelGenerator.blockModel(block, "_side", BlockModelTemplates.wallSide(texture));
        modelGenerator.itemModel(block, ModelBuilder.create(inventoryId));

        modelGenerator.blockstate(
                block,
                BlockModelDefinitionBuilder.create()
                        .selector(
                                ConditionsBuilder.properties(
                                        ConditionsBuilder.property(WallBlock.UP, true)
                                ),
                                VariantsBuilder.of(postId)
                        )
                        .selector(
                                ConditionsBuilder.properties(
                                        ConditionsBuilder.property(WallBlock.NORTH, true)
                                ),
                                VariantsBuilder.of(sideId).lockedUV(true)
                        )
                        .selector(
                                ConditionsBuilder.properties(
                                        ConditionsBuilder.property(WallBlock.EAST, true)
                                ),
                                VariantsBuilder.of(sideId).lockedUV(true).y(90)
                        )
                        .selector(
                                ConditionsBuilder.properties(
                                        ConditionsBuilder.property(WallBlock.SOUTH, true)
                                ),
                                VariantsBuilder.of(sideId).lockedUV(true).y(180)
                        )
                        .selector(
                                ConditionsBuilder.properties(
                                        ConditionsBuilder.property(WallBlock.WEST, true)
                                ),
                                VariantsBuilder.of(sideId).lockedUV(true).y(270)
                        )
        );

        return this;
    }

    @Override
    public BlockModelTemplates fence(Block block, NamespacedIdentifier texture) {
        NamespacedIdentifier inventoryId = modelGenerator.blockModel(block, "_inventory", BlockModelTemplates.fenceInventory(texture));
        NamespacedIdentifier postId = modelGenerator.blockModel(block, "_post", BlockModelTemplates.fencePost(texture));
        NamespacedIdentifier sideId = modelGenerator.blockModel(block, "_side", BlockModelTemplates.fenceSide(texture));
        modelGenerator.itemModel(block, ModelBuilder.create(inventoryId));

        modelGenerator.blockstate(
                block,
                BlockModelDefinitionBuilder.create()
                        .selector(postId)
                        .selector(
                                ConditionsBuilder.properties(
                                        ConditionsBuilder.property(FenceBlock.NORTH, true)
                                ),
                                VariantsBuilder.of(sideId).lockedUV(true)
                        )
                        .selector(
                                ConditionsBuilder.properties(
                                        ConditionsBuilder.property(FenceBlock.EAST, true)
                                ),
                                VariantsBuilder.of(sideId).lockedUV(true).y(90)
                        )
                        .selector(
                                ConditionsBuilder.properties(
                                        ConditionsBuilder.property(FenceBlock.SOUTH, true)
                                ),
                                VariantsBuilder.of(sideId).lockedUV(true).y(180)
                        )
                        .selector(
                                ConditionsBuilder.properties(
                                        ConditionsBuilder.property(FenceBlock.WEST, true)
                                ),
                                VariantsBuilder.of(sideId).lockedUV(true).y(270)
                        )
        );

        return this;
    }

    @Override
    public BlockModelTemplates button(Block block, NamespacedIdentifier texture) {
        NamespacedIdentifier inventoryId = modelGenerator.blockModel(block, "_inventory", BlockModelTemplates.buttonInventory(texture));
        NamespacedIdentifier baseId = modelGenerator.blockModel(block, BlockModelTemplates.button(texture));
        NamespacedIdentifier pressedId = modelGenerator.blockModel(block, "_pressed", BlockModelTemplates.buttonPressed(texture));
        modelGenerator.itemModel(block, ModelBuilder.create(inventoryId));

        modelGenerator.blockstate(
                block,
                BlockModelDefinitionBuilder.create()
                        // Floor not powered
                        .variant(
                                BlockModelDefinitionBuilder.predicate(ButtonBlock.FACE, AttachFace.FLOOR)
                                        .set(ButtonBlock.FACING, Direction.EAST)
                                        .set(ButtonBlock.POWERED, false),
                                VariantsBuilder.of(baseId).y(90)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(ButtonBlock.FACE, AttachFace.FLOOR)
                                        .set(ButtonBlock.FACING, Direction.WEST)
                                        .set(ButtonBlock.POWERED, false),
                                VariantsBuilder.of(baseId).y(270)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(ButtonBlock.FACE, AttachFace.FLOOR)
                                        .set(ButtonBlock.FACING, Direction.SOUTH)
                                        .set(ButtonBlock.POWERED, false),
                                VariantsBuilder.of(baseId).y(180)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(ButtonBlock.FACE, AttachFace.FLOOR)
                                        .set(ButtonBlock.FACING, Direction.NORTH)
                                        .set(ButtonBlock.POWERED, false),
                                VariantsBuilder.of(baseId)
                        )
                        // Wall not powered
                        .variant(
                                BlockModelDefinitionBuilder.predicate(ButtonBlock.FACE, AttachFace.WALL)
                                        .set(ButtonBlock.FACING, Direction.EAST)
                                        .set(ButtonBlock.POWERED, false),
                                VariantsBuilder.of(baseId).lockedUV(true).x(90).y(90)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(ButtonBlock.FACE, AttachFace.WALL)
                                        .set(ButtonBlock.FACING, Direction.WEST)
                                        .set(ButtonBlock.POWERED, false),
                                VariantsBuilder.of(baseId).lockedUV(true).x(90).y(270)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(ButtonBlock.FACE, AttachFace.WALL)
                                        .set(ButtonBlock.FACING, Direction.SOUTH)
                                        .set(ButtonBlock.POWERED, false),
                                VariantsBuilder.of(baseId).lockedUV(true).x(90).y(180)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(ButtonBlock.FACE, AttachFace.WALL)
                                        .set(ButtonBlock.FACING, Direction.NORTH)
                                        .set(ButtonBlock.POWERED, false),
                                VariantsBuilder.of(baseId).lockedUV(true).x(90)
                        )
                        // Ceiling not powered
                        .variant(
                                BlockModelDefinitionBuilder.predicate(ButtonBlock.FACE, AttachFace.CEILING)
                                        .set(ButtonBlock.FACING, Direction.EAST)
                                        .set(ButtonBlock.POWERED, false),
                                VariantsBuilder.of(baseId).x(180).y(270)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(ButtonBlock.FACE, AttachFace.CEILING)
                                        .set(ButtonBlock.FACING, Direction.WEST)
                                        .set(ButtonBlock.POWERED, false),
                                VariantsBuilder.of(baseId).x(180).y(90)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(ButtonBlock.FACE, AttachFace.CEILING)
                                        .set(ButtonBlock.FACING, Direction.SOUTH)
                                        .set(ButtonBlock.POWERED, false),
                                VariantsBuilder.of(baseId).x(180)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(ButtonBlock.FACE, AttachFace.CEILING)
                                        .set(ButtonBlock.FACING, Direction.NORTH)
                                        .set(ButtonBlock.POWERED, false),
                                VariantsBuilder.of(baseId).x(180).y(180)
                        )

                        // Floor powered
                        .variant(
                                BlockModelDefinitionBuilder.predicate(ButtonBlock.FACE, AttachFace.FLOOR)
                                        .set(ButtonBlock.FACING, Direction.EAST)
                                        .set(ButtonBlock.POWERED, true),
                                VariantsBuilder.of(pressedId).y(90)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(ButtonBlock.FACE, AttachFace.FLOOR)
                                        .set(ButtonBlock.FACING, Direction.WEST)
                                        .set(ButtonBlock.POWERED, true),
                                VariantsBuilder.of(pressedId).y(270)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(ButtonBlock.FACE, AttachFace.FLOOR)
                                        .set(ButtonBlock.FACING, Direction.SOUTH)
                                        .set(ButtonBlock.POWERED, true),
                                VariantsBuilder.of(pressedId).y(180)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(ButtonBlock.FACE, AttachFace.FLOOR)
                                        .set(ButtonBlock.FACING, Direction.NORTH)
                                        .set(ButtonBlock.POWERED, true),
                                VariantsBuilder.of(pressedId)
                        )
                        // Wall powered
                        .variant(
                                BlockModelDefinitionBuilder.predicate(ButtonBlock.FACE, AttachFace.WALL)
                                        .set(ButtonBlock.FACING, Direction.EAST)
                                        .set(ButtonBlock.POWERED, true),
                                VariantsBuilder.of(pressedId).lockedUV(true).x(90).y(90)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(ButtonBlock.FACE, AttachFace.WALL)
                                        .set(ButtonBlock.FACING, Direction.WEST)
                                        .set(ButtonBlock.POWERED, true),
                                VariantsBuilder.of(pressedId).lockedUV(true).x(90).y(270)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(ButtonBlock.FACE, AttachFace.WALL)
                                        .set(ButtonBlock.FACING, Direction.SOUTH)
                                        .set(ButtonBlock.POWERED, true),
                                VariantsBuilder.of(pressedId).lockedUV(true).x(90).y(180)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(ButtonBlock.FACE, AttachFace.WALL)
                                        .set(ButtonBlock.FACING, Direction.NORTH)
                                        .set(ButtonBlock.POWERED, true),
                                VariantsBuilder.of(pressedId).lockedUV(true).x(90)
                        )
                        // Ceiling powered
                        .variant(
                                BlockModelDefinitionBuilder.predicate(ButtonBlock.FACE, AttachFace.CEILING)
                                        .set(ButtonBlock.FACING, Direction.EAST)
                                        .set(ButtonBlock.POWERED, true),
                                VariantsBuilder.of(pressedId).x(180).y(270)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(ButtonBlock.FACE, AttachFace.CEILING)
                                        .set(ButtonBlock.FACING, Direction.WEST)
                                        .set(ButtonBlock.POWERED, true),
                                VariantsBuilder.of(pressedId).x(180).y(90)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(ButtonBlock.FACE, AttachFace.CEILING)
                                        .set(ButtonBlock.FACING, Direction.SOUTH)
                                        .set(ButtonBlock.POWERED, true),
                                VariantsBuilder.of(pressedId).x(180)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(ButtonBlock.FACE, AttachFace.CEILING)
                                        .set(ButtonBlock.FACING, Direction.NORTH)
                                        .set(ButtonBlock.POWERED, true),
                                VariantsBuilder.of(pressedId).x(180).y(180)
                        )
        );

        return this;
    }

    @Override
    public BlockModelTemplates door(Block block, NamespacedIdentifier inventoryTexture, NamespacedIdentifier topTexture, NamespacedIdentifier bottomTexture) {
        NamespacedIdentifier bottomId = modelGenerator.blockModel(block, "_bottom", BlockModelTemplates.doorBottom(topTexture, bottomTexture));
        NamespacedIdentifier bottomHingeId = modelGenerator.blockModel(block, "_bottom_hinge", BlockModelTemplates.doorBottomHinge(topTexture, bottomTexture));
        NamespacedIdentifier topId = modelGenerator.blockModel(block, "_top", BlockModelTemplates.doorTop(topTexture, bottomTexture));
        NamespacedIdentifier topHingeId = modelGenerator.blockModel(block, "_top_hinge", BlockModelTemplates.doorTopHinge(topTexture, bottomTexture));
        modelGenerator.itemModel(block, ItemModelTemplates.basic(inventoryTexture));

        modelGenerator.blockstate(
                block,
                BlockModelDefinitionBuilder.create()
                        // Closed lower left
                        .variant(
                                BlockModelDefinitionBuilder.predicate(DoorBlock.FACING, Direction.EAST)
                                        .set(DoorBlock.HALF, Half.LOWER)
                                        .set(DoorBlock.HINGE, DoorHinge.LEFT)
                                        .set(DoorBlock.OPEN, false),
                                VariantsBuilder.of(bottomId)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(DoorBlock.FACING, Direction.SOUTH)
                                        .set(DoorBlock.HALF, Half.LOWER)
                                        .set(DoorBlock.HINGE, DoorHinge.LEFT)
                                        .set(DoorBlock.OPEN, false),
                                VariantsBuilder.of(bottomId).y(90)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(DoorBlock.FACING, Direction.WEST)
                                        .set(DoorBlock.HALF, Half.LOWER)
                                        .set(DoorBlock.HINGE, DoorHinge.LEFT)
                                        .set(DoorBlock.OPEN, false),
                                VariantsBuilder.of(bottomId).y(180)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(DoorBlock.FACING, Direction.NORTH)
                                        .set(DoorBlock.HALF, Half.LOWER)
                                        .set(DoorBlock.HINGE, DoorHinge.LEFT)
                                        .set(DoorBlock.OPEN, false),
                                VariantsBuilder.of(bottomId).y(270)
                        )
                        // Closed lower right
                        .variant(
                                BlockModelDefinitionBuilder.predicate(DoorBlock.FACING, Direction.EAST)
                                        .set(DoorBlock.HALF, Half.LOWER)
                                        .set(DoorBlock.HINGE, DoorHinge.RIGHT)
                                        .set(DoorBlock.OPEN, false),
                                VariantsBuilder.of(bottomHingeId)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(DoorBlock.FACING, Direction.SOUTH)
                                        .set(DoorBlock.HALF, Half.LOWER)
                                        .set(DoorBlock.HINGE, DoorHinge.RIGHT)
                                        .set(DoorBlock.OPEN, false),
                                VariantsBuilder.of(bottomHingeId).y(90)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(DoorBlock.FACING, Direction.WEST)
                                        .set(DoorBlock.HALF, Half.LOWER)
                                        .set(DoorBlock.HINGE, DoorHinge.RIGHT)
                                        .set(DoorBlock.OPEN, false),
                                VariantsBuilder.of(bottomHingeId).y(180)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(DoorBlock.FACING, Direction.NORTH)
                                        .set(DoorBlock.HALF, Half.LOWER)
                                        .set(DoorBlock.HINGE, DoorHinge.RIGHT)
                                        .set(DoorBlock.OPEN, false),
                                VariantsBuilder.of(bottomHingeId).y(270)
                        )
                        // Opened lower left
                        .variant(
                                BlockModelDefinitionBuilder.predicate(DoorBlock.FACING, Direction.EAST)
                                        .set(DoorBlock.HALF, Half.LOWER)
                                        .set(DoorBlock.HINGE, DoorHinge.LEFT)
                                        .set(DoorBlock.OPEN, true),
                                VariantsBuilder.of(bottomHingeId).y(90)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(DoorBlock.FACING, Direction.SOUTH)
                                        .set(DoorBlock.HALF, Half.LOWER)
                                        .set(DoorBlock.HINGE, DoorHinge.LEFT)
                                        .set(DoorBlock.OPEN, true),
                                VariantsBuilder.of(bottomHingeId).y(180)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(DoorBlock.FACING, Direction.WEST)
                                        .set(DoorBlock.HALF, Half.LOWER)
                                        .set(DoorBlock.HINGE, DoorHinge.LEFT)
                                        .set(DoorBlock.OPEN, true),
                                VariantsBuilder.of(bottomHingeId).y(270)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(DoorBlock.FACING, Direction.NORTH)
                                        .set(DoorBlock.HALF, Half.LOWER)
                                        .set(DoorBlock.HINGE, DoorHinge.LEFT)
                                        .set(DoorBlock.OPEN, true),
                                VariantsBuilder.of(bottomHingeId)
                        )
                        // Opened lower right
                        .variant(
                                BlockModelDefinitionBuilder.predicate(DoorBlock.FACING, Direction.EAST)
                                        .set(DoorBlock.HALF, Half.LOWER)
                                        .set(DoorBlock.HINGE, DoorHinge.RIGHT)
                                        .set(DoorBlock.OPEN, true),
                                VariantsBuilder.of(bottomId).y(270)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(DoorBlock.FACING, Direction.SOUTH)
                                        .set(DoorBlock.HALF, Half.LOWER)
                                        .set(DoorBlock.HINGE, DoorHinge.RIGHT)
                                        .set(DoorBlock.OPEN, true),
                                VariantsBuilder.of(bottomId)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(DoorBlock.FACING, Direction.WEST)
                                        .set(DoorBlock.HALF, Half.LOWER)
                                        .set(DoorBlock.HINGE, DoorHinge.RIGHT)
                                        .set(DoorBlock.OPEN, true),
                                VariantsBuilder.of(bottomId).y(90)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(DoorBlock.FACING, Direction.NORTH)
                                        .set(DoorBlock.HALF, Half.LOWER)
                                        .set(DoorBlock.HINGE, DoorHinge.RIGHT)
                                        .set(DoorBlock.OPEN, true),
                                VariantsBuilder.of(bottomId).y(180)
                        )

                        // Closed top left
                        .variant(
                                BlockModelDefinitionBuilder.predicate(DoorBlock.FACING, Direction.EAST)
                                        .set(DoorBlock.HALF, Half.UPPER)
                                        .set(DoorBlock.HINGE, DoorHinge.LEFT)
                                        .set(DoorBlock.OPEN, false),
                                VariantsBuilder.of(topId)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(DoorBlock.FACING, Direction.SOUTH)
                                        .set(DoorBlock.HALF, Half.UPPER)
                                        .set(DoorBlock.HINGE, DoorHinge.LEFT)
                                        .set(DoorBlock.OPEN, false),
                                VariantsBuilder.of(topId).y(90)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(DoorBlock.FACING, Direction.WEST)
                                        .set(DoorBlock.HALF, Half.UPPER)
                                        .set(DoorBlock.HINGE, DoorHinge.LEFT)
                                        .set(DoorBlock.OPEN, false),
                                VariantsBuilder.of(topId).y(180)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(DoorBlock.FACING, Direction.NORTH)
                                        .set(DoorBlock.HALF, Half.UPPER)
                                        .set(DoorBlock.HINGE, DoorHinge.LEFT)
                                        .set(DoorBlock.OPEN, false),
                                VariantsBuilder.of(topId).y(270)
                        )
                        // Closed top right
                        .variant(
                                BlockModelDefinitionBuilder.predicate(DoorBlock.FACING, Direction.EAST)
                                        .set(DoorBlock.HALF, Half.UPPER)
                                        .set(DoorBlock.HINGE, DoorHinge.RIGHT)
                                        .set(DoorBlock.OPEN, false),
                                VariantsBuilder.of(topHingeId)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(DoorBlock.FACING, Direction.SOUTH)
                                        .set(DoorBlock.HALF, Half.UPPER)
                                        .set(DoorBlock.HINGE, DoorHinge.RIGHT)
                                        .set(DoorBlock.OPEN, false),
                                VariantsBuilder.of(topHingeId).y(90)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(DoorBlock.FACING, Direction.WEST)
                                        .set(DoorBlock.HALF, Half.UPPER)
                                        .set(DoorBlock.HINGE, DoorHinge.RIGHT)
                                        .set(DoorBlock.OPEN, false),
                                VariantsBuilder.of(topHingeId).y(180)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(DoorBlock.FACING, Direction.NORTH)
                                        .set(DoorBlock.HALF, Half.UPPER)
                                        .set(DoorBlock.HINGE, DoorHinge.RIGHT)
                                        .set(DoorBlock.OPEN, false),
                                VariantsBuilder.of(topHingeId).y(270)
                        )
                        // Opened top left
                        .variant(
                                BlockModelDefinitionBuilder.predicate(DoorBlock.FACING, Direction.EAST)
                                        .set(DoorBlock.HALF, Half.UPPER)
                                        .set(DoorBlock.HINGE, DoorHinge.LEFT)
                                        .set(DoorBlock.OPEN, true),
                                VariantsBuilder.of(topHingeId).y(90)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(DoorBlock.FACING, Direction.SOUTH)
                                        .set(DoorBlock.HALF, Half.UPPER)
                                        .set(DoorBlock.HINGE, DoorHinge.LEFT)
                                        .set(DoorBlock.OPEN, true),
                                VariantsBuilder.of(topHingeId).y(180)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(DoorBlock.FACING, Direction.WEST)
                                        .set(DoorBlock.HALF, Half.UPPER)
                                        .set(DoorBlock.HINGE, DoorHinge.LEFT)
                                        .set(DoorBlock.OPEN, true),
                                VariantsBuilder.of(topHingeId).y(270)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(DoorBlock.FACING, Direction.NORTH)
                                        .set(DoorBlock.HALF, Half.UPPER)
                                        .set(DoorBlock.HINGE, DoorHinge.LEFT)
                                        .set(DoorBlock.OPEN, true),
                                VariantsBuilder.of(topHingeId)
                        )
                        // Opened top right
                        .variant(
                                BlockModelDefinitionBuilder.predicate(DoorBlock.FACING, Direction.EAST)
                                        .set(DoorBlock.HALF, Half.UPPER)
                                        .set(DoorBlock.HINGE, DoorHinge.RIGHT)
                                        .set(DoorBlock.OPEN, true),
                                VariantsBuilder.of(topId).y(270)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(DoorBlock.FACING, Direction.SOUTH)
                                        .set(DoorBlock.HALF, Half.UPPER)
                                        .set(DoorBlock.HINGE, DoorHinge.RIGHT)
                                        .set(DoorBlock.OPEN, true),
                                VariantsBuilder.of(topId)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(DoorBlock.FACING, Direction.WEST)
                                        .set(DoorBlock.HALF, Half.UPPER)
                                        .set(DoorBlock.HINGE, DoorHinge.RIGHT)
                                        .set(DoorBlock.OPEN, true),
                                VariantsBuilder.of(topId).y(90)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(DoorBlock.FACING, Direction.NORTH)
                                        .set(DoorBlock.HALF, Half.UPPER)
                                        .set(DoorBlock.HINGE, DoorHinge.RIGHT)
                                        .set(DoorBlock.OPEN, true),
                                VariantsBuilder.of(topId).y(180)
                        )
        );

        return this;
    }

    @Override
    public BlockModelTemplates fenceGate(Block block, NamespacedIdentifier texture) {
        NamespacedIdentifier baseId = modelGenerator.blockModel(block, BlockModelTemplates.fenceGate(texture));
        NamespacedIdentifier openedId = modelGenerator.blockModel(block, "_open", BlockModelTemplates.fenceGateOpen(texture));
        NamespacedIdentifier wallId = modelGenerator.blockModel(block, "_wall", BlockModelTemplates.fenceGateWall(texture));
        NamespacedIdentifier wallOpenedId = modelGenerator.blockModel(block, "_wall_open", BlockModelTemplates.fenceGateWallOpen(texture));
        modelGenerator.itemModel(block, ModelBuilder.create(baseId));

        modelGenerator.blockstate(
                block,
                BlockModelDefinitionBuilder.create()
                        // Closed not in wall
                        .variant(
                                BlockModelDefinitionBuilder.predicate(FenceGateBlock.FACING, Direction.SOUTH)
                                        .set(FenceGateBlock.IN_WALL, false)
                                        .set(FenceGateBlock.OPEN, false),
                                VariantsBuilder.of(baseId).lockedUV(true)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(FenceGateBlock.FACING, Direction.WEST)
                                        .set(FenceGateBlock.IN_WALL, false)
                                        .set(FenceGateBlock.OPEN, false),
                                VariantsBuilder.of(baseId).lockedUV(true).y(90)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(FenceGateBlock.FACING, Direction.NORTH)
                                        .set(FenceGateBlock.IN_WALL, false)
                                        .set(FenceGateBlock.OPEN, false),
                                VariantsBuilder.of(baseId).lockedUV(true).y(180)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(FenceGateBlock.FACING, Direction.EAST)
                                        .set(FenceGateBlock.IN_WALL, false)
                                        .set(FenceGateBlock.OPEN, false),
                                VariantsBuilder.of(baseId).lockedUV(true).y(270)
                        )
                        // Opened not in wall
                        .variant(
                                BlockModelDefinitionBuilder.predicate(FenceGateBlock.FACING, Direction.SOUTH)
                                        .set(FenceGateBlock.IN_WALL, false)
                                        .set(FenceGateBlock.OPEN, true),
                                VariantsBuilder.of(openedId).lockedUV(true)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(FenceGateBlock.FACING, Direction.WEST)
                                        .set(FenceGateBlock.IN_WALL, false)
                                        .set(FenceGateBlock.OPEN, true),
                                VariantsBuilder.of(openedId).lockedUV(true).y(90)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(FenceGateBlock.FACING, Direction.NORTH)
                                        .set(FenceGateBlock.IN_WALL, false)
                                        .set(FenceGateBlock.OPEN, true),
                                VariantsBuilder.of(openedId).lockedUV(true).y(180)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(FenceGateBlock.FACING, Direction.EAST)
                                        .set(FenceGateBlock.IN_WALL, false)
                                        .set(FenceGateBlock.OPEN, true),
                                VariantsBuilder.of(openedId).lockedUV(true).y(270)
                        )
                        // Closed in wall
                        .variant(
                                BlockModelDefinitionBuilder.predicate(FenceGateBlock.FACING, Direction.SOUTH)
                                        .set(FenceGateBlock.IN_WALL, true)
                                        .set(FenceGateBlock.OPEN, false),
                                VariantsBuilder.of(wallId).lockedUV(true)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(FenceGateBlock.FACING, Direction.WEST)
                                        .set(FenceGateBlock.IN_WALL, true)
                                        .set(FenceGateBlock.OPEN, false),
                                VariantsBuilder.of(wallId).lockedUV(true).y(90)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(FenceGateBlock.FACING, Direction.NORTH)
                                        .set(FenceGateBlock.IN_WALL, true)
                                        .set(FenceGateBlock.OPEN, false),
                                VariantsBuilder.of(wallId).lockedUV(true).y(180)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(FenceGateBlock.FACING, Direction.EAST)
                                        .set(FenceGateBlock.IN_WALL, true)
                                        .set(FenceGateBlock.OPEN, false),
                                VariantsBuilder.of(wallId).lockedUV(true).y(270)
                        )
                        // Opened in wall
                        .variant(
                                BlockModelDefinitionBuilder.predicate(FenceGateBlock.FACING, Direction.SOUTH)
                                        .set(FenceGateBlock.IN_WALL, true)
                                        .set(FenceGateBlock.OPEN, true),
                                VariantsBuilder.of(wallOpenedId).lockedUV(true)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(FenceGateBlock.FACING, Direction.WEST)
                                        .set(FenceGateBlock.IN_WALL, true)
                                        .set(FenceGateBlock.OPEN, true),
                                VariantsBuilder.of(wallOpenedId).lockedUV(true).y(90)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(FenceGateBlock.FACING, Direction.NORTH)
                                        .set(FenceGateBlock.IN_WALL, true)
                                        .set(FenceGateBlock.OPEN, true),
                                VariantsBuilder.of(wallOpenedId).lockedUV(true).y(180)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(FenceGateBlock.FACING, Direction.EAST)
                                        .set(FenceGateBlock.IN_WALL, true)
                                        .set(FenceGateBlock.OPEN, true),
                                VariantsBuilder.of(wallOpenedId).lockedUV(true).y(270)
                        )
        );

        return this;
    }

    @Override
    public BlockModelTemplates leaves(Block block, NamespacedIdentifier texture) {
        NamespacedIdentifier modelId = modelGenerator.blockModel(block, BlockModelTemplates.leaves(texture));
        modelGenerator.itemModel(block, ModelBuilder.create(modelId));
        modelGenerator.blockstate(block, BlockModelDefinitionBuilder.createSimple(modelId));

        return this;
    }

    @Override
    public BlockModelTemplates pressurePlate(Block block, NamespacedIdentifier texture) {
        NamespacedIdentifier baseId = modelGenerator.blockModel(block, BlockModelTemplates.pressurePlateUp(texture));
        NamespacedIdentifier downId = modelGenerator.blockModel(block, "_down", BlockModelTemplates.pressurePlateDown(texture));
        modelGenerator.itemModel(block, ModelBuilder.create(baseId));

        modelGenerator.blockstate(
                block,
                BlockModelDefinitionBuilder.create()
                        .variant(
                                BlockModelDefinitionBuilder.predicate(PressurePlateBlock.POWERED, false),
                                VariantsBuilder.of(baseId)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(PressurePlateBlock.POWERED, true),
                                VariantsBuilder.of(downId)
                        )
        );

        return this;
    }

    @Override
    public BlockModelTemplates trapdoor(Block block, NamespacedIdentifier texture) {
        NamespacedIdentifier bottomId = modelGenerator.blockModel(block, "_bottom", BlockModelTemplates.trapdoorBottom(texture));
        NamespacedIdentifier topId = modelGenerator.blockModel(block, "_top", BlockModelTemplates.trapdoorTop(texture));
        NamespacedIdentifier openedId = modelGenerator.blockModel(block, "_open", BlockModelTemplates.trapdoorOpen(texture));
        modelGenerator.itemModel(block, ModelBuilder.create(bottomId));

        modelGenerator.blockstate(
                block,
                BlockModelDefinitionBuilder.create()
                        // Closed bottom
                        .variant(
                                BlockModelDefinitionBuilder.predicate(TrapdoorBlock.FACING, Direction.NORTH)
                                        .set(TrapdoorBlock.HALF, StairHalf.BOTTOM)
                                        .set(TrapdoorBlock.OPEN, false),
                                VariantsBuilder.of(bottomId)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(TrapdoorBlock.FACING, Direction.SOUTH)
                                        .set(TrapdoorBlock.HALF, StairHalf.BOTTOM)
                                        .set(TrapdoorBlock.OPEN, false),
                                VariantsBuilder.of(bottomId).y(180)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(TrapdoorBlock.FACING, Direction.EAST)
                                        .set(TrapdoorBlock.HALF, StairHalf.BOTTOM)
                                        .set(TrapdoorBlock.OPEN, false),
                                VariantsBuilder.of(bottomId).y(90)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(TrapdoorBlock.FACING, Direction.WEST)
                                        .set(TrapdoorBlock.HALF, StairHalf.BOTTOM)
                                        .set(TrapdoorBlock.OPEN, false),
                                VariantsBuilder.of(bottomId).y(270)
                        )
                        // Closed top
                        .variant(
                                BlockModelDefinitionBuilder.predicate(TrapdoorBlock.FACING, Direction.NORTH)
                                        .set(TrapdoorBlock.HALF, StairHalf.TOP)
                                        .set(TrapdoorBlock.OPEN, false),
                                VariantsBuilder.of(topId)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(TrapdoorBlock.FACING, Direction.SOUTH)
                                        .set(TrapdoorBlock.HALF, StairHalf.TOP)
                                        .set(TrapdoorBlock.OPEN, false),
                                VariantsBuilder.of(topId).y(180)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(TrapdoorBlock.FACING, Direction.EAST)
                                        .set(TrapdoorBlock.HALF, StairHalf.TOP)
                                        .set(TrapdoorBlock.OPEN, false),
                                VariantsBuilder.of(topId).y(90)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(TrapdoorBlock.FACING, Direction.WEST)
                                        .set(TrapdoorBlock.HALF, StairHalf.TOP)
                                        .set(TrapdoorBlock.OPEN, false),
                                VariantsBuilder.of(topId).y(270)
                        )

                        // Opened bottom
                        .variant(
                                BlockModelDefinitionBuilder.predicate(TrapdoorBlock.FACING, Direction.NORTH)
                                        .set(TrapdoorBlock.HALF, StairHalf.BOTTOM)
                                        .set(TrapdoorBlock.OPEN, true),
                                VariantsBuilder.of(openedId)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(TrapdoorBlock.FACING, Direction.SOUTH)
                                        .set(TrapdoorBlock.HALF, StairHalf.BOTTOM)
                                        .set(TrapdoorBlock.OPEN, true),
                                VariantsBuilder.of(openedId).y(180)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(TrapdoorBlock.FACING, Direction.EAST)
                                        .set(TrapdoorBlock.HALF, StairHalf.BOTTOM)
                                        .set(TrapdoorBlock.OPEN, true),
                                VariantsBuilder.of(openedId).y(90)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(TrapdoorBlock.FACING, Direction.WEST)
                                        .set(TrapdoorBlock.HALF, StairHalf.BOTTOM)
                                        .set(TrapdoorBlock.OPEN, true),
                                VariantsBuilder.of(openedId).y(270)
                        )
                        // Opened top
                        .variant(
                                BlockModelDefinitionBuilder.predicate(TrapdoorBlock.FACING, Direction.NORTH)
                                        .set(TrapdoorBlock.HALF, StairHalf.TOP)
                                        .set(TrapdoorBlock.OPEN, true),
                                VariantsBuilder.of(openedId).x(180).y(180)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(TrapdoorBlock.FACING, Direction.SOUTH)
                                        .set(TrapdoorBlock.HALF, StairHalf.TOP)
                                        .set(TrapdoorBlock.OPEN, true),
                                VariantsBuilder.of(openedId).x(180).y(0)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(TrapdoorBlock.FACING, Direction.EAST)
                                        .set(TrapdoorBlock.HALF, StairHalf.TOP)
                                        .set(TrapdoorBlock.OPEN, true),
                                VariantsBuilder.of(openedId).x(180).y(270)
                        )
                        .variant(
                                BlockModelDefinitionBuilder.predicate(TrapdoorBlock.FACING, Direction.WEST)
                                        .set(TrapdoorBlock.HALF, StairHalf.TOP)
                                        .set(TrapdoorBlock.OPEN, true),
                                VariantsBuilder.of(openedId).x(180).y(90)
                        )
        );

        return this;
    }
}
