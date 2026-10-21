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
     * Creates a wall block model, definition, and item model with the given texture.
     *
     * @param block the block
     * @param texture the texture
     * @return the model builder
     */
    BlockModelTemplates wall(Block block, NamespacedIdentifier texture);

    /**
     * Creates a fence block model, definition, and item model with the given texture.
     *
     * @param block the block
     * @param texture the texture
     * @return the model builder
     */
    BlockModelTemplates fence(Block block, NamespacedIdentifier texture);

    /**
     * Creates a button block model, definition, and item model with the given texture.
     *
     * @param block the block
     * @param texture the texture
     * @return the model builder
     */
    BlockModelTemplates button(Block block, NamespacedIdentifier texture);

    /**
     * Creates a door block model, definition, and item model with the given textures.
     *
     * @param block the block
     * @param inventoryTexture the inventory texture
     * @param topTexture the top texture
     * @param bottomTexture the bottom texture
     * @return the model builder
     */
    BlockModelTemplates door(Block block, NamespacedIdentifier inventoryTexture, NamespacedIdentifier topTexture, NamespacedIdentifier bottomTexture);

    /**
     * Creates a fence gate block model, definition, and item model with the given texture.
     *
     * @param block the block
     * @param texture the texture
     * @return the model builder
     */
    BlockModelTemplates fenceGate(Block block, NamespacedIdentifier texture);

    /**
     * Creates a leaves block model, definition, and item model with the given texture.
     *
     * @param block the block
     * @param texture the texture
     * @return the model builder
     */
    BlockModelTemplates leaves(Block block, NamespacedIdentifier texture);

    /**
     * Creates a pressure plate block model, definition, and item model with the given texture.
     *
     * @param block the block
     * @param texture the texture
     * @return the model builder
     */
    BlockModelTemplates pressurePlate(Block block, NamespacedIdentifier texture);

    /**
     * Creates a trapdoor block model, definition, and item model with the given texture.
     *
     * @param block the block
     * @param texture the texture
     * @return the model builder
     */
    BlockModelTemplates trapdoor(Block block, NamespacedIdentifier texture);

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

    /**
     * Creates a wall inventory block model with the given texture.
     *
     * @param texture the texture
     * @return the model builder
     */
    static ModelBuilder wallInventory(NamespacedIdentifier texture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/wall_inventory")).texture("wall", texture);
    }

    /**
     * Creates a wall post block model with the given texture.
     *
     * @param texture the texture
     * @return the model builder
     */
    static ModelBuilder wallPost(NamespacedIdentifier texture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/template_wall_post")).texture("wall", texture);
    }

    /**
     * Creates a wall side block model with the given texture.
     *
     * @param texture the texture
     * @return the model builder
     */
    static ModelBuilder wallSide(NamespacedIdentifier texture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/template_wall_side")).texture("wall", texture);
    }

    /**
     * Creates a fence inventory block model with the given texture.
     *
     * @param texture the texture
     * @return the model builder
     */
    static ModelBuilder fenceInventory(NamespacedIdentifier texture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/fence_inventory")).texture("texture", texture);
    }

    /**
     * Creates a fence post, block model with the given texture.
     *
     * @param texture the texture
     * @return the model builder
     */
    static ModelBuilder fencePost(NamespacedIdentifier texture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/fence_post")).texture("texture", texture);
    }

    /**
     * Creates a fence side block model with the given texture.
     *
     * @param texture the texture
     * @return the model builder
     */
    static ModelBuilder fenceSide(NamespacedIdentifier texture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/fence_side")).texture("texture", texture);
    }

    /**
     * Creates a button block model with the given texture.
     *
     * @param texture the texture
     * @return the model builder
     */
    static ModelBuilder button(NamespacedIdentifier texture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/button")).texture("texture", texture);
    }

    /**
     * Creates a button inventory block model with the given texture.
     *
     * @param texture the texture
     * @return the model builder
     */
    static ModelBuilder buttonInventory(NamespacedIdentifier texture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/button_inventory")).texture("texture", texture);
    }

    /**
     * Creates a button pressed, block model with the given texture.
     *
     * @param texture the texture
     * @return the model builder
     */
    static ModelBuilder buttonPressed(NamespacedIdentifier texture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/button_pressed")).texture("texture", texture);
    }

    /**
     * Creates a door bottom block model with the given top and bottom textures.
     *
     * @param topTexture  the top texture
     * @param bottomTexture the bottom texture
     * @return the model builder
     */
    static ModelBuilder doorBottom(NamespacedIdentifier topTexture, NamespacedIdentifier bottomTexture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/door_bottom")).texture("top", topTexture).texture("bottom", bottomTexture);
    }

    /**
     * Creates a door bottom block model with the given top and bottom textures, with the hinge on the right.
     *
     * @param topTexture  the top texture
     * @param bottomTexture the bottom texture
     * @return the model builder
     */
    static ModelBuilder doorBottomHinge(NamespacedIdentifier topTexture, NamespacedIdentifier bottomTexture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/door_bottom_rh")).texture("top", topTexture).texture("bottom", bottomTexture);
    }

    /**
     * Creates a door top block model with the given top and bottom textures.
     *
     * @param topTexture  the top texture
     * @param bottomTexture the bottom texture
     * @return the model builder
     */
    static ModelBuilder doorTop(NamespacedIdentifier topTexture, NamespacedIdentifier bottomTexture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/door_top")).texture("top", topTexture).texture("bottom", bottomTexture);
    }

    /**
     * Creates a door top block model with the given top and bottom textures, with the hinge on the right.
     *
     * @param topTexture  the top texture
     * @param bottomTexture the bottom texture
     * @return the model builder
     */
    static ModelBuilder doorTopHinge(NamespacedIdentifier topTexture, NamespacedIdentifier bottomTexture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/door_top_rh")).texture("top", topTexture).texture("bottom", bottomTexture);
    }

    /**
     * Creates a fence gate block model with the given texture.
     *
     * @param texture the texture
     * @return the model builder
     */
    static ModelBuilder fenceGate(NamespacedIdentifier texture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/template_fence_gate")).texture("texture", texture);
    }

    /**
     * Creates a fence gate block model with the given texture, with the gate open.
     *
     * @param texture the texture
     * @return the model builder
     */
    static ModelBuilder fenceGateOpen(NamespacedIdentifier texture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/template_fence_gate_open")).texture("texture", texture);
    }

    /**
     * Creates a fence gate block model with the given texture, with the gate open and on a wall.
     *
     * @param texture the texture
     * @return the model builder
     */
    static ModelBuilder fenceGateWall(NamespacedIdentifier texture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/template_fence_gate_wall")).texture("texture", texture);
    }

    /**
     * Creates a fence gate block model with the given texture, with the gate open and on a wall.
     *
     * @param texture the texture
     * @return the model builder
     */
    static ModelBuilder fenceGateWallOpen(NamespacedIdentifier texture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/template_fence_gate_wall_open")).texture("texture", texture);
    }

    /**
     * Creates a leaves block model with the given texture.
     *
     * @param texture the texture
     * @return the model builder
     */
    static ModelBuilder leaves(NamespacedIdentifier texture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/leaves")).texture("all", texture);
    }

    /**
     * Creates a pressure plate block model with the given texture, with the plate up.
     *
     * @param texture the texture
     * @return the model builder
     */
    static ModelBuilder pressurePlateUp(NamespacedIdentifier texture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/pressure_plate_up")).texture("texture", texture);
    }

    /**
     * Creates a pressure plate block model with the given texture, with the plate down.
     *
     * @param texture the texture
     * @return the model builder
     */
    static ModelBuilder pressurePlateDown(NamespacedIdentifier texture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/pressure_plate_down")).texture("texture", texture);
    }

    /**
     * Creates a closed trapdoor block model with the given texture, with the trapdoor at the bottom.
     *
     * @param texture the texture
     * @return the model builder
     */
    static ModelBuilder trapdoorBottom(NamespacedIdentifier texture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/template_orientable_trapdoor_bottom")).texture("texture", texture);
    }

    /**
     * Creates a trapdoor block model with the given texture, with the trapdoor open.
     *
     * @param texture the texture
     * @return the model builder
     */
    static ModelBuilder trapdoorOpen(NamespacedIdentifier texture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/template_orientable_trapdoor_open")).texture("texture", texture);
    }

    /**
     * Creates a closed trapdoor block model with the given texture, with the trapdoor at the top.
     *
     * @param texture the texture
     * @return the model builder
     */
    static ModelBuilder trapdoorTop(NamespacedIdentifier texture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("block/template_orientable_trapdoor_top")).texture("texture", texture);
    }
}
