package net.ornithemc.osl.keybinds.api;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.options.KeyBinding;

public interface KeyBindingRegistry {

	KeyBinding register(String name, int defaultKeyCode, String category);

	KeyBinding register(String name, InputConstants.Type type, int defaultKeyCode, String category);

	KeyBinding register(KeyBinding keybind);

}
