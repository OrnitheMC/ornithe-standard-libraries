package net.ornithemc.osl.keybinds.impl.mixin.client;

import java.util.LinkedHashSet;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.KeyBinding;

import net.ornithemc.osl.keybinds.api.KeyBindingEvents;
import net.ornithemc.osl.keybinds.api.KeyBindingRegistry;

@Mixin(GameOptions.class)
public class GameOptionsMixin implements KeyBindingRegistry {

	@Shadow private KeyBinding[] keyBindings;
	private Set<KeyBinding> modKeyBindings;

	@Inject(
		method = "<init>(Lnet/minecraft/client/Minecraft;Ljava/io/File;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/options/GameOptions;load()V"
		)
	)
	private void osl$keybinds$registerKeybinds(CallbackInfo ci) {
		modKeyBindings = new LinkedHashSet<>();

		KeyBindingEvents.REGISTER_KEYBINDS.invoker().accept(this);

		KeyBinding[] mcKeybinds = keyBindings;
		keyBindings = new KeyBinding[mcKeybinds.length + modKeyBindings.size()];

		int i = 0;
		for (KeyBinding keybind : mcKeybinds) {
			keyBindings[i++] = keybind;
		}
		for (KeyBinding keybind : modKeyBindings) {
			keyBindings[i++] = keybind;
		}
	}

	@Override
	public KeyBinding register(String name, int defaultKeyCode, String category) {
		return register(new KeyBinding(name, defaultKeyCode, category));
	}

	@Override
	public KeyBinding register(KeyBinding keybind) {
		modKeyBindings.add(keybind);
		return keybind;
	}
}
