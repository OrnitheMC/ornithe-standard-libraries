package net.ornithemc.osl.registries.api.registry.sync;

import java.util.Arrays;

/**
 * An {@linkplain IdMapper} implementation for int arrays ({@code int[]} where
 * IDs are used as indices to the array. The values are moved from the old index
 * (the old ID) to the new index (the new ID) within the same array. Since
 * {@code int} is a primitive type, {@code 0} is used as the default or "empty"
 * value.
 */
public class IntArrayMapper implements IdMapper {

	public static IntArrayMapper of(int[] registry) {
		return new IntArrayMapper(registry);
	}

	private final int[] registry;
	private final int[] backup;

	private boolean applied;

	private <T> IntArrayMapper(int[] registry) {
		this.registry = registry;
		this.backup = new int[registry.length];
	}

	@Override
	public void apply(RegistryMappings mappings) {
		Arrays.fill(this.backup, 0);
		System.arraycopy(this.registry, 0, this.backup, 0, this.backup.length);

		Arrays.fill(this.registry, 0);

		for (int oldId = 0; oldId < this.backup.length; oldId++) {
			int newId = mappings.remap(oldId);

			if (newId >= 0) {
				this.registry[newId] = this.backup[oldId];
			}
		}

		this.applied = true;
	}

	@Override
	public void undo(RegistryMappings mappings) {
		if (this.applied) {
			Arrays.fill(this.registry, 0);
			System.arraycopy(this.backup, 0, this.registry, 0, this.registry.length);
		}

		this.applied = false;
	}
}
