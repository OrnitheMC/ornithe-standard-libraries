package net.ornithemc.osl.keybinds.impl.mixin.client;

import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.options.KeyBinding;

@Mixin(KeyBinding.class)
public interface KeyBindingAccessor {

	@Accessor("CATEGORIES")
	static Set<String> accessCategories() {
		throw new UnsupportedOperationException();
	}
}
