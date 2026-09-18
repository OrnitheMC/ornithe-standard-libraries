package net.ornithemc.osl.datagen.api;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.io.IOException;
import java.nio.file.Path;

public interface PackCache {
    HashFunction SHA1 = Hashing.sha1();

    void checkAndWrite(Path path, byte[] data) throws IOException;
    void checkAndWrite(Path path, String data) throws IOException;
}
