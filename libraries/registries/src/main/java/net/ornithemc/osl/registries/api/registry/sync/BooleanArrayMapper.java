package net.ornithemc.osl.registries.api.registry.sync;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * An {@linkplain IdMapper} implementation for boolean arrays ({@code boolean[]}
 * where IDs are used as indices to the array. The values are moved from the old
 * index (the old ID) to the new index (the new ID) within the same array. Since
 * {@code boolean} is a primitive type, {@code false} is used as the default or
 * "empty" value.
 * 
 * @see DynamicBooleanArray
 */
public class BooleanArrayMapper implements IdMapper {

	public static BooleanArrayMapper of(Supplier<boolean[]> getter, Consumer<boolean[]> setter) {
		return new BooleanArrayMapper(DynamicBooleanArray.of(getter, setter));
	}

	public static BooleanArrayMapper of(DynamicBooleanArray registry) {
		return new BooleanArrayMapper(registry);
	}

	private final DynamicBooleanArray registry;
	private final DynamicBooleanArray backup;

	private boolean applied;

	private BooleanArrayMapper(DynamicBooleanArray registry) {
		this.registry = registry;
		this.backup = DynamicBooleanArray.of(registry.capacity());
	}

	@Override
	public void apply(RegistryMappings mappings) {
		this.backup.clear();
		this.backup.addAll(this.registry);

		this.registry.clear();

		for (int oldId = 0; oldId < this.backup.length(); oldId++) {
			int newId = mappings.remap(oldId);

			if (newId >= 0) {
				this.registry.add(newId, this.backup.get(oldId));
			}
		}

		this.applied = true;
	}

	@Override
	public void undo(RegistryMappings mappings) {
		if (this.applied) {
			this.registry.clear();
			this.registry.addAll(this.backup);
		}

		this.applied = false;
	}
}
