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
Mods can register network listeners through the `registerListener` and `registerListenerAsync` methods,
allowing them to receive data through specific channels. Sending data is done through the `send` methods.

Each custom payload is tied to a channel. Only connections that have listeners on that channel will receive the payload, and on the receiving end, the payload will only be handled by the listener on that channel. In Minecraft versions 1.13-pre2 and below, a channel can be any `String` of length 20 or less. In Minecraft versions 1.13-pre4 and above, a channel can be any valid `Identifier`. For `String` channels, the convention is `<(abbreviated) mod id>|<payload id>` (e.g. `Example|Cookie`), while for `Identifier` channels, the convention is `<mod id>:<payload id>` (e.g. `example:cookie`).

For ease of use data can be wrapped in custom payload objects. These must implement the `CustomPayload` interface
and must have a public construcor without parameters. An example can be seen below.

```java
package com.example;

import java.io.IOException;

import net.minecraft.network.PacketByteBuf;

import net.ornithemc.osl.networking.api.CustomPayload;

public class CookiePayload implements CustomPayload {

	public Cookie cookie;

	public CookiePayload() {
	}

	public CookiePayload(Cookie cookie) {
		this.cookie = cookie;
	}

	@Override
	public void read(PacketByteBuf buffer) throws IOException {
		// deserialize data
	}

	@Override
	public void write(PacketByteBuf buffer) throws IOException {
		// serialize data
	}
}
```

Listeners for these custom payload objects can be registered as follows:

```java
package com.example;

import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.networking.api.client.ClientPlayNetworking;

public class ExampleInitializer implements ModInitializer {

	@Override
	public void init() {
		ClientPlayNetworking.registerListener("Example|Cookie", CookiePayload::new, (minecraft, handler, payload) -> {
			// handle custom payload
		});
	}
}
```
