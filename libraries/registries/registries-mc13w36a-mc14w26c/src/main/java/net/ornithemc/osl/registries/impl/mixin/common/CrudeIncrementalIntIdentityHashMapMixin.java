package net.ornithemc.osl.registries.impl.mixin.common;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import gnu.trove.map.hash.TIntIntHashMap;

import net.minecraft.util.CrudeIncrementalIntIdentityHashMap;

import net.ornithemc.osl.registries.impl.access.Clearable;

@Mixin(CrudeIncrementalIntIdentityHashMap.class)
public class CrudeIncrementalIntIdentityHashMapMixin implements Clearable {

	@Shadow
	private TIntIntHashMap ids;
	@Shadow
	private List<?> values;

	@Override
	public void osl$registries$clear() {
		this.ids.clear();
		this.values.clear();
	}
}
