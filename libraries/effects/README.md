# Effects API

The Effects API provides events and utilities for registering and working with status effects.

## Registering Custom Status Effects

Effect registration should be done in a listener to the `REGISTER_STATUS_EFFECTS` event. The `StatusEffectRegistry` class provides utility methods for registering effects.

An example is shown below.

```java
package com.example;

import net.ornithemc.osl.effects.api.StatusEffectEvents;
import net.ornithemc.osl.entrypoints.api.ModInitializer;

public class ExampleInitializer implements ModInitializer {

	@Override
	public void init() {
		StatusEffectEvents.REGISTER_STATUS_EFFECTS.register(ExampleStatusEffects::init);
	}
}
```

```java
package com.example;

import net.minecraft.entity.living.effect.StatusEffect;

import net.ornithemc.osl.core.util.NamespacedIdentifiers;
import net.ornithemc.osl.effects.api.StatusEffectRegistry;

public final class ExampleStatusEffects {

	public static final CookieStatusEffect COOKIE = StatusEffectRegistry.register(NamespacedIdentifiers.from("example", "cookie"), new CookieStatusEffect());

	public static void init() {
	}
}
```
