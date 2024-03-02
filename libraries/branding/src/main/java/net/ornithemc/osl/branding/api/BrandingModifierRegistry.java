package net.ornithemc.osl.branding.api;

public interface BrandingModifierRegistry {

	void register(BrandingContext context, String key, Operation operation, String value);

	void register(BrandingContext context, String key, Operation operation, String value, int priority);

}
