package net.ornithemc.osl.datagen.api.model.item;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.datagen.api.model.ModelBuilder;

/**
 * A collection of item model templates.
 */
public final class ItemModelTemplates {
    /**
     * Creates a basic item model with the given texture.
     *
     * @param texture the texture
     * @return the model builder
     */
    public static ModelBuilder basic(NamespacedIdentifier texture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("item/generated")).texture("layer0", texture);
    }

    /**
     * Creates a basic item model with the given textures.
     *
     * @param firstLayer the first texture
     * @param secondLayer the second texture
     * @return the model builder
     */
    public static ModelBuilder basic(NamespacedIdentifier firstLayer, NamespacedIdentifier secondLayer) {
        return ModelBuilder.create(NamespacedIdentifiers.from("item/generated")).texture("layer0", firstLayer).texture("layer1", secondLayer);
    }
}
