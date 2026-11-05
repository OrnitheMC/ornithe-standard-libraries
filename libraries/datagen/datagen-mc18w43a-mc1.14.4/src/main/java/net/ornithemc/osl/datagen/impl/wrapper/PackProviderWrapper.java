package net.ornithemc.osl.datagen.impl.wrapper;

import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.ornithemc.osl.datagen.api.provider.ModDataProvider;

import java.io.IOException;

public class PackProviderWrapper implements DataProvider {
    private final ModDataProvider delegate;

    public PackProviderWrapper(ModDataProvider delegate) {
        this.delegate = delegate;
    }

    @Override
    public void run(HashCache cache) throws IOException {
        delegate.provide(cache);
    }

    @Override
    public String getName() {
        return delegate.getProviderName();
    }
}
