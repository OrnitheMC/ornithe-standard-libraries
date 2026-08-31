# Biomes API

The Biomes API provides events and utilities for registering and working with biomes.

## Registering Custom Biomes

Biome registration should be done in a listener to the `REGISTER_BIOMES` event. The `BiomeRegistry` class provides utility methods for registering biomes.

An example is shown below.

```java
package com.example;

import net.ornithemc.osl.biomes.api.BiomeEvents;
import net.ornithemc.osl.entrypoints.api.ModInitializer;

public class ExampleInitializer implements ModInitializer {

	@Override
	public void init() {
		BiomeEvents.REGISTER_BIOMES.register(ExampleBiomes::init);
	}
}
```

```java
package com.example;

import net.minecraft.world.biome.Biome;

import net.ornithemc.osl.biomes.api.BiomeRegistry;
import net.ornithemc.osl.core.util.NamespacedIdentifiers;

public final class ExampleBiomes {

	public static final CookieBiome COOKIE = BiomeRegistry.register(NamespacedIdentifiers.from("example", "cookie"), new CookieBiome(new Biome.Settings().setName("Cookie")));

	public static void init() {
	}
}
```
