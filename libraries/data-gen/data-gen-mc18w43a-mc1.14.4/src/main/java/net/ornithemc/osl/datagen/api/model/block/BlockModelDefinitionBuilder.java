package net.ornithemc.osl.datagen.api.model.block;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.block.state.BlockState;
import net.minecraft.client.render.block.BlockModelShaper;
import net.minecraft.state.property.Property;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

import java.util.*;

public class BlockModelDefinitionBuilder {
    private final Map<String, VariantsBuilder> variants = new HashMap<>();
    private final List<SelectorBuilder> selectors = new ArrayList<>();

    private BlockModelDefinitionBuilder() {
    }

    public static BlockModelDefinitionBuilder create() {
        return new BlockModelDefinitionBuilder();
    }

    public static BlockModelDefinitionBuilder createSimple(NamespacedIdentifier modelLocation) {
        return new BlockModelDefinitionBuilder().variant("", VariantsBuilder.of(modelLocation));
    }

    public BlockModelDefinitionBuilder variant(BlockState state, VariantsBuilder variants) {
        this.variants.put(BlockModelShaper.propertiesToString(state.values()), variants);
        return this;
    }

    public BlockModelDefinitionBuilder variant(BlockState state, NamespacedIdentifier modelLocation) {
        this.variants.put(BlockModelShaper.propertiesToString(state.values()), VariantsBuilder.of(modelLocation));
        return this;
    }

    public BlockModelDefinitionBuilder variant(PredicateBuilder predicateBuilder, VariantsBuilder variants) {
        this.variants.put(predicateBuilder.build(), variants);
        return this;
    }

    public BlockModelDefinitionBuilder variant(PredicateBuilder predicateBuilder, NamespacedIdentifier modelLocation) {
        this.variants.put(predicateBuilder.build(), VariantsBuilder.of(modelLocation));
        return this;
    }

    private BlockModelDefinitionBuilder variant(String variantName, VariantsBuilder variants) {
        this.variants.put(variantName, variants);
        return this;
    }

    public BlockModelDefinitionBuilder selector(VariantsBuilder variants) {
        this.selectors.add(new SelectorBuilder(null, variants));
        return this;
    }

    public BlockModelDefinitionBuilder selector(NamespacedIdentifier modelLocation) {
        this.selectors.add(new SelectorBuilder(null, VariantsBuilder.of(modelLocation)));
        return this;
    }

    public BlockModelDefinitionBuilder selector(ConditionsBuilder conditions, VariantsBuilder variants) {
        this.selectors.add(new SelectorBuilder(conditions, variants));
        return this;
    }

    public BlockModelDefinitionBuilder selector(ConditionsBuilder conditions, NamespacedIdentifier modelLocation) {
        this.selectors.add(new SelectorBuilder(conditions, VariantsBuilder.of(modelLocation)));
        return this;
    }

    public JsonObject build() {
        JsonObject json = new JsonObject();

        if (!variants.isEmpty()) {
            JsonObject variantsJson = new JsonObject();

            for (Map.Entry<String, VariantsBuilder> entry : variants.entrySet()) {
                variantsJson.add(entry.getKey(), entry.getValue().build());
            }

            json.add("variants", variantsJson);
        }

        if (!selectors.isEmpty()) {
            JsonArray multiPart = new JsonArray();

            for (SelectorBuilder entry : selectors) {
                multiPart.add(entry.build());
            }

            json.add("multipart", multiPart);
        }

        if (json.keySet().isEmpty()) {
            throw new IllegalStateException("Block state model definition is empty");
        }

        return json;
    }

    static class SelectorBuilder {
        private final ConditionsBuilder conditions;
        private final VariantsBuilder variants;

        SelectorBuilder(ConditionsBuilder conditions, VariantsBuilder variants) {
            this.conditions = conditions;
            this.variants = variants;
        }

        private JsonObject build() {
            JsonObject json = new JsonObject();

            if (conditions != null) json.add("when", conditions.build());
            json.add("apply", variants.build());

            return json;
        }
    }

    public static <T extends Comparable<T>> PredicateBuilder predicate(Property<T> property, T value) {
        return new PredicateBuilder().set(property, value);
    }

    public static class PredicateBuilder {
        private final Map<Property<?>, Comparable<?>> predicates = new HashMap<>();

        public <T extends Comparable<T>> PredicateBuilder set(Property<T> property, T value) {
            predicates.put(property, value);
            return this;
        }

        private String build() {
            return BlockModelShaper.propertiesToString(predicates);
        }
    }
}
