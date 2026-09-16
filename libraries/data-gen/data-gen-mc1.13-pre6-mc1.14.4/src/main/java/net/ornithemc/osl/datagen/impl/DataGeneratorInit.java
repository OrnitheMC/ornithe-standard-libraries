package net.ornithemc.osl.datagen.impl;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.ornithemc.osl.datagen.api.DataGeneratorInitializer;
import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.registries.api.RegistryEvents;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DataGeneratorInit implements ModInitializer {
    @Override
    public void init() {
        if (!DataGenHelperImpl.ENABLED) return;

        RegistryEvents.REGISTRIES_FROZEN.register(() -> {
            Path basePath = DataGenHelperImpl.getOutputDir();

            List<EntrypointContainer<DataGeneratorInitializer>> entrypoints = FabricLoader.getInstance()
                    .getEntrypointContainers(DataGeneratorInitializer.KEY, DataGeneratorInitializer.class);

            List<ModDataGeneratorImpl> generators = new ArrayList<>();

            for (EntrypointContainer<DataGeneratorInitializer> entrypoint : entrypoints) {
                final String modId = entrypoint.getProvider().getMetadata().getId();

                if (DataGenHelperImpl.MOD_ID_FILTER != null && !modId.equals(DataGenHelperImpl.MOD_ID_FILTER)) {
                    continue;
                }

                ModDataGeneratorImpl generator = new ModDataGeneratorImpl(basePath, entrypoint.getProvider());
                generators.add(generator);

                DataGeneratorInitializer initializer = entrypoint.getEntrypoint();
                initializer.onDatagenInit(generator);
            }

            for (ModDataGeneratorImpl generator : generators) {
                generator.run();
            }
        });
    }
}
