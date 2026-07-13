package net.ornithemc.osl.registries.impl.mixin.common;

import java.util.Map;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.util.CrudeIncrementalIntIdentityHashMap;
import net.minecraft.util.registry.IdRegistry;
import net.minecraft.util.registry.MappedRegistry;

import net.ornithemc.osl.registries.impl.access.Clearable;
import net.ornithemc.osl.registries.impl.access.IdRegistryAccess;

@Mixin(IdRegistry.class)
public class IdRegistryMixin<K, V> extends MappedRegistry<K, V> implements IdRegistryAccess, Clearable {

	@Shadow @Final
	private CrudeIncrementalIntIdentityHashMap<V> ids;
	@Shadow @Final
	private Map<V, K> keys;

	@Unique
	private RegisterCallback callback;

	@Inject(
		method = "register",
		at = @At(
			value = "RETURN"
		)
	)
	private void osl$registries$register(int id, K key, V value, CallbackInfo ci) {
		if (this.callback != null) {
			this.callback.valueRegistered(id, key, value);
		}
	}

	@Override
	public void osl$registries$setCallback(RegisterCallback callback) {
		this.callback = callback;
	}

	@Override
	public void osl$registries$clear() {
		this.entries.clear();
		this.keys.clear();
		Clearable.clear(this.ids);
	}
}
