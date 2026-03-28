# Resource Loader API

The Resource Loader API allows mods to load their own resources into the game. On top of that, it provides its own access layer for resource packs and resource management.

## Loading Your Mod's Resources

Mods do not need to manually load their resources into the game. OSL wraps each mod's resources in a resource pack and loads all those resource packs in under the `fabric-mod-resources` pack. You can check that this is working by opening the Resource Packs screen and checking that `fabric-mod-resources` appears in the selected packs list.

## Resource Loader Events

The API provides several events for both the client and server, for initializing resource management and listening for resource reloads. Event listeners for these events should be registered in your mod's entrypoint.

```java
package com.example;

import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.resource.loader.api.client.ClientResourceLoaderEvents;

public class ExampleInitializer implements ClientModInitializer {

	@Override
	public void initClient() {
		ClientResourceLoaderEvents.INIT_RESOURCE_PACK_REPOSITORY.register(packRepository -> {
			// register resource pack repository sources here
		});
		ClientResourceLoaderEvents.INIT_RESOURCE_MANAGER.register(resourceManager -> {
			// register resource reloaders and reload listeners here
		});
		ClientResourceLoaderEvents.START_RESOURCE_PACKS_RELOAD.register(() -> {
			// this code is run before the resource pack repository is reloaded
		});
		ClientResourceLoaderEvents.START_RESOURCE_RELOAD.register(() -> {
			// this code is run before a resource reload is started
		});
	}
}
```

## Bundled Mod Resource Packs

The API provides a way for mods to provide multiple bundled resource packs. You can do this by using sub-directories in your project resources. This is useful if you want to separate client assets from server data, or if you want to organize resources into bundles for specific feature sets or project modules.

To ensure these resources are loaded properly, register them in your mod's entrypoint.

```java
package com.example;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackRepository;

public class ExampleInitializer implements ClientModInitializer {

	private static final ModContainer MOD = FabricLoader.getInstance().getModContainer("example").get()

	@Override
	public void initClient() {
		ResourcePackRepository.registerBundledModResources("cookie-assets", "Cookie Assets", MOD, "client/cookies/")
	}
}
```

## Resource Pack Repositories

The client and server each have their own resource pack repositories. You can add a custom source to load in custom resource packs.

```java
package com.example;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.resource.loader.api.client.ClientResourceLoaderEvents;

public class ExampleInitializer implements ClientModInitializer {

	private static final ModContainer MOD = FabricLoader.getInstance().getModContainer("example").get()

	@Override
	public void initClient() {
		ClientResourceLoaderEvents.INIT_RESOURCE_PACK_REPOSITORY.register(packRepository -> {
			packRepository.addSource(new ExampleRepositorySource());
		});
	}
}
```

```java
package com.example;

import java.util.function.Consumer;

import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackRepository;

public class ExampleRepositorySource implements ResourcePackRepository.Source {

	@Override
	public void loadResourcePacks(Consumer<ResourcePack> consumer) {
		// Load your custom resource packs here.
		// These can be virtual resource packs, or packs loaded
		// from a special file or directory.
		...
	}
}
```


## Resource Management

The client and server each has their own resource manager. You can use this to access resources and register resource reloaders or reload listeners.


```java
ResourceManager resourceManager = ResourceManager.client();

// Get resource as an InputStream using a direct String path.
// Use this to access Vanilla resources in versions before 1.6.
// For custom resources you add, it is recommended to lay them
// out in namespaced directories like Vanilla in 1.6 and later.
resourceManager.getResource("/path/to/resource");

// Get resource as an Optional<Resource> using a namespaced location.
// Use this to access Vanilla resources in versions 1.6 and later.
// It is recommended to lay out custom resources in namespaced
// directories for improved compatibility and extra features.
resourceManager.getResource(NamespacedIdentifiers.from("example", "path/to/resource"));

// Get all resources as a Map<NamespacedIdentifier, Resource>
// in the specified directory that match the specified filter.
// NOTE: the specified directory is not resolved from the root
// of the resource packs, but from the namespaced directories!
// For example: a file at /directory/to/resources/example.json
// will NOT be found, but
// a file at `/assets/example/directory/to/resources/example.json
// WILL be found.`
resourceManager.findResources("directory/to/resources/", location -> location.path().endsWith(".json"));
```

```java
package com.example;

import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.resource.loader.api.client.ClientResourceLoaderEvents;

public class ExampleInitializer implements ClientModInitializer {

	@Override
	public void initClient() {
		ClientResourceLoaderEvents.INIT_RESOURCE_MANAGER.register(resourceManager -> {
			resourceManager.addReloader(new CookiesManager());
			resourceManager.addReloader(new ExampleReloadListener());
		});
	}
}
```

```java
package com.example;

import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;
import net.ornithemc.osl.resource.loader.api.resource.reload.SimpleResourceReloader;

public class CookiesManager implements SimpleResourceReloader<Cookies> {

	private Cookies cookies;

	@Override
	public Cookies reloadResources(ResourceManager manager) {
		// Load resources from the resource manager here, parse them
		// as necessary, and package them up. This code may not run
		// on the main game thread, so it is imperative that you do
		// not modify the world or render state here!
		Cookies cookies = ...
		return cookies;
	}

	@Override
	public void applyResources(Cookies cookies, ResourceManager manager) {
		// Use the resources you loaded, and parsed above to modify
		// the world or render state as necessary. 
		this.cookies = cookies;
	}
}
```

```java
package com.example;

import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;
import net.ornithemc.osl.resource.loader.api.resource.reload.ResourceReloadListener;

public class ExampleReloadListener implements ResourceReloadListener {

	@Override
	public void resourcesReloaded(ResourceManager manager) {
		// 
		...
	}
}
```
