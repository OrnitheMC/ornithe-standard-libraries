package net.ornithemc.osl.datagen.api;

public interface DataGeneratorInitializer {
    String KEY = "datagen";

    void onDatagenInit(ModDataGenerator dataGenerator);
}
