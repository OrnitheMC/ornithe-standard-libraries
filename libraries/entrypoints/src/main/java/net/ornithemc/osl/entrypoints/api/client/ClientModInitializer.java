package net.ornithemc.osl.entrypoints.api.client;

public interface ClientModInitializer {

	String ENTRYPOINT_KEY = "client-init";

	void initClient();

}
