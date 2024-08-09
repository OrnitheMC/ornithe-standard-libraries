package net.ornithemc.osl.keybinds.api;

import net.minecraft.client.options.KeyBinding;

public interface KeyBindingRegistry {

	KeyBinding register(String name, int defaultKeyCode, String category);

	KeyBinding register(KeyBinding keybind);

}
