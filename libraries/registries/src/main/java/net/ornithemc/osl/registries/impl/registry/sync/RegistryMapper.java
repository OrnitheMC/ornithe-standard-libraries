package net.ornithemc.osl.registries.impl.registry.sync;

import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.ResourceKey;
import net.ornithemc.osl.registries.api.registry.WritableRegistry;
import net.ornithemc.osl.registries.api.registry.sync.IdMapper;
import net.ornithemc.osl.registries.api.registry.sync.RegistryMappings;
import net.ornithemc.osl.registries.impl.registry.ClearableRegistry;
import net.ornithemc.osl.registries.impl.registry.SimpleRegistry;

public class RegistryMapper implements IdMapper {

	public static RegistryMapper of(Registry<?> registry) {
		if (registry instanceof ClearableRegistry) {
			return new RegistryMapper(registry);
		} else {
			throw new IllegalArgumentException("cannot create mapper for non-clearable registry " + registry.identifier());
		}
	}

	private final ClearableRegistry<Object> registry;
	private final ClearableRegistry<Object> backup;

	private boolean applied;

	@SuppressWarnings("unchecked")
	private RegistryMapper(Registry<?> registry) {
		this.registry = (ClearableRegistry<Object>) registry;
		this.backup = new SimpleRegistry<>(registry.identifier());
	}

	@Override
	public void apply(RegistryMappings mappings) {
		this.backup.clear();

		for (Object value : this.registry) {
			ResourceKey<Object> key = this.registry.getKey(value);
			int id = this.registry.getId(value);

			register(this.backup, id, key, value);
		}

		this.registry.clear();

		for (Object value : this.backup) {
			ResourceKey<Object> key = this.backup.getKey(value);
			int id = mappings.remap(key);

			if (id >= 0) {
				register(this.registry, id, key, value);
			}
		}

		this.registry.freeze();

		this.applied = true;
	}

	@Override
	public void undo(RegistryMappings mappings) {
		if (this.applied) {
			this.registry.clear();

			for (Object value : this.backup) {
				int id = this.backup.getId(value);
				ResourceKey<Object> key = this.backup.getKey(value);

				register(this.registry, id, key, value);
			}

			this.registry.freeze();
		}

		this.applied = false;
	}

	@SuppressWarnings("deprecation")
	private static void register(WritableRegistry<Object> registry, int id, ResourceKey<Object> key, Object value) {
		registry.register(id, key, value);
	}
}
