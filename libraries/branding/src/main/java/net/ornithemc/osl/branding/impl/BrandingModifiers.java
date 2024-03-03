package net.ornithemc.osl.branding.impl;

import java.util.EnumMap;
import java.util.Map;

import net.ornithemc.osl.branding.api.BrandingContext;
import net.ornithemc.osl.branding.api.BrandingModifierRegistry;
import net.ornithemc.osl.branding.api.Operation;

public class BrandingModifiers implements BrandingModifierRegistry {

	private final Map<BrandingContext, BrandingModifier> modifiers;

	public BrandingModifiers() {
		this.modifiers = new EnumMap<>(BrandingContext.class);

		for (BrandingContext context : BrandingContext.values()) {
			if (context != BrandingContext.ALL) {
				this.modifiers.put(context, new BrandingModifier());
			}
		}
	}

	@Override
	public void register(BrandingContext context, String key, Operation operation, String value) {
		register(context, key, operation, value, 0, true);
	}

	@Override
	public void register(BrandingContext context, String key, Operation operation, String value, int priority) {
		register(context, key, operation, value, priority, true);
	}

	@Override
	public void register(BrandingContext context, String key, Operation operation, String value, int priority, boolean required) {
		if (context == BrandingContext.ALL) {
			for (BrandingContext c : modifiers.keySet()) {
				register(c, key, operation, value, priority, required);
			}
		} else {
			modifiers.get(context).register(key, operation, value, priority, required);
		}
	}

	public String apply(BrandingContext context, String s) {
		BrandingModifier modifier = modifiers.get(context);

		if (modifier == null) {
			throw new IllegalArgumentException("no branding modifier for context " + context.toString().toLowerCase());
		} else {
			try {
				return modifier.apply(s);
			} catch (Throwable t) {
				throw new RuntimeException("unable to apply branding modifier for " + context.toString().toLowerCase());
			}
		}
	}
}
