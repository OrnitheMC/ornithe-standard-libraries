# Lifecycle Events API

The Lifecycle Events API provides events to track the lifecycle of the Minecraft client and server.
You may register a callback to these events in your mod's initializer:

```java
package com.example;

import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.lifecycle.api.server.MinecraftServerEvents;

public class ExampleInitializer implements ModInitializer {

	@Override
	public void init() {
		MinecraftServerEvents.START.register(server -> {
			// this code will be called upon server start-up
		});
	}
}
```

While `MinecraftClientEvents` provides events for the lifecycle of the game client, the events in `MineraftServerEvents`
are called for both integrated and dedicated servers. `ClientWorldEvents` and `ServerWorldEvents` provide events for the
lifecycle of client worlds and server worlds respectively. Note that a "world" is a single dimension, as opposed to
the collection of dimensions that make up a world save.