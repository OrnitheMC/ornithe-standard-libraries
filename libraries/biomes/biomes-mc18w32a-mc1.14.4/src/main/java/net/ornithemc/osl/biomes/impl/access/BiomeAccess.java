package net.ornithemc.osl.biomes.impl.access;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

public interface BiomeAccess {

	default NamespacedIdentifier osl$biomes$getParentIdentifier() {
		throw new AbstractMethodError();
	}
}
