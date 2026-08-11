package net.ornithemc.osl.registries.api.registry.sync;

import java.util.Arrays;

/**
 * An {@linkplain IdMapper} implementation for object arrays ({@code T[]} where
 * IDs are used as indices to the array. The values are moved from the old index
 * (the old ID) to the new index (the new ID) within the same array.
 * {@code null} is used as the default or "empty" value.
 */
public class ObjectArrayMapper implements IdMapper {

	public static <T> ObjectArrayMapper of(T[] registry) {
		return new ObjectArrayMapper(registry);
	}

	private final Object[] registry;
	private final Object[] backup;

	private boolean applied;

	private <T> ObjectArrayMapper(T[] registry) {
		this.registry = (Object[]) registry;
		this.backup = new Object[registry.length];
	}

	@Override
	public void apply(RegistryMappings mappings) {
		Arrays.fill(this.backup, null);
		System.arraycopy(this.registry, 0, this.backup, 0, this.backup.length);

		Arrays.fill(this.registry, null);

		for (int oldId = 0; oldId < this.backup.length; oldId++) {
			Object value = this.backup[oldId];

			if (value != null) {
				int newId = mappings.remap(oldId);

				if (newId >= 0) {
					this.registry[newId] = value;
				}
			}
		}

		this.applied = true;
	}

	@Override
	public void undo(RegistryMappings mappings) {
		if (this.applied) {
			Arrays.fill(this.registry, null);
			System.arraycopy(this.backup, 0, this.registry, 0, this.registry.length);
		}

		this.applied = false;
	}
}
