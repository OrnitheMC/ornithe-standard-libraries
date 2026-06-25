# Keybinds API

The Keybinds API provides events and utilities for registering and working with keybinds.

## Registering Custom Keybinds

Keybind registration should be done in a listener to the `REGISTER_KEYBINDS` event. The `KeybindRegistry` class provides utility methods for registering keybinds.

An example is shown below.

```java
package com.example;

import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.keybinds.api.KeybindEvents;

public class ExampleInitializer implements ClientModInitializer {

	@Override
	public void initClient() {
		KeybindEvents.REGISTER_KEYBINDS.register(ExampleKeybinds::init);
	}
}
```

```java
package com.example;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.options.KeyBinding;

import net.ornithemc.osl.keybinds.api.KeybindRegistry;

public final class ExampleKeybinds {

	public static final KeyBinding COOKIE = KeybindRegistry.register("cookie", GLFW.GLFW_KEY_Z, "example");

	public static void init() {
	}
}
```
