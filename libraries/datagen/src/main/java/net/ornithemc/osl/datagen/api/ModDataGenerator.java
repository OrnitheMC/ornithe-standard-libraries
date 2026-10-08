package net.ornithemc.osl.datagen.api;

import net.fabricmc.loader.api.ModContainer;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

/**
 * A data generator for a mod.
 */
public interface ModDataGenerator {
    /**
     * Creates a pack generator for the mod.
     */
    PackGenerator createPack();
    /**
     * Creates a bundled resource pack generator for the mod.
     */
    PackGenerator createBundledResourcePack(NamespacedIdentifier id);
    /**
     * Returns the mod container assigned to this ModDataGenerator.
     */
    ModContainer getMod();
}
