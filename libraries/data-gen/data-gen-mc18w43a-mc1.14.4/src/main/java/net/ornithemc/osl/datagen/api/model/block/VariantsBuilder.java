package net.ornithemc.osl.datagen.api.model.block;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.resource.model.ModelRotation;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

/**
 * Represents block model definition variants.
 */
public interface VariantsBuilder {
    JsonElement build();

    /**
     * Creates a variant builder with the specified model location.
     *
     * @param modelLocation the model location
     * @return the variant builder
     */
    static VariantBuilder of(NamespacedIdentifier modelLocation) {
        return new VariantBuilder(modelLocation);
    }

    /**
     * Creates a multi-variant builder with the specified variants.
     *
     * @param variants the variants
     * @return the multi-variant builder
     */
    static VariantsBuilder multi(VariantBuilder... variants) {
        if (variants.length == 0) {
            throw new IllegalArgumentException("Must provide at least one variant");
        }

        return new MultiBuilder(variants);
    }

    class VariantBuilder implements VariantsBuilder {
        private final NamespacedIdentifier modelLocation;
        private int x = 0;
        private int y = 0;
        private boolean lockedUV = false;
        private int weight = 1;

        private VariantBuilder(NamespacedIdentifier modelLocation) {
            this.modelLocation = modelLocation;
        }

        /**
         * Sets the x rotation of the model.
         *
         * @param x the x rotation
         * @return the variant builder
         */
        public VariantBuilder x(int x) {
            ModelRotation rotation = ModelRotation.by(x, y);

            if (rotation == null) {
                throw new IllegalArgumentException("Invalid x rotation " + x);
            }

            this.x = x;
            return this;
        }

        /**
         * Sets the y rotation of the model.
         *
         * @param y the y rotation
         * @return the variant builder
         */
        public VariantBuilder y(int y) {
            ModelRotation rotation = ModelRotation.by(x, y);

            if (rotation == null) {
                throw new IllegalArgumentException("Invalid y rotation " + y);
            }

            this.y = y;
            return this;
        }

        /**
         * Sets whether the UV coordinates of the model should be locked.
         *
         * @param lockUV whether the UV coordinates should be locked
         * @return the variant builder
         */
        public VariantBuilder lockedUV(boolean lockUV) {
            this.lockedUV = lockUV;
            return this;
        }

        /**
         * Sets the weight of the variant.
         *
         * @param weight the weight
         * @return the variant builder
         */
        public VariantBuilder weight(int weight) {
            if (weight <= 0) {
                throw new IllegalArgumentException("Weight must be greater than 0");
            }

            this.weight = weight;
            return this;
        }

        public JsonElement build() {
            JsonObject json = new JsonObject();
            json.addProperty("model", modelLocation.toString());

            // Defaulted properties
            if (x != 0) {
                json.addProperty("x", x);
            }

            if (y != 0) {
                json.addProperty("y", y);
            }

            if (lockedUV) {
                json.addProperty("uvlock", true);
            }

            if (weight > 1) {
                json.addProperty("weight", weight);
            }

            return json;
        }
    }

    class MultiBuilder implements VariantsBuilder {
        private final VariantBuilder[] variants;

        private MultiBuilder(VariantBuilder... variants) {
            this.variants = variants;
        }

        @Override
        public JsonElement build() {
            JsonArray json = new JsonArray();

            for (VariantBuilder variant : variants) {
                json.add(variant.build());
            }

            return json;
        }
    }
}
