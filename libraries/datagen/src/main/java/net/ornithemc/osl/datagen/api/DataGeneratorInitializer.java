package net.ornithemc.osl.datagen.api;

/**
 * An initializer for data generators.
 */
public interface DataGeneratorInitializer {
    String KEY = "datagen";

    void onDatagenInit(ModDataGenerator dataGenerator);
}
