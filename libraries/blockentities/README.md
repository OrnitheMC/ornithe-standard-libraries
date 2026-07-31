# Block Entities API

The Block Entities API provides events and utilities for registering and working with block entities.

## Registering Custom Block Entity Types

Block entity type registration should be done in a listener to the `REGISTER_BLOCK_ENTITY_TYPES` event. The `BlockEntityTypeRegistry` class provides utility methods for registering block entity types.

An example is shown below.

```java
package com.example;

import net.ornithemc.osl.blockentities.api.BlockEntityEvents;
import net.ornithemc.osl.entrypoints.api.ModInitializer;

public class ExampleInitializer implements ModInitializer {

	@Override
	public void init() {
		BlockEntityEvents.REGISTER_BLOCK_ENTITY_TYPES.register(ExampleBlockEntityTypes::init);
	}
}
```

```java
package com.example;

import net.minecraft.block.entity.BlockEntityType;

import net.ornithemc.osl.blockentities.api.BlockEntityTypeRegistry;
import net.ornithemc.osl.blockentities.api.BlockEntityTypes;
import net.ornithemc.osl.core.util.NamespacedIdentifiers;

public final class ExampleBlockEntityTypes {

	public static final BlockEntityType<CookieBlockEntity> COOKIE = BlockEntityTypeRegistry.register(NamespacedIdentifiers.from("example", "cookie"), BlockEntityTypes.builder(CookieBlockEntity::new, ExampleBlocks.COOKIE));

	public static void init() {
	}
}
```
