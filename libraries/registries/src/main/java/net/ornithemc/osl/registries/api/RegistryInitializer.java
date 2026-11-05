package net.ornithemc.osl.registries.api;

public interface RegistryInitializer {

	String ENTRYPOINT_KEY = "registry-init";

	void initRegistries();

}
