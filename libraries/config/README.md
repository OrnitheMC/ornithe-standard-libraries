# Config API

The Config API provides a framework for building and storing configs for your mods.

## Creating a config

The easiest way to create a config is to extend the `net.ornithemc.osl.config.api.config.BaseConfig` class.
In order to make serialization work, you must register all your options in the `init` method. With the `registerOptions(String group, Option... options)` method, you can register an entire option group at once.
An option group is a collection of options with a name. It's a useful tool to categoryize your options if you have
many. You can of course also add all your options in just one group. 

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

Custom options of immutable types are the most straightforward. Immutable types are classes that do not
allow their data to be modified. Immutable types include primitives such as `boolean`, `float`, and `int`,
but classes such as `String`, `UUID`, and `Path` are also examples of immutable types. Options for these
types should made by extending the `BaseOption` class.

```java
package com.example;

import java.util.function.Predicate;

import net.ornithemc.osl.config.api.config.option.BaseOption;

public class CookieOption extends BaseOption<Cookie> {

	public CookieOption(String name, String description, Cookie defaultValue) {
		super(name, description, defaultValue);
	}

	public CookieOption(String name, String description, Cookie defaultValue, Predicate<Cookie> validator) {
		super(name, description, defaultValue, validator);
	}
}
```

Custom options of mutable types are more involved. Mutable types are classes that allow their data to be modified.
Mutable types include collections like `List` and `Map`, but classes such as Minecraft's `NbtList` and `NbtCompound`
are also examples of mutable types. Options for these types should be made by extending the `ModifiableOption` class.
Using this class does require that your objects of your custom type can be converted to a 'modifiable view' (which it
most likely is by default) and an 'unmodifiable view'. The reason for this is that the config should be able keep track
of when the option's value is modified. Providing an unmodifiable view allows the option to force modifications to happen
through the option's `modify` method.

```java
package com.example;

import java.util.function.Predicate;

import net.ornithemc.osl.config.api.config.option.ModifiableOption;

public class CookieOption extends ModifiableOption<Cookie> {

	public CookieOption(String name, String description, Cookie defaultValue) {
		super(name, description, defaultValue);
	}

	public CookieOption(String name, String description, Cookie defaultValue, Predicate<Cookie> validator) {
		super(name, description, defaultValue, validator);
	}

	@Override
	protected Cookie modifiable(Cookie value) {
		// convert to modifiable view
	}

	@Override
	protected Cookie unmodifiable(Cookie value) {
		// convert to unmodifiable view
	}
}
```

Option serialization can be done in two ways: through custom object serializers or through custom option serializers.
The former is recommended, but in some cases (such as generic options) the latter is preferable. Custom serializers
should all be registered in your mod's entrypoint.

If you're registering a custom object serializer, then registering the option serializer simplifies to a one-liner.

```java
package com.example;

import java.io.IOException;

import net.ornithemc.osl.config.api.serdes.JsonSerializer;
import net.ornithemc.osl.config.api.serdes.JsonSerializers;
import net.ornithemc.osl.config.api.serdes.config.option.JsonOptionSerializers;
import net.ornithemc.osl.core.api.json.JsonFile;
import net.ornithemc.osl.entrypoints.api.ModInitializer;

public class ExampleInitializer implements ModInitializer {

	@Override
	public void init() {
		JsonSerializers.register(Cookie.class, new JsonSerializer<Cookie>() {

			@Override
			public void serialize(Cookie value, JsonFile json) throws IOException {
				// write value to json
			}

			@Override
			public void deserialize(Cookie value, JsonFile json) throws IOException {
				// read value from json
			}
		});
		JsonOptionSerializers.register(CookieOption.class, Cookie.class);
	}
}
```

A custom option serializer may look as follows.

```java
package com.example;

import java.io.IOException;

import net.ornithemc.osl.config.api.serdes.SerializationSettings;
import net.ornithemc.osl.config.api.serdes.config.option.JsonOptionSerializer;
import net.ornithemc.osl.config.api.serdes.config.option.JsonOptionSerializers;
import net.ornithemc.osl.core.api.json.JsonFile;
import net.ornithemc.osl.entrypoints.api.ModInitializer;

public class ExampleInitializer implements ModInitializer {

	@Override
	public void init() {
		JsonOptionSerializers.register(CookieOption.class, new JsonOptionSerializer<CookieOption>() {

			@Override
			public void serialize(CookieOption option, SerializationSettings settings, JsonFile json) throws IOException {
				// write value to json
			}

			@Override
			public void deserialize(CookieOption option, SerializationSettings settings, JsonFile json) throws IOException {
				// read value from json
			}
		});
	}
}
```
