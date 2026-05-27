package net.ornithemc.osl.keybinds.impl.keybind;

import net.ornithemc.osl.keybinds.api.keybind.KeyBindingExtension;

public interface KeyBindingExtensionImpl extends KeyBindingExtension {

	@Override
	default String getCategory() {
		throw new AbstractMethodError();
	}
}
