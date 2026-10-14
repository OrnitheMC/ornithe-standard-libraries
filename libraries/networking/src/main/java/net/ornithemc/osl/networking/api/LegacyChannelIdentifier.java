package net.ornithemc.osl.networking.api;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

/**
 * A {@linkplain NamespacedIdentifier} implementation that represents a packet
 * channel in the legacy format used in 1.13-pre2 and below. The legacy format
 * uses the vertical bar character ({@code '|'}) as the separator, but that is
 * only by convention, as no format is strictly enforced, and any character in
 * the UTF-8 character set is allowed. As such, strings like {@code "MC|Brand"}
 * and {@code "REGISTER"} are valid channel identifiers in the legacy format.
 */
public interface LegacyChannelIdentifier extends NamespacedIdentifier {

	char SEPARATOR = '|';

	@Override
	LegacyChannelIdentifier prefixed(String prefix);

	@Override
	LegacyChannelIdentifier suffixed(String suffix);

}
