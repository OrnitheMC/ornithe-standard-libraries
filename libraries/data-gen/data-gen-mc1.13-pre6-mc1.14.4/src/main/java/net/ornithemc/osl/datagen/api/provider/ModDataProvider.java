package net.ornithemc.osl.datagen.api.provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.BiConsumer;

public abstract class ModDataProvider implements DataProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    public final DataGenerator generator;
    public final ModContainer mod;

    protected ModDataProvider(DataGenerator generator, ModContainer mod) {
        this.generator = generator;
        this.mod = mod;
    }

    @Override
    public void run(HashCache cache) throws IOException {
        generate((id, json) -> {
            Path path = getPath(id);

            try {
                String string = GSON.toJson(json);
                String string2 = SHA1.hashUnencodedChars(string).toString();
                if (!Objects.equals(cache.get(path), string2) || !Files.exists(path)) {
                    Files.createDirectories(path.getParent());

                    try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path)) {
                        bufferedWriter.write(string);
                    }
                }

                cache.put(path, string2);
            } catch (IOException iOException) {
                LOGGER.error("Couldn't save tags to {}", path, iOException);
            }
        });
    }

    protected abstract void generate(BiConsumer<NamespacedIdentifier, JsonObject> consumer);

    protected abstract Path getPath(NamespacedIdentifier id);
}
