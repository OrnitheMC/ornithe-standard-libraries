package net.ornithemc.osl.entrypoints.api.server;

public interface ServerModInitializer {

	String ENTRYPOINT_KEY = "server-init";

	void initServer();

}
