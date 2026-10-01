package net.ornithemc.osl.datagen.impl.provider;

import net.ornithemc.osl.datagen.api.PackCache;
import net.ornithemc.osl.datagen.api.PackGenerator;
import net.ornithemc.osl.datagen.api.provider.PackProvider;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePackMetadata;
import net.ornithemc.osl.resource.loader.impl.resource.pack.ResourcePacks;
import net.ornithemc.osl.resource.loader.impl.resource.pack.SimpleResourcePackMetadata;
import net.ornithemc.osl.text.api.TextComponents;

import java.io.IOException;
import java.nio.file.Path;

public class MCMetaProvider implements PackProvider {
    private final PackGenerator generator;
    private final String description;

    public MCMetaProvider(PackGenerator generator, String description) {
        this.generator = generator;
        this.description = description;
    }

    private Path getPath() {
        return this.generator.getOutputPath().resolve("pack.mcmeta");
    }

    @Override
    public String getProviderName() {
        return "MCMeta";
    }

    @Override
    public void provide(PackCache cache) throws IOException {
        ResourcePackMetadata metadata = new SimpleResourcePackMetadata(ResourcePacks.getSupportedFormat(), TextComponents.literal(this.description));

        Path path = getPath();

        String json = SimpleResourcePackMetadata.SERIALIZER.serialize(metadata).toString();

        cache.checkAndWrite(path, json);
    }
}
