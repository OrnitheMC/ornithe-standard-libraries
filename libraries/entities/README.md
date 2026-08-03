# Entities API

The Entities API provides events and utilities for registering and working with entities.

## Registering Custom Entity Types

Entity type registration should be done in a listener to the `REGISTER_ENTITY_TYPES` event. The `EntityTypeRegistry` class provides utility methods for registering entity types.

An example is shown below.

```java
package com.example;

import net.ornithemc.osl.entities.api.EntityEvents;
import net.ornithemc.osl.entrypoints.api.ModInitializer;

public class ExampleInitializer implements ModInitializer {

	@Override
	public void init() {
		EntityEvents.REGISTER_ENTITY_TYPES.register(ExampleEntityTypes::init);
	}
}
```

```java
package com.example;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.living.MobCategory;

import net.ornithemc.osl.entities.api.EntityTypeRegistry;
import net.ornithemc.osl.entities.api.EntityTypes;
import net.ornithemc.osl.core.util.NamespacedIdentifiers;

public final class ExampleEntityTypes {

	public static final EntityType<CookieMonsterEntity> COOKIE_MONSTER = EntityTypeRegistry.register(NamespacedIdentifiers.from("example", "cookie_monster"), EntityTypes.builder(CookieMonsterEntity::new, MobCategory.MISC));

	public static void init() {
	}
}
```
