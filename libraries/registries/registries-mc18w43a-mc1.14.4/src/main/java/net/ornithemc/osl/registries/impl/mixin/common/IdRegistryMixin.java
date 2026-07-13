package net.ornithemc.osl.registries.impl.mixin.common;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.BiMap;

import net.minecraft.resource.Identifier;
import net.minecraft.util.CrudeIncrementalIntIdentityHashMap;
import net.minecraft.util.registry.IdRegistry;

import net.ornithemc.osl.registries.impl.registry.Clearable;
import net.ornithemc.osl.registries.impl.registry.IdRegistryAccess;
import net.ornithemc.osl.registries.impl.registry.WrappedIdRegistry.RegisterCallback;

@Mixin(IdRegistry.class)
public class IdRegistryMixin<T> implements IdRegistryAccess, Clearable {

	@Shadow @Final
	private CrudeIncrementalIntIdentityHashMap<T> ids;
	@Shadow @Final
	private BiMap<Identifier, T> values;

	@Unique
	private RegisterCallback callback;

	@Inject(
		method = "m_26252208",
		at = @At(
			value = "RETURN"
		)
	)
	private void osl$registries$register(int id, Identifier key, T value, CallbackInfoReturnable<T> cir) {
		if (this.callback != null) {
			this.callback.valueRegistered(id, key, value);
		}
	}

	@Override
	public void osl$registries$setCallback(RegisterCallback callback) {
		this.callback = callback;
	}

	@Override
	public boolean osl$registries$has(Object value) {
		return this.values.containsValue(value);
	}

	@Override
	public void osl$registries$clear() {
		this.values.clear();
		Clearable.clear(this.ids);
	}
}
