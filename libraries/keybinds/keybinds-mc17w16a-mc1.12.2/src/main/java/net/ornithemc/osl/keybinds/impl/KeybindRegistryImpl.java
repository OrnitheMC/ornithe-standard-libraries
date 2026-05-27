package net.ornithemc.osl.keybinds.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.KeyBinding;

import net.ornithemc.osl.keybinds.api.KeyBindingEvents;
import net.ornithemc.osl.keybinds.api.KeyBindingRegistry;
import net.ornithemc.osl.keybinds.api.KeybindEvents;
import net.ornithemc.osl.keybinds.impl.mixin.client.KeyBindingAccessor;

public final class KeybindRegistryImpl {

	private static GameOptions options;
	private static Set<String> categories;
	private static Map<String, Integer> sortOrder;

	private static List<KeyBinding> pendingKeybinds;

	public static Set<String> getCategories() {
		return categories;
	}

	public static KeyBinding register(String name, int defaultKeyCode, String category) {
		return register(new KeyBinding(name, defaultKeyCode, category));
	}

	public static KeyBinding register(KeyBinding keybind) {
		addCategory(keybind.getCategory());
		addKeybind(keybind);

		return keybind;
	}

	private static void addCategory(String category) {
		categories.add(category);

		if (!sortOrder.containsKey(category)) {
			sortOrder.put(category, sortOrder.size() + 1);
		}
	}

	private static void addKeybind(KeyBinding keybind) {
		if (pendingKeybinds != null) {
			pendingKeybinds.add(keybind);
		} else {
			// this code path is only reached if register methods are called after
			// the event has been run - manually append the keybind anyway
			options.keyBindings = ArrayUtils.addAll(options.keyBindings, keybind);
		}
	}

	public static void init(GameOptions gameOptions) {
		options = gameOptions;
		categories = KeyBindingAccessor.accessCategories();
		sortOrder = KeyBindingAccessor.accessCategorySortOrder();

		pendingKeybinds = new ArrayList<>();

		KeyBindingEvents.REGISTER_KEYBINDS.invoker().accept(new KeyBindingRegistry() {

			@Override
			public KeyBinding register(String name, int defaultKeyCode, String category) {
				return KeybindRegistryImpl.register(name, defaultKeyCode, category);
			}

			@Override
			public KeyBinding register(KeyBinding keybind) {
				return KeybindRegistryImpl.register(keybind);
			}
		});
		KeybindEvents.REGISTER_KEYBINDS.invoker().run();

		options.keyBindings = ArrayUtils.addAll(options.keyBindings, pendingKeybinds.toArray(new KeyBinding[0]));
		pendingKeybinds = null;
	}
}
