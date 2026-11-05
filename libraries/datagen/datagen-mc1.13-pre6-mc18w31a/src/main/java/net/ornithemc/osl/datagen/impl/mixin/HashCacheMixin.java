package net.ornithemc.osl.datagen.impl.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.data.HashCache;
import net.ornithemc.osl.core.impl.util.MinecraftVersion;
import net.ornithemc.osl.datagen.api.PackCache;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
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

    @Inject(method = "write", at = @At(value = "INVOKE", target = "Lorg/apache/commons/io/IOUtils;writeLines(Ljava/util/Collection;Ljava/lang/String;Ljava/io/Writer;)V"))
    private void osl$addVersionHeader(CallbackInfo ci, @Local Writer writer) throws IOException {
        writer.write("// " + MinecraftVersion.resolve().gameVersion() + System.lineSeparator());
    }
}
