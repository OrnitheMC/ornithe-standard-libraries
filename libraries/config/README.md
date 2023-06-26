# Config API

The Config API provides a framework for building and storing configs for your mods.

## Creating a config

The easiest way to create a config is to extend the `net.ornithemc.osl.config.api.config.BaseConfig` class:

```java
package com.example;

import net.ornithemc.osl.config.api.ConfigScope;
import net.ornithemc.osl.config.api.LoadingPhase;
import net.ornithemc.osl.config.api.config.BaseConfig;
import net.ornithemc.osl.config.api.config.option.BooleanOption;
import net.ornithemc.osl.config.api.config.option.IntegerOption;
import net.ornithemc.osl.config.api.config.option.validator.IntegerValidators;
import net.ornithemc.osl.config.api.serdes.FileSerializerType;
import net.ornithemc.osl.config.api.serdes.SerializerTypes;

public class ExampleConfig extends BaseConfig {

	public static final BooleanOption EXAMPLE_OPTION_1 = new BooleanOption("Example Option 1", "This is a description.", false);
	public static final IntegerOption EXAMPLE_OPTION_2 = new IntegerOption("Example Option 2", "This is also a description.", 2, IntegerValidators.minmax(0,  10));

	@Override
	public String getNamespace() {
		return "example-mod-id";
	}

	@Override
	public String getName() {
		return "Example Config";
	}

	@Override
	public String getSaveName() {
		return "example-config";
	}

	@Override
	public ConfigScope getScope() {
		return ConfigScope.GLOBAL;
	}

	@Override
	public LoadingPhase getLoadingPhase() {
		return LoadingPhase.START;
	}

	@Override
	public FileSerializerType<?> getType() {
		return SerializerTypes.JSON;
	}

	@Override
	public int getVersion() {
		return 0;
	}

	@Override
	public void init() {
		registerOptions("Example Group", EXAMPLE_OPTION_1, EXAMPLE_OPTION_2);
	}
}
```

## Registering a config

You should register your configs in your mod's entrypoint:

```java
package com.example;

import net.ornithemc.osl.config.api.ConfigManager;
import net.ornithemc.osl.entrypoints.api.ModInitializer;

public class ExampleInitializer implements ModInitializer {

	@Override
	public void init() {
		ConfigManager.register(new ExampleConfig());
	}
}
```

## Custom option types

The API allows you to provide custom option types, so long as you register the required serializers.
Network serializers must be provided, as well as file serializers for the type your config uses.
These serializers should be registered in your mod's entrypoint.

```java
package com.example;

public class CookieOption extends BaseOption<Cookie> {

	public CookieOption(String name, String description, Cookie defaultValue) {
		super(name, description, defaultValue);
	}

	public CookieOption(String name, String description, Cookie defaultValue, Predicate<Cookie> validator) {
		super(name, description, defaultValue, validator);
	}
}
```
```java
package com.example;

import net.minecraft.network.PacketByteBuf;

import net.ornithemc.osl.config.api.serdes.config.option.JsonOptionSerializer;
import net.ornithemc.osl.config.api.serdes.config.option.JsonOptionSerializers;
import net.ornithemc.osl.config.api.serdes.config.option.NetworkOptionSerializer;
import net.ornithemc.osl.config.api.serdes.config.option.NetworkOptionSerializers;
import net.ornithemc.osl.entrypoints.api.ModInitializer;

public class ExampleInitializer implements ModInitializer {

	@Override
	public void init() {
		JsonOptionSerializers.register(CookieOption.class, new JsonOptionSerializer<CookieOption>() {

			@Override
			public void serialize(IdentifierOption option, SerializationSettings settings, JsonFile json) throws IOException {
				// write value to json
			}

			@Override
			public void deserialize(IdentifierOption option, SerializationSettings settings, JsonFile json) throws IOException {
				// read value from json
			}
		});
		NetworkOptionSerializers.register(CookieOption.class, new NetworkOptionSerializer<CookieOption>() {

			@Override
			public void serialize(IdentifierOption option, SerializationSettings settings, PacketByteBuf buffer) throws IOException {
				// write value to buffer
			}

			@Override
			public void deserialize(IdentifierOption option, SerializationSettings settings, PacketByteBuf buffer) throws IOException {
				// read value from buffer
			}
		});

		...
	}
}
```
