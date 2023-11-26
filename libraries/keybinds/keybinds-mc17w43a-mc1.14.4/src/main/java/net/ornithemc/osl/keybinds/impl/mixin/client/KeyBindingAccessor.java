package net.ornithemc.osl.keybinds.impl.mixin.client;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.options.KeyBinding;

@Mixin(KeyBinding.class)
public interface KeyBindingAccessor {

	@Accessor("CATEGORY_SORT_ORDER")
	static Map<String, Integer> osl$keybinds$getCategorySortOrder() {
		throw new UnsupportedOperationException();
	}
}
