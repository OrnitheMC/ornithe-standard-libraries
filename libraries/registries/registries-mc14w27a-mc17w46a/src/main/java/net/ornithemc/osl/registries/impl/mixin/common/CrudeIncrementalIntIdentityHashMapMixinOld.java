package net.ornithemc.osl.registries.impl.mixin.common;

import java.util.IdentityHashMap;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.util.CrudeIncrementalIntIdentityHashMap;

import net.ornithemc.osl.registries.impl.access.Clearable;

@Mixin(CrudeIncrementalIntIdentityHashMap.class)
public class CrudeIncrementalIntIdentityHashMapMixinOld implements Clearable {

	@Shadow
	private IdentityHashMap<?, ?> f_29800171; // ids
	@Shadow
	private List<?> f_87828088; // values

	@Override
	public void osl$registries$clear() {
		this.f_29800171.clear();
		this.f_87828088.clear();
	}
}
