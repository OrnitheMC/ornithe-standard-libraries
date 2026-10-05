package net.ornithemc.osl.datagen.api.model.block;

import net.minecraft.block.Block;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.datagen.api.model.ModelBuilder;

/**
 * A collection of block model and definition templates.
 */
public interface BlockModelTemplates {
    /**
     * Creates a simple block model, definition, and item model with the given texture.
     *
     * @param block the block
     * @param texture the texture
     */
    BlockModelTemplates simpleBlock(Block block, NamespacedIdentifier texture);

    /**
     * Creates a simple block model, definition, and item model with the given textures.
     *
     * @param block the block
     * @param firstLayer the first texture
     * @param secondLayer the second texture
     */
    BlockModelTemplates simpleBlock(Block block, NamespacedIdentifier firstLayer, NamespacedIdentifier secondLayer);

    /**
     * Creates a simple block model, definition, without an item model with the given texture.
     *
     * @param block the block
     * @param texture the texture
     */
    BlockModelTemplates simpleBlockWithoutItem(Block block, NamespacedIdentifier texture);

    /**
     * Creates a slab block model, definition, and item model with the given textures.
     *
     * @param block the block
     * @param doubleModelLocation the double slab model location
     * @param sideTexture the side texture
     * @param topTexture the top texture
     * @param bottomTexture the bottom texture
     */
    BlockModelTemplates slab(Block block, NamespacedIdentifier doubleModelLocation, NamespacedIdentifier sideTexture, NamespacedIdentifier topTexture, NamespacedIdentifier bottomTexture);

    /**
     * Creates a slab block model, definition, and item model with the given texture.
     *
     * @param block the block
     * @param doubleModelLocation the double slab model location
     * @param texture the texture
     */
    BlockModelTemplates simpleSlab(Block block, NamespacedIdentifier doubleModelLocation, NamespacedIdentifier texture);

    /**
     * Creates a stairs block model, definition, and item model with the given textures.
     *
     * @param block the block
     * @param sideTexture the side texture
     * @param topTexture the top texture
     * @param bottomTexture the bottom texture
     */
    BlockModelTemplates stairs(Block block, NamespacedIdentifier sideTexture, NamespacedIdentifier topTexture, NamespacedIdentifier bottomTexture);

    /**
     * Creates a stairs block model, definition, and item model with the given texture.
     *
     * @param block the block
     * @param texture the texture
     */
    BlockModelTemplates simpleStairs(Block block, NamespacedIdentifier texture);

    /**
     * Creates a cube block model with the given textures.
     *
     * @param downTexture the bottom texture
     * @param upTexture the top texture
     * @param northTexture the north texture
     * @param southTexture the south texture
     * @param westTexture the west texture
     * @param eastTexture the east texture
     * @return the model builder
     */
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

    /**
     * Creates a cube block model with the given texture for all sides.
     *
     * @param texture the texture
     * @return the model builder
     */
    static ModelBuilder cubeAll(NamespacedIdentifier texture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/cube_all")).texture("all", texture);
    }

    /**
     * Creates a cube block model with the given textures.
     *
     * @param sideTexture the side texture
     * @param topTexture the top texture
     * @param bottomTexture the bottom texture
     * @return the model builder
     */
    static ModelBuilder cubeBottomTop(NamespacedIdentifier sideTexture, NamespacedIdentifier topTexture, NamespacedIdentifier bottomTexture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/cube_bottom_top"))
                .texture("side", sideTexture)
                .texture("bottom", bottomTexture)
                .texture("top", topTexture);
    }

    /**
     * Creates a cube block model with the given textures for the sides and a different texture for the ends.
     *
     * @param sideTexture the side texture
     * @param endTexture the end texture
     * @return the model builder
     */
    static ModelBuilder cubeColumn(NamespacedIdentifier sideTexture, NamespacedIdentifier endTexture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/cube_column"))
                .texture("side", sideTexture)
                .texture("end", endTexture);
    }

