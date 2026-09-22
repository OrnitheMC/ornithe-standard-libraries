package net.ornithemc.osl.datagen.api.model.block;

import net.minecraft.block.Block;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.datagen.api.model.ModelBuilder;

public interface BlockModelTemplates {
    BlockModelTemplates simpleBlock(Block block, NamespacedIdentifier texture);
    BlockModelTemplates simpleBlockWithoutItem(Block block, NamespacedIdentifier texture);
    BlockModelTemplates slab(Block block, NamespacedIdentifier doubleModelLocation, NamespacedIdentifier sideTexture, NamespacedIdentifier topTexture, NamespacedIdentifier bottomTexture);
    BlockModelTemplates simpleSlab(Block block, NamespacedIdentifier doubleModelLocation, NamespacedIdentifier texture);
    BlockModelTemplates stairs(Block block, NamespacedIdentifier sideTexture, NamespacedIdentifier topTexture, NamespacedIdentifier bottomTexture);
    BlockModelTemplates simpleStairs(Block block, NamespacedIdentifier texture);

    static ModelBuilder cube(
            NamespacedIdentifier downTexture,
            NamespacedIdentifier upTexture,
            NamespacedIdentifier northTexture,
            NamespacedIdentifier southTexture,
            NamespacedIdentifier westTexture,
            NamespacedIdentifier eastTexture
    ) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/cube"))
                .texture("down", downTexture)
                .texture("up", upTexture)
                .texture("north", northTexture)
                .texture("south", southTexture)
                .texture("west", westTexture)
                .texture("east", eastTexture);
    }

    static ModelBuilder cubeAll(NamespacedIdentifier texture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/cube_all")).texture("all", texture);
    }

    static ModelBuilder cubeBottomTop(NamespacedIdentifier sideTexture, NamespacedIdentifier topTexture, NamespacedIdentifier bottomTexture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/cube_bottom_top"))
                .texture("side", sideTexture)
                .texture("bottom", bottomTexture)
                .texture("top", topTexture);
    }

    static ModelBuilder cubeColumn(NamespacedIdentifier sideTexture, NamespacedIdentifier endTexture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/cube_column"))
                .texture("side", sideTexture)
                .texture("end", endTexture);
    }

    static ModelBuilder cubeDirectional(
            NamespacedIdentifier downTexture,
            NamespacedIdentifier upTexture,
            NamespacedIdentifier northTexture,
            NamespacedIdentifier southTexture,
            NamespacedIdentifier westTexture,
            NamespacedIdentifier eastTexture
    ) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/cube_directional"))
                .texture("down", downTexture)
                .texture("up", upTexture)
                .texture("north", northTexture)
                .texture("south", southTexture)
                .texture("west", westTexture)
                .texture("east", eastTexture);
    }

    static ModelBuilder cubeMirrored(
            NamespacedIdentifier downTexture,
            NamespacedIdentifier upTexture,
            NamespacedIdentifier northTexture,
            NamespacedIdentifier southTexture,
            NamespacedIdentifier westTexture,
            NamespacedIdentifier eastTexture
    ) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/cube_mirrored"))
                .texture("down", downTexture)
                .texture("up", upTexture)
                .texture("north", northTexture)
                .texture("south", southTexture)
                .texture("west", westTexture)
                .texture("east", eastTexture);
    }

    static ModelBuilder cubeAllMirrored(NamespacedIdentifier texture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/cube_all_mirrored")).texture("all", texture);
    }

    static ModelBuilder cubeTop(NamespacedIdentifier sideTexture, NamespacedIdentifier topTexture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/cube_top"))
                .texture("side", sideTexture)
                .texture("top", topTexture);
    }

    static ModelBuilder slabBottom(NamespacedIdentifier sideTexture, NamespacedIdentifier topTexture, NamespacedIdentifier bottomTexture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/slab"))
                .texture("side", sideTexture)
                .texture("top", topTexture)
                .texture("bottom", bottomTexture);
    }

    static ModelBuilder slabTop(NamespacedIdentifier sideTexture, NamespacedIdentifier topTexture, NamespacedIdentifier bottomTexture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/slab_top"))
                .texture("side", sideTexture)
                .texture("top", topTexture)
                .texture("bottom", bottomTexture);
    }

    static ModelBuilder cross(NamespacedIdentifier texture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/cross")).texture("cross", texture);
    }

    static ModelBuilder stairs(NamespacedIdentifier sideTexture, NamespacedIdentifier topTexture, NamespacedIdentifier bottomTexture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/stairs"))
                .texture("side", sideTexture)
                .texture("top", topTexture)
                .texture("bottom", bottomTexture);
    }

    static ModelBuilder stairsInner(NamespacedIdentifier sideTexture, NamespacedIdentifier topTexture, NamespacedIdentifier bottomTexture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/inner_stairs"))
                .texture("side", sideTexture)
                .texture("top", topTexture)
                .texture("bottom", bottomTexture);
    }

    static ModelBuilder stairsOuter(NamespacedIdentifier sideTexture, NamespacedIdentifier topTexture, NamespacedIdentifier bottomTexture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/outer_stairs"))
                .texture("side", sideTexture)
                .texture("top", topTexture)
                .texture("bottom", bottomTexture);
    }
}
