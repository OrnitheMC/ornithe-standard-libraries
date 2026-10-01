package net.ornithemc.osl.datagen.api.model.item;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.datagen.api.model.ModelBuilder;

public final class ItemModelTemplates {
    public static ModelBuilder basic(NamespacedIdentifier texture) {
        return ModelBuilder.create(NamespacedIdentifiers.from("item/generated")).texture("layer0", texture);
    }

    public static ModelBuilder basic(NamespacedIdentifier firstLayer, NamespacedIdentifier secondLayer) {
        return ModelBuilder.create(NamespacedIdentifiers.from("item/generated")).texture("layer0", firstLayer).texture("layer1", secondLayer);
    }
}
