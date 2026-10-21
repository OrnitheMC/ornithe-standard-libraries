package net.ornithemc.osl.datagen.impl;

import net.ornithemc.osl.datagen.api.PackCache;
import net.ornithemc.osl.datagen.api.provider.ModDataProvider;

public interface ModDataProviderExtensionImpl extends ModDataProvider {
    @Override
    default void provide(PackCache cache) {}

    @Override
    default String getProviderName() {
        return "";
    }
}
