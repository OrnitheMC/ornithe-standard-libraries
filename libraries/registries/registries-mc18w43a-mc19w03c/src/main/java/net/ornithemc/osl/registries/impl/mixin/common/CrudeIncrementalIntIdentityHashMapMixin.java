package net.ornithemc.osl.registries.impl.mixin.common;

import java.util.Arrays;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.util.CrudeIncrementalIntIdentityHashMap;

import net.ornithemc.osl.registries.impl.access.Clearable;

@Mixin(CrudeIncrementalIntIdentityHashMap.class)
public class CrudeIncrementalIntIdentityHashMapMixin implements Clearable {

	@Shadow
	private Object[] keys;
	@Shadow
	private Object[] byId;

	@Shadow
	private int nextId;
	@Shadow
	private int size;

	@Override
	public void osl$registries$clear() {
		Arrays.fill(this.keys, null);
		Arrays.fill(this.byId, null);

		this.nextId = 0;
		this.size = 0;
	}
}
