package net.ornithemc.osl.datagen.impl.provider;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePackMetadata;
import net.ornithemc.osl.resource.loader.impl.resource.pack.ResourcePacks;
import net.ornithemc.osl.resource.loader.impl.resource.pack.SimpleResourcePackMetadata;
import net.ornithemc.osl.text.api.TextComponents;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Objects;

public class MCMetaProvider implements DataProvider {
    private final DataGenerator generator;
    private final String description;

    public MCMetaProvider(DataGenerator generator, String description) {
        this.generator = generator;
        this.description = description;
    }

    @Override
    public void run(HashCache cache) throws IOException {
        ResourcePackMetadata metadata = new SimpleResourcePackMetadata(ResourcePacks.getSupportedFormat(), TextComponents.literal(this.description));

        Path path = getPath();

        String json = SimpleResourcePackMetadata.SERIALIZER.serialize(metadata).toString();
        String hash = SHA1.hashUnencodedChars(json).toString();

        if (!Objects.equals(cache.get(path), hash) || !Files.exists(path, new LinkOption[0])) {
            Files.createDirectories(path.getParent());

            try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path)) {
                bufferedWriter.write(json);
            }
        }

        cache.put(path, hash);
    }

    private Path getPath() {
        return this.generator.getOutput().resolve("pack.mcmeta");
    }

    @Override
    public String getName() {
        return "MCMeta";
    }
}
