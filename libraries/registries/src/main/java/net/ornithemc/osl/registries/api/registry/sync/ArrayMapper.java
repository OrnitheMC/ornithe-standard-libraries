package net.ornithemc.osl.registries.api.registry.sync;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * An {@linkplain IdMapper} implementation for object arrays ({@code T[]} where
 * IDs are used as indices to the array. The values are moved from the old index
 * (the old ID) to the new index (the new ID) within the same array.
 * {@code null} is used as the default or "empty" value.
 * 
 * @see DynamicArray
 */
public class ArrayMapper implements IdMapper {

	public static <T> ArrayMapper of(Supplier<T[]> getter, Consumer<T[]> setter) {
		return new ArrayMapper(DynamicArray.of(getter, setter));
	}

	public static ArrayMapper of(DynamicArray<?> registry) {
		return new ArrayMapper(registry);
	}

	private final DynamicArray<Object> registry;
	private final DynamicArray<Object> backup;

	private boolean applied;

	@SuppressWarnings("unchecked")
	private ArrayMapper(DynamicArray<?> registry) {
		this.registry = (DynamicArray<Object>) registry;
		this.backup = DynamicArray.of(registry.capacity());
	}

	@Override
	public void apply(RegistryMappings mappings) {
		this.backup.clear();
		this.backup.addAll(this.registry);

		this.registry.clear();

		for (int oldId = 0; oldId < this.backup.length(); oldId++) {
			Object value = this.backup.get(oldId);

			if (value != null) {
				int newId = mappings.remap(oldId);

				if (newId >= 0) {
					this.registry.add(newId, value);
				}
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
