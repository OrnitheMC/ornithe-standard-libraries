package net.ornithemc.osl.registries.impl.mixin.common;

import java.util.Map;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
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
	@Unique
	private int nextId;

	@Shadow
	private void register(int id, K key, V value) { }

	@Redirect(
		method = "register",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/util/registry/IdRegistry;put(Ljava/lang/Object;Ljava/lang/Object;)V"
		)
	)
	private void osl$registries$replaceThisPutWithSuperPut(IdRegistry<K, V> self, K key, V value) {
		super.put(key, value);
	}

	@Inject(
		method = "register",
		at = @At(
			value = "RETURN"
		)
	)
	private void osl$registries$register(int id, K key, V value, CallbackInfo ci) {
		this.nextId = Math.max(this.nextId, id + 1);

		if (this.callback != null) {
			this.callback.valueRegistered(id, key, value);
		}
	}

	@Override
	public void put(K key, V value) {
		this.register(this.nextId, key, value);
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

		this.nextId = 0;
	}
}
