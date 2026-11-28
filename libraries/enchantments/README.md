# Enchantments API

The Enchantments API provides events and utilities for registering and working with enchantments.

## Registering Custom Enchantments

Enchantment registration should be done in a listener to the `REGISTER_ENCHANTMENTS` event. The `EnchantmentRegistry` class provides utility methods for registering enchantments.

An example is shown below.

```java
package com.example;

import net.ornithemc.osl.enchantments.api.EnchantmentEvents;
import net.ornithemc.osl.entrypoints.api.ModInitializer;

public class ExampleInitializer implements ModInitializer {

	@Override
	public void init() {
		EnchantmentEvents.REGISTER_ENCHANTMENTS.register(ExampleEnchantments::init);
	}
}
```

```java
package com.example;

import net.minecraft.enchantment.Enchantment;

import net.ornithemc.osl.core.util.NamespacedIdentifiers;
import net.ornithemc.osl.enchantments.api.EnchantmentRegistry;

public final class ExampleEnchantments {

	public static final CookieEnchantment COOKIE = EnchantmentRegistry.register(NamespacedIdentifiers.from("example", "cookie"), new CookieEnchantment());

	public static void init() {
	}
}
```
