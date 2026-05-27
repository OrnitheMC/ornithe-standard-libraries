package net.ornithemc.osl.keybinds.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.client.options.KeyBinding;

import net.ornithemc.osl.keybinds.api.keybind.KeyBindingExtension;
import net.ornithemc.osl.keybinds.impl.access.KeyBindingAccess;

@Mixin(KeyBinding.class)
public class KeyBindingMixin implements KeyBindingExtension, KeyBindingAccess {

	@Unique
	private String category = "None";

	@Override
	public String getCategory() {
		return this.category;
	}

	@Override
	public void osl$keybinds$setCategory(String category) {
		this.category = category;
	}
}
