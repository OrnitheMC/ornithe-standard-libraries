package net.ornithemc.osl.registries.api.registry.sync;

import java.util.Arrays;

public class ArrayMapper implements IdMapper {

	public static <T> ArrayMapper of(T[] registry) {
		return new ArrayMapper(registry);
	}

	protected final Object[] registry;
	private final Object[] backup;

	private boolean applied;

	protected <T> ArrayMapper(T[] registry) {
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
