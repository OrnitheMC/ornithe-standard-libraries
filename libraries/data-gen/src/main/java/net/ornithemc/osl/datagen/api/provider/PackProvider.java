package net.ornithemc.osl.datagen.api.provider;

import net.ornithemc.osl.datagen.api.PackCache;

public interface PackProvider {
    String getProviderName();
    void provide(PackCache cache);
}
