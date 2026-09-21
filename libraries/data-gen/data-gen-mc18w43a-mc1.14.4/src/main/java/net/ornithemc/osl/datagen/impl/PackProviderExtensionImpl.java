package net.ornithemc.osl.datagen.impl;

import net.ornithemc.osl.datagen.api.PackCache;
import net.ornithemc.osl.datagen.api.provider.PackProvider;

public interface PackProviderExtensionImpl extends PackProvider {
    @Override
    default void provide(PackCache cache) {}

    @Override
    default String getProviderName() {
        return "";
    }
}
