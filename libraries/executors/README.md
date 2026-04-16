# Executors API

The Executors API provides an interface for main thread and background thread executors.

## Main Thread Executors

The `Minecraft` and `MinecraftServer` game instances are made to implement `java.util.concurrent.Executor`.
Any tasks passed to the `execute` method will be executed on the main game thread. If `execute` is called from a different thread,
the task will be added to a queue. At the beginning of every tick, the game instance will run any pending tasks from that queue.

```java
Minecraft minecraft = ...;

minecraft.execute(() -> {
	// this code is guaranteed to be run on the main client thread!
	...
});
```

The game instance can be acquired through the Lifecycle Events API.

```java
MinecraftServerInstance.get().execute(() -> {
	// this code is guaranteed to be run on the main server thread!
	...
});
```

## Background Executor

A background executor service is provided through which tasks can be run *off* the main thread. By default, this executor service will use
up to the number available threads on your machine, but this can be configured through the `max.bg.threads` system property.
The background executor can be acquired through the `net.ornithemc.executors.api.BackgroundExecutor` class.

```java
BackgroundExecutor.get().execute(() -> {
	// this code is guaranteed NOT to run on the main game thread!
	...
});
```
