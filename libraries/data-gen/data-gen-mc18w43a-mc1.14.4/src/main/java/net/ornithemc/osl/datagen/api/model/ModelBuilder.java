package net.ornithemc.osl.datagen.api.model;

import com.google.gson.JsonObject;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

import java.util.HashMap;
import java.util.Map;

public class ModelBuilder {
    private NamespacedIdentifier parent;
    private Map<String, NamespacedIdentifier> textures = new HashMap<>();

    public static ModelBuilder create() {
        return new ModelBuilder();
    }

    public static ModelBuilder create(NamespacedIdentifier parent) {
        return new ModelBuilder().parent(parent);
    }

    public ModelBuilder parent(NamespacedIdentifier parent) {
        this.parent = parent;
        return this;
    }

    public ModelBuilder texture(String name, NamespacedIdentifier texture) {
        this.textures.put(name, texture);
        return this;
    }

    public JsonObject build() {
        JsonObject model = new JsonObject();

        if (parent != null) {
            model.addProperty("parent", parent.toString());
        }

        if (!textures.isEmpty()) {
            JsonObject texturesJson = new JsonObject();
            for (Map.Entry<String, NamespacedIdentifier> entry : textures.entrySet()) {
                texturesJson.addProperty(entry.getKey(), entry.getValue().toString());
            }
            model.add("textures", texturesJson);
        }

        return model;
    }
}
