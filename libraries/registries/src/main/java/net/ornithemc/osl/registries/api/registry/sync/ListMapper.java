package net.ornithemc.osl.registries.api.registry.sync;

import java.util.ArrayList;
import java.util.Collections;
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
		Collections.fill(this.backup, null);
		Collections.copy(this.backup, this.registry);
		
		Collections.fill(this.registry, null);
		
		for (int oldId = 0; oldId < this.backup.size(); oldId++) {
			Object value = this.backup.get(oldId);

			if (value != null) {
				int newId = mappings.remap(oldId);

				if (newId >= 0) {
					this.registry.set(newId, this.backup.get(oldId));
				}
			}
		}
		
		this.applied = true;
	}

	@Override
	public void undo(RegistryMappings mappings) {
		if (this.applied) {
			Collections.fill(this.registry, null);
			Collections.copy(this.registry, this.backup);
		}
		
		this.applied = false;
	}
}
