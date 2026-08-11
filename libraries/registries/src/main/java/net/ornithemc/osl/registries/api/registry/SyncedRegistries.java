package net.ornithemc.osl.registries.api.registry;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.registries.api.registry.sync.IdFixer;
import net.ornithemc.osl.registries.api.registry.sync.IdMapper;
import net.ornithemc.osl.registries.impl.registry.SyncedRegistriesImpl;

/**
 * Utility class that serves as the public API for Registry Sync. Registry Sync
 * is an important part of modding new content into the game. It ensures that
 * the client and server agree on what content is present and how to serialize
 * it. It is also responsible for synchronizing content across game sessions.
 * 
 * <p>
 * The reason this is necessary is that the game uses numerical IDs to serialize
 * content. This makes serialization for efficient, reducing network traffic and
 * world save size. The issue is that these IDs are automatically generated,
 * usually based on the order in which content is registered. This means that
 * the IDs for mod content is dependent on the mods list and the mod loading
 * order. If these are different between the server and client, or across game
 * sessions, it can lead to bugs or even crashes and corrupted world saves.
 * 
 * <p>
 * To prevent these issues, the Registry Sync API takes on the responsibility of
 * synchronizing registry data between the client and server and across game
 * sessions. This is done by remapping the numerical IDs of registry contents,
 * and fixing up any resources that depend on these numerical IDs.
 * 
 * <p>
 * Registries are not synchronized by default, and must be registered to do so.
 * The API also accepts custom ID mappers and fixers for remapping other
 * resources, such as arrays, lists,maps that use the numerical IDs as indices
 * or keys, and fixing up other objects that store the numerical IDs.
 * 
 * @see IdMapper
 * @see IdFixer
 */
public final class SyncedRegistries {

	/**
	 * Registers the registry identified by the given {@linkplain ResourceKey
	 * resource key} to be synchronized between the client and server and across
	 * game sessions. A registry mapper for the registry will automatically be
	 * registered as well.
	 */
	public static void register(ResourceKey<? extends Registry<?>> registry) {
		SyncedRegistriesImpl.register(registry);
	}

	/**
	 * Registers the given ID mapper to the specified synchronized registry with
	 * the given {@linkplain NamespacedIdentifier} namespaced identifier.
	 * 
	 * @param registry   the {@linkplain ResourceKey resource key} that identifies
	 *                   the registry.
	 * @param identifier the {@linkplain NamespacedIdentifier} identifier for the
	 *                   ID mapper.
	 * @param mapper     the ID mapper to be registered.
	 */
	public static void registerMapper(ResourceKey<? extends Registry<?>> registry, NamespacedIdentifier identifier, IdMapper mapper) {
		SyncedRegistriesImpl.registerMapper(registry, identifier, mapper);
	}

	/**
	 * Registers the given ID fixer to the specified synchronized registry with
	 * the given {@linkplain NamespacedIdentifier} namespaced identifier.
	 * 
	 * @param registry   the {@linkplain ResourceKey resource key} that identifies
	 *                   the registry.
	 * @param identifier the {@linkplain NamespacedIdentifier} identifier for the
	 *                   ID fixer.
	 * @param fixer      the ID fixer to be registered.
	 */
	public static void registerFixer(ResourceKey<? extends Registry<?>> registry, NamespacedIdentifier identifier, IdFixer fixer) {
		SyncedRegistriesImpl.registerFixer(registry, identifier, fixer);
	}
}
