# Registries API

The Registries API provides an alternative to Vanilla's registry system, with a more modern feature set and available for all Minecraft versions. It also provides an API for synchronizing numerical IDs between the server and client and across sessions, to help avoid desync and world corruption bugs.

## Resource Keys

Registries are in essence fancy `Map`s. The keys used in registries are `ResourceKey`s. A resource key consists of two components, a `registry` identifier that uniquely identifies the registry it's a part of, and an `identifier` that uniquely identifies a resource within that registry. Thus, a resource key can uniquely identity any registered resource.

The `ResourceKeys` class provides factory methods for creating resource keys:

```java
ResourceKey<Block> COOKIE_KEY = ResourceKeys.from(RegistryKeys.BLOCK, NamespacedIdentifiers.from("example", "cookie"));
```

## Registering Resources

The `Registry` class provides helper methods for registering resources to a registry:

```java
Block COOKIE = Registry.register(BlockRegistry.REGISTRY, COOKIE_KEY, new CookieBlock());
```

## Creating Registries

Just like with other resources, you must create a `ResourceKey` that uniquely identifies your registry. The `RegistryKeys` class provides some factory methods for creating basic resource keys for registries.

```java
ResourceKey<Registry<CookieRecipe>> COOKIE_RECIPE_REGISTRY = RegistryKeys.from(NamespacedIdentifiers.from("example", "cookie_recipe"));
```

Registries should be loaded in your mod's entrypoint. The `Registries` class provides helper methods for registering simple and defaulted registries. You can pass along a bootstrap for generating your registry's contents.

```java
Registry<CookieRecipe> COOKIE_RECIPE = Registries.registerSimple(COOKIE_RECIPE_REGISTRY, CookieRecipes::init);
```

An example setup is shown below.

```java
package com.example;

import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.registry.Registries;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.ResourceKey;

public class ExampleRegistries {

	public static final ResourceKey<Registry<CookieRecipe>> COOKIE_RECIPE_REGISTRY = RegistryKeys.from(NamespacedIdentifiers.from("example", "cookie_recipe"));

	public static final Registry<CookieRecipe> COOKIE_RECIPE = Registries.registerSimple(COOKIE_RECIPE_REGISTRY, CookieRecipes::init);

	public static void init() {
	}
}
```

```java
package com.example;

import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.registry.Registry;

public class CookieRecipes {

	public static final CookieRecipe CHOCOLATE_CHIP = Registry.register(ExampleRegistries.COOKIE_RECIPE, NamespacedIdentifiers.from("example", "chocolate_chip"), new ChocolateChipCookieRecipe());

	public static void init() {
	}
}
```

```java
package com.example;

import net.ornithemc.osl.entrypoints.api.ModInitializer;

public class ExampleInitializer implements ModInitializer {

	@Override
	public void init() {
		ExampleRegistries.init();
	}
}
```

## Registry Sync

To ensure numerical IDs are consistent between the server and client and across sessions, you can register registry your registry through the `SyncedRegistries` class. This should also be done in your mod's entrypoint. An update to the above example is shown below.

```java
package com.example;

import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.registry.Registries;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.ResourceKey;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;

public class ExampleRegistries {

	public static final ResourceKey<Registry<CookieRecipe>> COOKIE_RECIPE_REGISTRY = RegistryKeys.from(NamespacedIdentifiers.from("example", "cookie_recipe"));

	public static final Registry<CookieRecipe> COOKIE_RECIPE = Registries.registerSimple(COOKIE_RECIPE_REGISTRY, CookieRecipes::init);

	public static void init() {
		SyncedRegistries.register(COOKIE_RECIPE_REGISTRY);
	}
}
```

If the numerical IDs of your registry are used in any way that is persistent across game sessions (think of Vanilla's item model registry, for example), you must register an ID mapper or fixer for those. This API provides implementations for remapping fastutils' `Int2ObjectMap` and Vanilla's `Id2ObjectBiMap` in `Int2ObjectMapMapper` and `Id2ObjectBiMapMapper` respectively.
