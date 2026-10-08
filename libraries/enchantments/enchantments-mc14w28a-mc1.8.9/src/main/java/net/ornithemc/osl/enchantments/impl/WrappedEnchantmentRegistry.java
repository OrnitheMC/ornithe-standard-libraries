package net.ornithemc.osl.enchantments.impl;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.resource.Identifier;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.registries.api.registry.ResourceKey;
import net.ornithemc.osl.registries.api.registry.ResourceKeys;
import net.ornithemc.osl.registries.impl.registry.SimpleRegistry;

public class WrappedEnchantmentRegistry extends SimpleRegistry<Enchantment> {

	private boolean skipEnchantmentRegister;

	WrappedEnchantmentRegistry(NamespacedIdentifier identifier) {
		super(identifier);
	}

	public void register(int id, NamespacedIdentifier identifier, Enchantment enchantment) {
		ResourceKey<Enchantment> key = ResourceKeys.from(this.identifier(), identifier);

		this.skipEnchantmentRegister = true;
		this.register(id, key, enchantment);
		this.skipEnchantmentRegister = false;
	}

	@Override
	public <V extends Enchantment> V register(int id, ResourceKey<Enchantment> key, V enchantment) {
		enchantment = super.register(id, key, enchantment);

		if (!this.skipEnchantmentRegister) {
			Identifier identifier = new Identifier(key.identifier().namespace(), key.identifier().identifier());

			if (Enchantment.BY_KEY.containsKey(identifier)) {
				throw new IllegalStateException("Duplicate Enchantment ID " + identifier);
			}

			Enchantment.BY_KEY.put(identifier, enchantment);
		}

		return enchantment;
	}

	@Override
	public void clear() {
		super.clear();

		Enchantment.BY_KEY.clear();
	}
}
