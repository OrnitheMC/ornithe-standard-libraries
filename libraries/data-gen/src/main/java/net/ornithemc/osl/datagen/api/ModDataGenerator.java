package net.ornithemc.osl.datagen.api;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

public interface ModDataGenerator<T> {
    T createPack();
    T createBuiltinResourcePack(NamespacedIdentifier id);
}
