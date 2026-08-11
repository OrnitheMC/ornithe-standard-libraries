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
public class IdRegistryMixin<T> extends MappedRegistry<String, T> implements IdRegistryAccess, Clearable {

	@Shadow @Final
	private CrudeIncrementalIntIdentityHashMap<T> ids;
	@Shadow @Final
	private Map<T, String> keys;

	@Unique
	private RegisterCallback callback;
	@Unique
	private int nextId;

	@Shadow
	private void register(int id, String key, T value) { }

	@Redirect(
		method = "register",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/util/registry/IdRegistry;put(Ljava/lang/Object;Ljava/lang/Object;)V"
		)
	)
	private void osl$registries$replaceThisPutWithSuperPut(IdRegistry<T> self, Object key, T value) {
		super.put((String) key, value);
	}

	@Inject(
		method = "register",
		at = @At(
			value = "RETURN"
		)
	)
	private void osl$registries$register(int id, String key, T value, CallbackInfo ci) {
		this.nextId = Math.max(this.nextId, id + 1);

		if (this.callback != null) {
			this.callback.valueRegistered(id, key, value);
		}
	}

	@Override
	public void put(String key, T value) {
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
