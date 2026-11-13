package net.ornithemc.osl.registries.impl.mixin.common;

import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.util.CrudeIncrementalIntIdentityHashMap;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.registries.impl.RegistriesEntrypoint;
import net.ornithemc.osl.registries.impl.access.Clearable;

@Mixin(CrudeIncrementalIntIdentityHashMap.class)
public class CrudeIncrementalIntIdentityHashMapMixin implements Clearable {

	@Conditional(minecraftVersion = @Version("<15w35a"))
	@Shadow
	private IdentityHashMap<?, ?> f_29800171; // ids
	@Conditional(minecraftVersion = @Version("<15w35a"))
	@Shadow
	private List<?> f_87828088; // values

	@Conditional(minecraftVersion = @Version(">=15w35a"))
	@Shadow
	private Object[] keys;
	@Conditional(minecraftVersion = @Version(">=15w35a"))
	@Shadow
	private Object[] byId;

	@Conditional(minecraftVersion = @Version(">=15w35a"))
	@Shadow
	private int nextId;
	@Conditional(minecraftVersion = @Version(">=15w35a"))
	@Shadow
	private int size;

	@Override
	public void osl$registries$clear() {
		if (RegistriesEntrypoint.OLD_CRUDE_IDENTITY_HASH_MAP) {
			this.f_29800171.clear();
			this.f_87828088.clear();
		} else {
			Arrays.fill(this.keys, null);
			Arrays.fill(this.byId, null);

			this.nextId = 0;
			this.size = 0;
		}
	}
}
