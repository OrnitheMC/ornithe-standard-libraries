# Items API

The Items API provides events and utilities for registering and working with items.

## Registering Custom Items

Item registration should be done in a listener to the `REGISTER_ITEMS` event. The `ItemRegistry` class provides utility methods for registering items.

An example is shown below.

```java
package com.example;

import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.items.api.ItemEvents;

public class ExampleInitializer implements ModInitializer {

	@Override
	public void init() {
		ItemEvents.REGISTER_ITEMS.register(ExampleItems::init);
	}
}
```

```java
package com.example;

import net.minecraft.item.Item;

import net.ornithemc.osl.core.util.NamespacedIdentifiers;
import net.ornithemc.osl.items.api.ItemRegistry;

public final class ExampleItems {

	public static final CookieItem COOKIE = ItemRegistry.register(NamespacedIdentifiers.from("example", "cookie"), new CookieItem());

	public static void init() {
	}
}
```
