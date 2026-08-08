# Entities Rendering API

The Entities Rendering API provides events and utilities for registering and working with entity renderers.

## Registering Custom Entity Renderers

Entity renderer registration should be done in a listener to the `REGISTER_ENTITY_RENDERERS` event.

An example is shown below.

```java
package com.example;

import net.ornithemc.osl.entities.api.client.EntityRenderingEvents;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;

public class ExampleInitializer implements ClientModInitializer {

	@Override
	public void initClient() {
		EntityRenderingEvents.REGISTER_ENTITY_RENDERERS.register(registry -> {
			registry.register(CookieMonsterEntity.class, CookieMonsterRenderer::new);
		});
	}
}
```
