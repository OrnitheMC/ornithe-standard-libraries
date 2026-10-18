package net.ornithemc.osl.resource.loader.impl.adapter;

import net.minecraft.resource.manager.ResourceReloadListener;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

public interface ResourceReloaderIdentifier {

	NamespacedIdentifier identify(ResourceReloadListener reloader);

}
