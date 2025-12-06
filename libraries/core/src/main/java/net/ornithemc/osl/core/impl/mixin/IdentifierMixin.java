package net.ornithemc.osl.core.impl.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.resource.Identifier;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;

@Pseudo // needed because Identifier does not exist in all versions
@Mixin(Identifier.class)
public class IdentifierMixin implements NamespacedIdentifier { // TODO: interface injection

	@Shadow
	private String namespace;
	@Shadow
	private String path;

	@Inject(
		method = "equals",
		remap = false,
		cancellable = true,
		at = @At(
			value = "HEAD"
		)
	)
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
	public String identifier() {
		return path;
	}
}
