package net.ornithemc.osl.resource.loader.impl.resource.reload;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;

import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;
import net.ornithemc.osl.resource.loader.api.resource.reload.ResourceReloader.SharedState;

public class SharedReloaderState implements SharedState {

	private final ResourceManager resourceManager;
	private final Map<SharedState.Key<?>, Object> state;

	public SharedReloaderState(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
		this.state = new IdentityHashMap<>();
	}

	@Override
	public ResourceManager resourceManager() {
		return this.resourceManager;
	}

	@Override
	public <T> void set(Key<T> key, T value) {
		this.state.put(key, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Key<T> key) {
		return (T) Objects.requireNonNull(this.state.get(key));
	}
}
