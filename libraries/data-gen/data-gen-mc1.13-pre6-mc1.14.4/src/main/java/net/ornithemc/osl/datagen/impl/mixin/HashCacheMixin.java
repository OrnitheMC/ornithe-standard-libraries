package net.ornithemc.osl.datagen.impl.mixin;

import net.minecraft.data.HashCache;
import net.ornithemc.osl.datagen.api.PackCache;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Mixin(HashCache.class)
public abstract class HashCacheMixin implements PackCache {
    @Shadow
    @Nullable
    public abstract String get(Path path);

    @Shadow
    public abstract void put(Path path, String hash);

    @Override
    public void checkAndWrite(Path path, byte[] data) throws IOException {
        String hash = SHA1.hashBytes(data).toString();

        if (!Objects.equals(this.get(path), hash) || !Files.exists(path)) {
            Files.createDirectories(path.getParent());

            Files.write(path, data);
        }

        this.put(path, hash);
    }

    @Override
    public void checkAndWrite(Path path, String data) throws IOException {
        String hash = SHA1.hashUnencodedChars(data).toString();

        if (!Objects.equals(this.get(path), hash) || !Files.exists(path)) {
            Files.createDirectories(path.getParent());

            try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path)) {
                bufferedWriter.write(data);
            }
        }

        this.put(path, hash);
    }
}
