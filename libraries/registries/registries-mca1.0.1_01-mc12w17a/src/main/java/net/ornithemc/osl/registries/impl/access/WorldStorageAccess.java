package net.ornithemc.osl.registries.impl.access;

import java.io.IOException;

public interface WorldStorageAccess {

	void osl$registries$loadRegistryMappings() throws IOException;

	void osl$registries$saveRegistryMappings() throws IOException;

}
