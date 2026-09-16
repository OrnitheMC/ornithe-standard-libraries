package net.ornithemc.osl.datagen.api;

import net.minecraft.data.DataGenerator;
import net.ornithemc.osl.datagen.impl.DataGenHelperImpl;

public interface DataGeneratorInitializer {
    String KEY = DataGenHelperImpl.ENTRYPOINT_KEY;

    void onDatagenInit(ModDataGenerator<DataGenerator> dataGenerator);
}
