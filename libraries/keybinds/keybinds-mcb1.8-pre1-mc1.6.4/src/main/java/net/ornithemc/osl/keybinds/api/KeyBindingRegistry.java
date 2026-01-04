package net.ornithemc.osl.keybinds.api;

import net.minecraft.client.options.KeyBinding;

public interface KeyBindingRegistry {

	KeyBinding register(String name, int defaultKeyCode);

	KeyBinding register(KeyBinding keybind);

}
