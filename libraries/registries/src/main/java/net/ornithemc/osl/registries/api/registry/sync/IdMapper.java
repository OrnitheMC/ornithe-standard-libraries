package net.ornithemc.osl.registries.api.registry.sync;

/**
 * An ID mapper is used to remap numerical IDs from old to new values and vice
 * versa. This could be arrays, lists, or maps that use the IDs as indices or
 * keys, or it could be objects that store the IDs in a field. Some examples of
 * objects that require an ID mapper are:
 * <li>The {@code Block[] BY_ID} field in {@code Block} 1.6.4 and below, which
 *     uses block IDs as indices to the array.</li>
 * <li>The {@code Int2ObjectMap<BakedModel>} field in {@code ItemModelShaper}
 *     which uses item IDs as keys to the map.</li>
 * <li>The {@code ItemStat} instances in 1.6.4 and below, which store the item
 *     ID as a field.</li>
 * 
 * <p>
 * The Registries API provides several implementations of this interface in this
 * package which you can use.
 * 
 * @see BooleanArrayMapper
 * @see Int2ObjectMapMapper
 * @see IntArrayMapper
 * @see IntegerMapMapper
 * @see ListMapper
 * @see ObjectArrayMapper
 */
public interface IdMapper {

	/**
	 * Apply the given ID mappings to the bound registry or map.
	 */
	void apply(RegistryMappings mappings);

	/**
	 * Undo the given ID mappings and reset the bound registry or map.
	 */
	void undo(RegistryMappings mappings);

}
