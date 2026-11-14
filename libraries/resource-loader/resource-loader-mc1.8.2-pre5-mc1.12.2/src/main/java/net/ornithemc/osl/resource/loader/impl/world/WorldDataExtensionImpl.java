package net.ornithemc.osl.resource.loader.impl.world;

import java.util.Set;

import net.ornithemc.osl.resource.loader.api.world.WorldDataExtension;

public interface WorldDataExtensionImpl extends WorldDataExtension {

	@Override
	default Set<String> getDisabledDataPacks() {
		throw new AbstractMethodError();
	}

	@Override
	default Set<String> getEnabledDataPacks() {
		throw new AbstractMethodError();
	}
}
