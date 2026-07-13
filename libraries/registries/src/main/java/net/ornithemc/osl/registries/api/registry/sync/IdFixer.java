package net.ornithemc.osl.registries.api.registry.sync;

/**
 * An ID fixer is used to fix up numerical IDs after registries have been
 * remapped. In most cases, a {@linkplain IdMapper ID mapper} is necessary, but
 * in some cases, the IDs can be inferred or generated, and an ID mapper is
 * either overkill or cannot be used at all. In these cases, an ID fixer is the
 * right solution. Some examples of ID fixers are:
 * <li>The {@code Id2ObjectBiMap<BlockState>} block state registry. This is
 *     either automatically filled based on the block registry iteration order,
 *     or the IDs are generated based on block IDs and metadata values.</li>
 * <li>The {@code int id} fields in {@code Block} and {@code Item} in 1.6.4 and
 *     below. Since the {@code Block[] BY_ID} and {@code Item[] BY_ID} arrays
 *     are remapped first, the IDs can be inferred from the indices so an ID
 *     mapper is not needed.</li>
 */
public interface IdFixer {

	/**
	 * Fix up IDs for the bound registry or map.
	 */
	void apply();

}
