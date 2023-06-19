package net.ornithemc.osl.config.api;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;

import org.quiltmc.loader.api.QuiltLoader;

import net.ornithemc.osl.config.api.config.Config;
import net.ornithemc.osl.config.impl.ConfigManagerImpl;
import net.ornithemc.osl.config.impl.InternalConfigEvents;

public final class ConfigManager {

	public static void register(Config config) {
		forInstance(config.getScope(), manager -> manager.register(config));
	}

	public static void load(ConfigScope scope, LoadingPhase phase) {
		forInstance(scope, manager -> manager.load(phase));
	}

	public static void unload(ConfigScope scope) {
		forInstance(scope, manager -> manager.unload());
	}

	public static void save(ConfigScope scope) {
		forInstance(scope, manager -> manager.save());
	}

	private static final Map<ConfigScope, ConfigManagerImpl> MANAGERS = new EnumMap<>(ConfigScope.class);

	public static void setUp(ConfigManagerImpl manager) {
		ConfigScope scope = manager.getScope();

		if (MANAGERS.containsKey(scope)) {
			throw new IllegalStateException("tried to set up a config manager for the " + scope.name().toLowerCase() + " scope while it was already set up!");
		}
		if (!isAllowed(manager.getClass())) {
			throw new IllegalStateException("tried to set up illegal config manager!");
		}

		MANAGERS.put(scope, manager);
		InternalConfigEvents.CONFIG_MANAGER_READY.invoker().accept(scope);
	}

	public static void destroy(ConfigManagerImpl manager) {
		ConfigScope scope = manager.getScope();

		if (!MANAGERS.containsKey(scope)) {
			throw new IllegalStateException("tried to destroy the config manager for the " + scope.name().toLowerCase() + " scope while it was not set up!");
		}
		if (manager != MANAGERS.get(scope)) {
			throw new IllegalStateException("tried to destroy a config manager that was not set up!");
		}

		MANAGERS.remove(scope, manager);
	}

	private static boolean isAllowed(Class<? extends ConfigManagerImpl> type) {
		return type.isAssignableFrom(ConfigManagerImpl.class) && QuiltLoader.getModContainer(type) == QuiltLoader.getModContainer(ConfigManager.class);
	}

	private static void forInstance(ConfigScope scope, Consumer<ConfigManagerImpl> action) {
		ConfigManagerImpl manager = MANAGERS.get(scope);

		if (manager == null) {
			InternalConfigEvents.CONFIG_MANAGER_READY.register(readyScope -> {
				if (readyScope == scope) {
					forInstance(scope, action);
				}
			});
		} else {
			action.accept(manager);
		}
	}
}
