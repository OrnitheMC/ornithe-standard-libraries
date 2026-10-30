package net.ornithemc.osl.datagen.impl.provider;

import org.quiltmc.parsers.json.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.BiConsumer;

public class LanguageProviderHelper {
    public static void collectFrom(Path languageFilePath, BiConsumer<String, String> consumer) throws IOException {
        if (languageFilePath.endsWith(".lang")) {
            loadFromLang(Files.newInputStream(languageFilePath), consumer);
        } else if (languageFilePath.endsWith(".json")) {
            loadFromJson(Files.newInputStream(languageFilePath), consumer);
        }
    }

    public static void loadFromLang(InputStream is, BiConsumer<String, String> consumer) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                String[] args = line.split("=", 2);
                if (args.length != 2) {
                    continue;
                }
                consumer.accept(args[0], args[1]);
            }
        }
    }

    public static void loadFromJson(InputStream is, BiConsumer<String, String> consumer) throws IOException {
        try (JsonReader reader = JsonReader.json(new InputStreamReader(is))) {
            reader.beginObject();
            while (reader.hasNext()) {
                consumer.accept(reader.nextName() , reader.nextString());
            }
            reader.endObject();
        }
    }
}