    /**
     * Creates a cube block model with the given textures for the sides and different textures for the ends.
     *
     * @param downTexture the bottom texture
     * @param upTexture the top texture
     * @param northTexture the north texture
     * @param southTexture the south texture
     * @param westTexture the west texture
     * @param eastTexture the east texture
     * @return the model builder
     */
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

    /**
     * Creates a cube block model with the given texture for all sides mirrored.
     *
     * @param downTexture the bottom texture
     * @param upTexture the top texture
     * @param northTexture the north texture
     * @param southTexture the south texture
     * @param westTexture the west texture
     * @param eastTexture the east texture
     * @return the model builder
     */
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

    /**
     * Creates a cube block model with the given texture for all sides mirrored.
     *
     * @param texture the texture
     * @return the model builder
     */
    static ModelBuilder cubeAllMirrored(NamespacedIdentifier texture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/cube_all_mirrored")).texture("all", texture);
    }

    /**
     * Creates a cube block model with the given texture for the sides and a different texture for the top.
     *
     * @param sideTexture the side texture
     * @param topTexture the top texture
     * @return the model builder
     */
    static ModelBuilder cubeTop(NamespacedIdentifier sideTexture, NamespacedIdentifier topTexture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/cube_top"))
                .texture("side", sideTexture)
                .texture("top", topTexture);
    }

    /**
     * Creates a bottom slab block model with the given textures.
     *
     * @param sideTexture the side texture
     * @param topTexture the top texture
     * @param bottomTexture the bottom texture
     * @return the model builder
     */
    static ModelBuilder slabBottom(NamespacedIdentifier sideTexture, NamespacedIdentifier topTexture, NamespacedIdentifier bottomTexture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/slab"))
                .texture("side", sideTexture)
                .texture("top", topTexture)
                .texture("bottom", bottomTexture);
    }

    /**
     * Creates a top slab block model with the given textures.
     *
     * @param sideTexture the side texture
     * @param topTexture the top texture
     * @param bottomTexture the bottom texture
     * @return the model builder
     */
    static ModelBuilder slabTop(NamespacedIdentifier sideTexture, NamespacedIdentifier topTexture, NamespacedIdentifier bottomTexture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/slab_top"))
                .texture("side", sideTexture)
                .texture("top", topTexture)
                .texture("bottom", bottomTexture);
    }

    /**
     * Creates a cross-like block model with the given texture.
     *
     * @param texture the texture
     * @return the model builder
     */
    static ModelBuilder cross(NamespacedIdentifier texture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/cross")).texture("cross", texture);
    }

    /**
     * Creates a stairs block model with the given textures.
     *
     * @param sideTexture the side texture
     * @param topTexture the top texture
     * @param bottomTexture the bottom texture
     * @return the model builder
     */
    static ModelBuilder stairs(NamespacedIdentifier sideTexture, NamespacedIdentifier topTexture, NamespacedIdentifier bottomTexture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/stairs"))
                .texture("side", sideTexture)
                .texture("top", topTexture)
                .texture("bottom", bottomTexture);
    }

    /**
     * Creates an inner stairs block model with the given textures.
     *
     * @param sideTexture the side texture
     * @param topTexture the top texture
     * @param bottomTexture the bottom texture
     * @return the model builder
     */
    static ModelBuilder stairsInner(NamespacedIdentifier sideTexture, NamespacedIdentifier topTexture, NamespacedIdentifier bottomTexture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/inner_stairs"))
                .texture("side", sideTexture)
                .texture("top", topTexture)
                .texture("bottom", bottomTexture);
    }

    /**
     * Creates an outer stairs block model with the given textures.
     *
     * @param sideTexture the side texture
     * @param topTexture the top texture
     * @param bottomTexture the bottom texture
     * @return the model builder
     */
    static ModelBuilder stairsOuter(NamespacedIdentifier sideTexture, NamespacedIdentifier topTexture, NamespacedIdentifier bottomTexture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/outer_stairs"))
                .texture("side", sideTexture)
                .texture("top", topTexture)
                .texture("bottom", bottomTexture);
    }
}
