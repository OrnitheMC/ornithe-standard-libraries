package net.ornithemc.osl.branding.impl;

import java.util.Objects;

import net.ornithemc.osl.branding.api.BrandingModifier;

public class BrandingModifierImpl {

	private final BrandingModifier modifier;
	private final String value;

	public BrandingModifierImpl(BrandingModifier modifier, String value) {
		this.modifier = modifier;
		this.value = value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof BrandingModifierImpl)) {
			return false;
		}
		BrandingModifierImpl other = (BrandingModifierImpl)o;
		return modifier != BrandingModifier.REPLACE || other.modifier != BrandingModifier.REPLACE;
	}

	@Override
	public int hashCode() {
		return Objects.hash(modifier, value);
	}

	public String apply(String original, String modified) {
		return modifier.apply(original, modified, value);
	}
}
