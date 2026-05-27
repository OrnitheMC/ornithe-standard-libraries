package net.ornithemc.osl.keybinds.api;

import java.util.Set;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.options.KeyBinding;

import net.ornithemc.osl.keybinds.impl.KeybindRegistryImpl;

/**
 * Public access to the Keybinds registry.
 */
public final class KeybindRegistry {

	/**
	 * @return the set of all keybind categories.
	 */
	public static Set<String> getCategories() {
		return KeybindRegistryImpl.getCategories();
	}

	/**
	 * @param name           the name or translation key of the keybind.
	 * @param defaultKeyCode the default key code of the keybind.
	 * @param category       the name or translation key of the category to which the keybind belongs.
	 * @return the registered keybind.
	 */
	public static KeyBinding register(String name, int defaultKeyCode, String category) {
		return KeybindRegistryImpl.register(name, defaultKeyCode, category);
	}

	/**
	 * @param name           the name or translation key of the keybind.
	 * @param type           the type of input allowed for the default key code.
	 * @param defaultKeyCode the default key code of the keybind.
	 * @param category       the name or translation key of the category to which the keybind belongs.
	 * @return the registered keybind.
	 */
	public static KeyBinding register(String name, InputConstants.Type type, int defaultKeyCode, String category) {
		return KeybindRegistryImpl.register(name, type, defaultKeyCode, category);
	}

	/**
	 * @param keybind the keybind to register.
	 * @return the registered keybind.
	 */
	public static KeyBinding register(KeyBinding keybind) {
		return KeybindRegistryImpl.register(keybind);
	}
}
