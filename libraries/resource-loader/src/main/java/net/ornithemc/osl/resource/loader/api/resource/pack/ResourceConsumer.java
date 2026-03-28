package net.ornithemc.osl.resource.loader.api.resource.pack;

import java.io.InputStream;
import java.util.function.BiConsumer;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.function.IOSupplier;

@FunctionalInterface
public interface ResourceConsumer extends BiConsumer<NamespacedIdentifier, IOSupplier<InputStream>> {
}
