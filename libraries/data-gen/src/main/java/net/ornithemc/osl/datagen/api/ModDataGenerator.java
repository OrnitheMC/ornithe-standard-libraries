package net.ornithemc.osl.datagen.api;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

public interface ModDataGenerator {
    PackGenerator createPack();
    PackGenerator createBuiltinResourcePack(NamespacedIdentifier id);
}
