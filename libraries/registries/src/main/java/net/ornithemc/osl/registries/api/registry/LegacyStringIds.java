package net.ornithemc.osl.registries.api.registry;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.core.impl.util.Util;

/**
 * Utilities for converting legacy {@linkplain String} IDs to proper
 * {@linkplain NamespacedIdentifier namespaced IDs} for use in registries.
 * <p>
 * Legacy IDs were not namespaced and used PascalCase rather than snake_case.
 * Many of these IDs are very old and some use outdated names or conventions.
 * For example:
 * <li>{@code LavaSlime} is the legacy ID for {@code minecraft:magma_cube} (the
 * name changed entirely)</li>
 * <li>{@code MinecartChest} and {@code MinecartTNT} are the legacy IDs for
 * {@code minecraft:chest_minecart} and {@code minecraft:tnt_minecart} (the word
 * order changed)</li>
 * <p>
 * The various Minecraft systems switched over to namespaced identifiers in
 * different updates, and in the process the IDs were often tweaked or changed
 * to better fit new conventions. For this reason, OSL uses hard-coded
 * conversion tables for known Vanilla legacy IDs and their respective new
 * namespaced IDs.
 * <p>
 * Where content from mods is concerned however, that is not a feasible
 * approach. With that in mind, this class provides utility methods for
 * <i>general</i> conversion between legacy IDs and namespaced IDs. An attempt
 * was made to match the different conventions, but to keep the complexity down,
 * no attempt is made to change things like word order. See
 * {@link #fromIdentifier(String, String)} and {@link #toIdentifier(String)} for
 * implementation details.
 */
public final class LegacyStringIds {

	/**
	 * Converts the given {@linkplain NamespacedIdentifier namespaced ID} to a
	 * legacy format {@linkplain String} ID. See
	 * {@link #fromIdentifier(String, String)} for implementation details.
	 * 
	 * @param identifier the namespaced ID to convert.
	 * @return the legacy ID representation of the namespaced ID.
	 * 
	 * @see #fromIdentifier(String, String)
	 */
	public static String fromIdentifier(NamespacedIdentifier identifier) {
		return fromIdentifier(identifier.namespace(), identifier.identifier());
	}

	/**
	 * Converts the given {@linkplain NamespacedIdentifier namespaced ID} to a
	 * legacy format {@linkplain String} ID. Legacy IDs are not namespaced and use
	 * PascalCase rather than snake_case.
	 * <p>
	 * Namespaced IDs are converted to the format
	 * {@code <namespace>.<IdentifierInPascalCase>}, though for Minecraft IDs the
	 * namespace is dropped and the result looks like
	 * {@code <IdentifierInPascalCase>}.
	 * <p>
	 * The conversion to PascalCase works as follows:
	 * <li>All {@code _} characters are dropped.</li>
	 * <li>The first character of the string, and any character superceding a
	 * {@code _} character is converted to upper case using
	 * {@linkplain Character#toUpperCase(char)}.</li>
	 * <p>
	 * See below for some example conversions.
	 * <li>{@code minecraft:tnt_minecart} -> {@code TntMinecart}</li>
	 * <li>{@code example:chocolate_chip_cookie} ->
	 * {@code example.ChocolateChipCookie}</li>
	 * 
	 * @param namespace  the namespace of the namespaced ID to convert.
	 * @param identifier the identifier of the namespaced ID to convert.
	 * @return the legacy ID representation of the namespaced ID.
	 */
	public static String fromIdentifier(String namespace, String identifier) {
		String id = Util.snakeCaseToPascalCase(identifier);

		if (!namespace.equals(NamespacedIdentifiers.MINECRAFT_NAMESPACE)) {
			id = namespace + "." + id;
		}

		return id;
	}

	/**
	 * Converts the given legacy format {@linkplain String} ID to a proper
	 * {@linkplain NamespacedIdentifier namespaced ID}. Legacy IDs are not
	 * namespaced and use PascalCase rather than snake_case.
	 * <p>
	 * Legacy IDs are converted to the format
	 * {@code <namespace>:<id_in_snake_case>}. The namespace is parsed from the
	 * legacy ID based on the presence of a {@code .} character. If that character
	 * is not present in the legacy ID, the {@code minecraft} namespace is used
	 * instead.
	 * <p>
	 * The conversion to snake_case works as follows:
	 * <li>All characters that match {@linkplain Character#isUpperCase(char)} are
	 * converted to lower case using {@linkplain Character#toLowerCase(char)}.</li>
	 * <li>A {@code _} character is inserted before every upper case character that
	 * is not the first character of the string <b>IF</b> the preceding character is
	 * not upper case.</li>
	 * <p>
	 * See below for some example conversions.
	 * <li>{@code MinecartTNT} -> {@code minecraft:minecart_tnt}</li>
	 * <li>{@code example.ChocolateChipCookie} ->
	 * {@code example:chocolate_chip_cookie}</li>
	 * 
	 * @param id the legacy ID to convert.
	 * @return the namespaced ID representation of the legacy ID.
	 */
	public static NamespacedIdentifier toIdentifier(String id) {
		String namespace = NamespacedIdentifiers.MINECRAFT_NAMESPACE;
		String identifier = id;

		int i = id.indexOf('.');

		if (i > 0) {
			namespace = id.substring(0, i);
			identifier = id.substring(i + 1);
		}

		identifier = Util.pascalCaseToSnakeCase(identifier);

		return NamespacedIdentifiers.from(namespace, identifier);
	}
}
