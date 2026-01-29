# Networking API

The Networking API provides a framework for client-server communication.

## Events

The API provides a several events to track the lifecycle of a client-server connection.
`ClientConnectionEvents` has events for the client side, whereas `ServerConnectionEvents`
has events for the server side. The `LOGIN` event is fired after a successful login occurs,
the `PLAY_READY` event is fired once channel registration is complete and data can safely
be sent over the connection, and the `DISCONNECT` event is fired when a player disconnects
from the server.

In 1.3 and above, singleplayer worlds are run on an integrated server, and these events
are also fired for connections to integrated servers.

## Networking

Sending and receiving data is done through the `ClientPlayNetworking` and `ServerPlayNetworking` classes.
Mods can register packet listeners through the `registerListener` methods, and send data through the `send` methods.

Custom payloads are sent over specific channels. Channels are namespaced identifiers, used to identify the payload being sent or received.
Channels identifiers should be constructed through the `ChannelIdentifieres` class. The convention is to use your mod id as the namespace,
and snake case for the identifier.

```java
public static final NamespacedIdentifier COOKIE_CHANNEL = ChannelIdentifiers.from("example", "cookie");
```

You are expected to register your channels through the `ChannelRegistry`.

```java
ChannelRegistry.register(COOKIE_CHANNEL);
```

You are expected to register your packet listeners in your mod initializer through `ClientPlayNetworking` and `ServerPlayNetworking`.

```java
ClientPlayNetworking.registerListener(COOKIE_CHANNEL, (context, buffer) -> { });
```

For ease of use data can be wrapped in custom payload objects.
These must implement the `CustomPayload` interface and must have a public constructor without parameters.
An example can be seen below.

```java
package com.example;

import java.io.IOException;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.networking.api.ChannelIdentifiers;
import net.ornithemc.osl.networking.api.ChannelRegistry;
import net.ornithemc.osl.networking.api.CustomPayload;
import net.ornithemc.osl.networking.api.PacketBuffer;

public class CookiePayload implements CustomPayload {

	public static final NamespacedIdentifier CHANNEL = ChannelRegistry.register(ChannelIdentifiers.from("example", "cookie"));

	public Cookie cookie;

	public CookiePayload() {
	}

	public CookiePayload(Cookie cookie) {
		this.cookie = cookie;
	}

	@Override
	public void read(PacketBuffer buffer) throws IOException {
		// deserialize data
	}

	@Override
	public void write(PacketBuffer buffer) throws IOException {
		// serialize data
	}
}
```

A basic networking setup might look as follows.


```java
package com.example;

import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.networking.api.client.ClientPlayNetworking;

public class ExampleInitializer implements ClientModInitializer {

	@Override
	public void initClient() {
		ClientPlayNetworking.registerListener(CookiePayload.CHANNEL, CookiePayload::new, (context, payload) -> {
			// ensure this listener is running on the main thread
			context.ensureOnMainThread();

			// handle custom payload
		});
	}
}
```
