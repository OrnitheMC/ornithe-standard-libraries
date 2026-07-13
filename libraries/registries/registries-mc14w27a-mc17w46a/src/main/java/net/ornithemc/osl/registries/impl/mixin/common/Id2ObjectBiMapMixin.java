package net.ornithemc.osl.registries.impl.mixin.common;

import java.util.IdentityHashMap;
import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

import net.minecraft.util.Id2ObjectBiMap;

import net.ornithemc.osl.registries.impl.registry.Clearable;
import net.ornithemc.osl.registries.impl.registry.Id2ObjectBiMapAccess;

@Mixin(Id2ObjectBiMap.class)
public class Id2ObjectBiMapMixin<T> implements Id2ObjectBiMapAccess, Clearable {

	@Shadow @Final
	private List<T> values;
	@Shadow @Final
	private IdentityHashMap<T, Integer> ids;

	@Unique
	private final IntSet idSet = new IntOpenHashSet();

	@Inject(
		method = "put(Ljava/lang/Object;I)V",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$registries$put(T value, int id, CallbackInfo ci) {
		this.idSet.add(id);
	}

	@Override
	public IntSet osl$registries$idSet() {
		return this.idSet;
	}

	@Override
	public void osl$registries$clear() {
		this.values.clear();
		this.ids.clear();
		this.idSet.clear();
	}
}
