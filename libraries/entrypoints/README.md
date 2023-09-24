# Entrypoints API

The Entrypoints API allows mods to submit entrypoints that are called before the game is initialized.
These entrypoints should be registered in your `mod.json`.

example `fabric.mod.json`:

```json
{
	entrypoints: {
		"init": [
			"com.example.ExampleInitializer"
		],
		"client-init": [
			"com.example.ExampleClientInitializer"
		],
		"server-init": [
			"com.example.ExampleServerInitializer"
		]
	}
}
```

example `quilt.mod.json`:

```json
{
	"quilt_loader": {
		"entrypoints": {
			"init": "com.example.ExampleInitializer",
			"client-init": "com.example.ExampleClientInitializer",
			"server-init": "com.example.ExampleServerInitializer"
		}
	}
}
```

Your entrypoints should then implement the appropriate interface:

```java
package com.example;

import net.ornithemc.osl.entrypoints.api.ModInitializer;

public class ExampleInitializer implements ModInitializer {

	@Override
	public void init() {
		// this entrypoint will be called before game start-up, on both the client and server
	}
}
```
```java
package com.example;

import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;

public class ExampleClientInitializer implements ClientModInitializer {

	@Override
	public void initClient() {
		// this entrypoint will be called before game start-up, on the client only
	}
}
```
```java
package com.example;

import net.ornithemc.osl.entrypoints.api.server.ServerModInitializer;

public class ExampleServerInitializer implements ServerModInitializer {

	@Override
	public void initServer() {
		// this entrypoint will be called before game start-up, on the dedicated server only
	}
}
```
