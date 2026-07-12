package net.ornithemc.osl.resource.loader.impl.resource;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

/**
 * This interface is used in some implementations to access
 * the location field of Vanilla's SimpleResource class.
 */
public interface ResourceLocationAccess {

	NamespacedIdentifier osl$resource_loader$resourceLocation();

}
