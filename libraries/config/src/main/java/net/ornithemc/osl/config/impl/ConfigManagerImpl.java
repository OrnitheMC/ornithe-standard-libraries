package net.ornithemc.osl.config.impl;

import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;

import net.ornithemc.osl.config.api.ConfigScope;
import net.ornithemc.osl.config.api.LoadingPhase;
import net.ornithemc.osl.config.api.config.Config;

public final class ConfigManagerImpl {

	public static void register(Config config) {
		forInstance(config.getScope(), manager -> manager.register(config));
	}

	public static void load(ConfigScope scope, LoadingPhase phase) {
		forInstance(scope, manager -> manager.load(phase));
	}

	public static void unload(ConfigScope scope) {
		forInstance(scope, manager -> manager.unload());
	}

	public static void save(Config config) {
		forInstance(config.getScope(), manager -> manager.save(config));
	}

	private static void forInstance(ConfigScope scope, Consumer<ScopedConfigManager> action) {
		ScopedConfigManager manager = MANAGERS.get(scope);

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

	private static final Map<ConfigScope, ScopedConfigManager> MANAGERS = new EnumMap<>(ConfigScope.class);

	public static void setUp(ConfigScope scope, Path rootDir) {
		if (MANAGERS.containsKey(scope)) {
			throw new IllegalStateException("tried to set up a config manager for the " + scope.name().toLowerCase() + " scope while it was already set up!");
		}

		MANAGERS.put(scope, new ScopedConfigManager(scope, rootDir));
		InternalConfigEvents.CONFIG_MANAGER_READY.invoker().accept(scope);
	}

	public static void destroy(ConfigScope scope) {
		if (!MANAGERS.containsKey(scope)) {
			throw new IllegalStateException("tried to destroy the config manager for the " + scope.name().toLowerCase() + " scope while it was not set up!");
		}

		MANAGERS.remove(scope);
	}
}
