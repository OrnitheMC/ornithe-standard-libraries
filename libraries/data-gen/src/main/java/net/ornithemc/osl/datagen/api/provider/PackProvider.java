package net.ornithemc.osl.datagen.api.provider;

import net.ornithemc.osl.datagen.api.PackCache;

import java.io.IOException;

public interface PackProvider {
    String getProviderName();
    void provide(PackCache cache) throws IOException;
}
