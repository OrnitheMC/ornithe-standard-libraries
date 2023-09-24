# Keybinds API

The keybinds API provides events for registering keybinds. You may register a callback to this event in your mod's initializer:

```java
package com.example;

import net.minecraft.client.options.KeyBinding;

import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.keybinds.api.KeyBindingEvents;

import org.lwjgl.input.Keyboard;

public class ExampleInitializer implements ClientModInitializer {

	public static KeyBinding cookieKeybind;

	@Override
	public void initClient() {
		KeyBindingEvents.REGISTER_KEYBINDS.register(registry -> {
			cookieKeybind = registry.register("Cookie", Keyboard.KEY_NONE, "Example Mod");
		});
	}
}
```
