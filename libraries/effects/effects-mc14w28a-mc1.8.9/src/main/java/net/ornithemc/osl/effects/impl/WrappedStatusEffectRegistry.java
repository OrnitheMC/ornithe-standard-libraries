package net.ornithemc.osl.effects.impl;

import net.minecraft.entity.living.effect.StatusEffect;
import net.minecraft.resource.Identifier;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.registries.api.registry.ResourceKey;
import net.ornithemc.osl.registries.api.registry.ResourceKeys;
import net.ornithemc.osl.registries.impl.registry.SimpleRegistry;

public class WrappedStatusEffectRegistry extends SimpleRegistry<StatusEffect> {

	private boolean skipStatusEffectRegister;

	WrappedStatusEffectRegistry(NamespacedIdentifier identifier) {
		super(identifier);
	}

	public void register(int id, NamespacedIdentifier identifier, StatusEffect effect) {
		ResourceKey<StatusEffect> key = ResourceKeys.from(this.identifier(), identifier);

		this.skipStatusEffectRegister = true;
		this.register(id, key, effect);
		this.skipStatusEffectRegister = false;
	}

	@Override
	public <V extends StatusEffect> V register(int id, ResourceKey<StatusEffect> key, V effect) {
		effect = super.register(id, key, effect);

		if (!this.skipStatusEffectRegister) {
			Identifier identifier = new Identifier(key.identifier().namespace(), key.identifier().identifier());

			if (StatusEffect.REGISTRY.containsKey(identifier)) {
				throw new IllegalStateException("Duplicate StatusEffect ID " + identifier);
			}

			StatusEffect.REGISTRY.put(identifier, effect);
		}

		return effect;
	}

	@Override
	public void clear() {
		super.clear();

		StatusEffect.REGISTRY.clear();
	}
}
