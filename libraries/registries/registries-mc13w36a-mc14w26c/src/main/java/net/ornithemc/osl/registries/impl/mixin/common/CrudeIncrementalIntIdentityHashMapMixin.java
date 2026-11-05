package net.ornithemc.osl.registries.impl.mixin.common;

import java.util.IdentityHashMap;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import gnu.trove.map.hash.TIntIntHashMap;

import net.minecraft.util.CrudeIncrementalIntIdentityHashMap;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.registries.impl.RegistriesEntrypoint;
import net.ornithemc.osl.registries.impl.access.Clearable;

@Mixin(CrudeIncrementalIntIdentityHashMap.class)
public class CrudeIncrementalIntIdentityHashMapMixin implements Clearable {

	@Conditional(minecraftVersion = @Version({ "<1.7.10-pre3", ">=14w02 <14w25a" }))
	@Shadow
	private TIntIntHashMap ids;
	@Conditional(minecraftVersion = @Version({ "<1.7.10-pre3", ">=14w02 <14w25a" }))
	@Shadow
	private List<?> values;

	@Conditional(minecraftVersion = @Version({ ">=1.7.10-pre3 <=1.7.10", ">=14w25"}))
	@Shadow
	private IdentityHashMap<?, ?> f_29800171; // ids
	@Conditional(minecraftVersion = @Version({ ">=1.7.10-pre3 <=1.7.10", ">=14w25"}))
	@Shadow
	private List<?> f_87828088; // values

	@Override
	public void osl$registries$clear() {
		if (RegistriesEntrypoint.OLD_CRUDE_IDENTITY_HASH_MAP) {
			this.ids.clear();
			this.values.clear();
		} else {
			this.f_29800171.clear();
			this.f_87828088.clear();
		}
	}
}
