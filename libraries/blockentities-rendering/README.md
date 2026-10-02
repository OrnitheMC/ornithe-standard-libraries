# Block Entities Rendering API

The Block Entities Rendering API provides events and utilities for registering and working with block entity renderers.

## Registering Custom Block Entity Renderers

Block entity renderer registration should be done in a listener to the `REGISTER_BLOCK_ENTITY_RENDERERS` event.

An example is shown below.

```java
package com.example;

import net.ornithemc.osl.blockentities.api.client.BlockEntityRenderingEvents;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;

public class ExampleInitializer implements ClientModInitializer {

	@Override
	public void initClient() {
		BlockEntityRenderingEvents.REGISTER_BLOCK_ENTITY_RENDERERS.register(registry -> {
			registry.register(CookieBlockEntity.class, CookieBlockRenderer::new);
		});
	}
}
```
