package net.ornithemc.osl.registries.api.registry.sync;

import java.util.ArrayList;
import java.util.List;

public class ListMapper implements IdMapper {

	public static ListMapper of(List<?> registry) {
		return new ListMapper(registry);
	}
	
	private final List<Object> registry;
	private final List<Object> backup;
	
	private boolean applied;

	@SuppressWarnings("unchecked")
	private ListMapper(List<?> registry) {
		this.registry = (List<Object>) registry;
		this.backup = new ArrayList<>(registry.size());
	}

	@Override
	public void apply(RegistryMappings mappings) {
		this.backup.clear();
		this.backup.addAll(this.registry);
		
		this.registry.clear();
		
		for (int oldId = 0; oldId < this.backup.size(); oldId++) {
			Object value = this.backup.get(oldId);

			if (value != null) {
				int newId = mappings.remap(oldId);

				if (newId >= 0) {
					while (newId >= this.registry.size()) {
						this.registry.add(null);
					}

					this.registry.set(newId, value);
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
