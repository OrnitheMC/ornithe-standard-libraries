package net.ornithemc.osl.resource.loader.api.resource.reload;

import java.util.concurrent.CompletableFuture;

public interface ReloadStep {

	<T> CompletableFuture<T> await(T object);

}
