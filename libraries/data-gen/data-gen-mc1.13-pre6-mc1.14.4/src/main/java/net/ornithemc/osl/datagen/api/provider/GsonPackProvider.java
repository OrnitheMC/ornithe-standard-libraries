package net.ornithemc.osl.datagen.api.provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.ModContainer;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.datagen.api.PackGenerator;
import net.ornithemc.osl.datagen.api.PackCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.BiConsumer;

public abstract class GsonPackProvider implements PackProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    public final PackGenerator generator;
    public final ModContainer mod;

    protected GsonPackProvider(PackGenerator generator, ModContainer mod) {
        this.generator = generator;
        this.mod = mod;
    }

    @Override
    public void provide(PackCache cache) {
        generate((id, json) -> {
            Path path = getPath(id);

            try {
                String string = GSON.toJson(json);
                cache.checkAndWrite(path, string);
            } catch (IOException iOException) {
                LOGGER.error("Couldn't save data to {}", path, iOException);
            }
        });
    }

    protected abstract void generate(BiConsumer<NamespacedIdentifier, JsonObject> consumer);

    protected abstract Path getPath(NamespacedIdentifier id);
}
