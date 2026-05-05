# Blocks API

The Blocks API provides events and utilities for registering and working with blocks.

## Registering Custom Blocks

Block registration should be done in a listener to the `REGISTER_BLOCKS` event. The `BlockRegistry` class provides utility methods for registering blocks.

An example is shown below.

```java
package com.example;

import net.ornithemc.osl.blocks.api.BlockEvents;
import net.ornithemc.osl.entrypoints.api.ModInitializer;

public class ExampleInitializer implements ModInitializer {

	@Override
	public void init() {
		BlockEvents.REGISTER_BLOCKS.register(ExampleBlocks::init);
	}
}
```

```java
package com.example;

import net.minecraft.block.Block;

import net.ornithemc.osl.blocks.api.BlockRegistry;
import net.ornithemc.osl.core.util.NamespacedIdentifiers;

public final class ExampleBlocks {

	public static final CookieBlock COOKIE = BlockRegistry.register(NamespacedIdentifiers.from("example", "cookie"), new CookieBlock());

	public static void init() {
	}
}
```
