package net.ornithemc.osl.registries.api.registry.sync;

import java.util.Arrays;

public class BooleanArrayMapper implements IdMapper {

	public static BooleanArrayMapper of(boolean[] registry) {
		return new BooleanArrayMapper(registry);
	}

	private final boolean[] registry;
	private final boolean[] backup;

	private boolean applied;

	private <T> BooleanArrayMapper(boolean[] registry) {
		this.registry = registry;
		this.backup = new boolean[registry.length];
	}

	@Override
	public void apply(RegistryMappings mappings) {
		Arrays.fill(this.backup, false);
		System.arraycopy(this.registry, 0, this.backup, 0, this.backup.length);

		Arrays.fill(this.registry, false);

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
			Arrays.fill(this.registry, false);
			System.arraycopy(this.backup, 0, this.registry, 0, this.registry.length);
		}

		this.applied = false;
	}
}
