package net.ornithemc.osl.effects.api.effect;

import net.minecraft.entity.living.effect.StatusEffect;
import net.ornithemc.osl.effects.impl.StatusEffectRegistryImpl;
import net.ornithemc.osl.registries.api.registry.Registry;

public interface StatusEffectExtension {

	Registry<StatusEffect> REGISTRY = StatusEffectRegistryImpl.REGISTRY;
	int AUTO_ASSIGN_ID = -172;

}
