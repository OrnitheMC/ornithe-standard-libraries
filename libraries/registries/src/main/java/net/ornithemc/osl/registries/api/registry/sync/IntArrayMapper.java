package net.ornithemc.osl.registries.api.registry.sync;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * An {@linkplain IdMapper} implementation for int arrays ({@code int[]} where
 * IDs are used as indices to the array. The values are moved from the old index
 * (the old ID) to the new index (the new ID) within the same array. Since
 * {@code int} is a primitive type, {@code 0} is used as the default or "empty"
 * value.
 * 
 * @see DynamicIntArray
 */
public class IntArrayMapper implements IdMapper {

	public static IntArrayMapper of(Supplier<int[]> getter, Consumer<int[]> setter) {
		return new IntArrayMapper(DynamicIntArray.of(getter, setter));
	}

	public static IntArrayMapper of(DynamicIntArray registry) {
		return new IntArrayMapper(registry);
	}

	private final DynamicIntArray registry;
	private final DynamicIntArray backup;

	private boolean applied;

	private IntArrayMapper(DynamicIntArray registry) {
		this.registry = registry;
		this.backup = DynamicIntArray.of(registry.capacity());
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
