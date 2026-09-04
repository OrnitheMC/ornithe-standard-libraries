package net.ornithemc.osl.core.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.resource.Identifier;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;

@Mixin(Identifier.class)
public class IdentifierMixin implements NamespacedIdentifier {
	@Shadow
	private String namespace;

	@Shadow
	private String path;

	@Inject(method = "equals", at = @At(value = "HEAD"), cancellable = true, remap = false)
	private void osl$core$equalsNamespacedIdentifier(Object o, CallbackInfoReturnable<Boolean> cir) {
		if (o instanceof NamespacedIdentifier) {
			cir.setReturnValue(NamespacedIdentifiers.equals(this, (NamespacedIdentifier) o));
		}
	}

	@Override
	public String namespace() {
		return namespace;
	}

	@Override
	public String path() {
		return path;
	}
}
