package net.ornithemc.osl.branding.api;

public interface BrandingModifierRegistry {

	/**
	 * Registers a modifier component for the given branding context.
	 *
	 * @param context    The branding context in which the modifier component should apply.
	 *                   Using {@link BrandingContext#ALL} will register it to all contexts.
	 * @param key        A string that uniquely identifies this component within the modifier.
	 * @param operation  The modification that this component will apply to the branding string.
	 * @param value      The value that this component will apply to the branding string.
	 */
	void register(BrandingContext context, String key, Operation operation, String value);

	/**
	 * Registers a modifier component for the given branding context.
	 *
	 * @param context    The branding context in which the modifier component should apply.
	 *                   Using {@link BrandingContext#ALL} will register it to all contexts.
	 * @param key        A string that uniquely identifies this component within the modifier.
	 * @param operation  The modification that this component will apply to the branding string.
	 * @param value      The value that this component will apply to the branding string.
	 * @param priority   The priority with which this component will be sorted against other components.
	 */
	void register(BrandingContext context, String key, Operation operation, String value, int priority);

	/**
	 * Registers a modifier component for the given branding context.
	 *
	 * @param context    The branding context in which the modifier component should apply.
	 *                   Using {@link BrandingContext#ALL} will register it to all contexts.
	 * @param key        A string that uniquely identifies this component within the modifier.
	 * @param operation  The modification that this component will apply to the branding string.
	 * @param value      The value that this component will apply to the branding string.
	 * @param priority   The priority with which this component will be sorted against other components.
	 * @param required   Whether this component is required. If multiple required components
	 *                   are attempted to be registered an exception will be thrown, otherwise
	 *                   the unrequired component will be removed.
	 */
	void register(BrandingContext context, String key, Operation operation, String value, int priority, boolean required);

}
