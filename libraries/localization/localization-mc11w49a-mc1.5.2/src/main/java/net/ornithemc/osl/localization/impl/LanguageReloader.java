package net.ornithemc.osl.localization.impl;

import net.ornithemc.osl.resource.loader.api.client.ClientResourceLoaderEvents;
import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;
import net.ornithemc.osl.resource.loader.api.resource.reload.ResourceReloadListener;

public class LanguageReloader implements ResourceReloadListener {

	public static void init() {
		ClientResourceLoaderEvents.INIT_RESOURCE_MANAGER.register(resourceManager -> {
			resourceManager.addReloader(new LanguageReloader());
		});
		ClientResourceLoaderEvents.START_RESOURCE_RELOAD.register((resourceManager, context) -> {
			Localization.getLanguageManager().reload(context.resourcePacks());
		});
	}

	@Override
	public void resourcesReloaded(ResourceManager resourceManager) {
		Localization.getLanguageManager().reloadLocale(resourceManager);
	}
}
