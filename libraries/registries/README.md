# Registries API

The Registries API provides an alternative to Vanilla's registry system, with a more modern feature set and available for all Minecraft versions. It also provides an API for synchronizing numerical IDs between the server and client and across sessions, to help avoid desync and world corruption bugs.

## Resource Keys

Registries are used to keep track of in-game resources. They are assumed to hold all known values of a type, and each value is assigned a unique namespaced identifier. Registries themselves are also registered to their own registry. Thus, a single pair of namespaced identifiers can identify an in-game resource: the first specifies the registry the resource belongs to, and the second identifies the resource within that registry. This is called a `ResourceKey`, and it can be used to refer to a resource without a reference to the actual object. For example, `minecraft:block/minecraft:stone` points to the block `minecraft:stone` in the registry `minecraft:block`.

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

Just like with other resources, you must create a `ResourceKey` that uniquely identifies your registry. The `RegistryKeys` class provides some factory methods for creating resource keys for registries.

```java
ResourceKey<Registry<CookieRecipe>> COOKIE_RECIPE_REGISTRY = RegistryKeys.from(NamespacedIdentifiers.from("example", "cookie_recipe"));
```

Registries should be loaded in your mod's entrypoint. The `Registries` class provides helper methods for registering simple and defaulted registries. You can pass along a callback for bootstrapping your registry's contents.

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

Numerical IDs may be used for serialization in server-client communication or in world saves. If this is the case, you must register your registry to be synchronized. The `SyncedRegistries` class has helper methods for this. An update to the above example is shown below.

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

It is not recommended that you use the numerical IDs in any other places. If it is unavoidable, however, you can register a custom `IdMapper` or `IdFixer` to ensure there are no desync or corruption bugs. This API provides several `IdMapper` implementations you can use, such `BooleanArrayMapper`, `IntArrayMapper`, `ListMapper`, and `Int2ObjectMapMapper`. You can of course create a custom implementation as well.
