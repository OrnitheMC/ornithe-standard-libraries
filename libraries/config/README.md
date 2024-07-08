# Config API

The Config API provides a framework for building and storing configs for your mods.

## What is a config?

A config is a collection of options, with instructions for when to load and unload it and how and where to save it to disk. The `net.ornithemc.osl.config.api.config.Config` interface defines getter methods for these attributes. They must be unchangeable, or your config could get corrupted. The following attributes must be configured:

- Namespace: the namespace this config belongs to. Namespaces can be used to group related configs together. Each namespace gets its own directory within the config directory of each scope. Typically each mod should use its mod id as the namespace for its configs. Can be `null`.
- Name: the display name of this config. Can be a normal string or a translation key.
- Save Name: the name of the file this config is saved to.
- Scope: the scope of this config. The config scope defines the context in which configs should apply. Configs are loaded when the scope is initialized, and unloaded when it shuts down. A config only has defined behavior from within its scope, and should not be accessed when its scope is not active.
- Loading Phase: the loading phase of this config. A loading phase defines a point in the scope's lifecycle at which configs are loaded.
- Type: the type of serializer used to save this config to file.

A config also contains a collection of option groups. Option groups can be used to group related options together. An option group has a name and a collection of options. The option group's name must be unique within its config, as it is used as a display name as well as in serialization.

The `net.ornithemc.osl.config.api.config.BaseConfig` class provides a base from which to build custom configs. It takes care of option management, leaving only the attribute getters and config initializer to be implemented.

### What is an option?

An option must implement the `net.ornithemc.osl.config.api.config.option.Option` interface. This interface defines basic attributes of an option. These attributes must be unchangeable, or your config could get corrupted. The following attributes must be configured:

- Name: the display name of this option. An option's name must be unique within its group, as it is also used in serialization. Can be a normal string or a translation key.
- Description: a short description explaining what this option does. Can be a normal string or a translation key.

The `net.ornithemc.osl.config.api.config.option.BaseOption` class provides a base from which to build custom option types. It defines getters and setters for the option value as well as a getter for the default value. The base option class validates new values passed to the setter with a `java.util.function.Predicate`. Some basic validators can be constructed with the methods in `net.ornithemc.osl.config.api.config.option.validator.OptionValidators`, as well as its siblings `IntegerValidators` and `StringValidators`, but custom predicates can of course be used as well.

Custom option classes extending `BaseOption` must be for *immutable types*. Immutable types do not allow their data to be modified. Immutable types include primitives such as `boolean`, `float`, and `int`, and classes such as `String`, `UUID`, and `Path` are also examples of immutable types. These are the most straightforward options to implement, often only requiring one or two constructors to be defined.

Custom options for *mutable types* must be made by extending the `net.ornithemc.osl.config.api.config.option.ModifiableOption` class. Mutable types are types that do allow their data to be modified. Mutable types include collections like `List` and `Map`, and classes such as Minecraft's `NbtList` and `NbtCompound` are also examples of mutable types. Using the `ModifiableOption` class does require that option values can be converted between a 'modifiable view' and an 'unmodifiable view'. Consider `List`s, many implementations, like `ArrayList`, are modifiable. Getters and setters can be called to modify the contents of the list. But any list can be converted to an unmodifiable through `Collections.unmodifiableList(List list)`. These conversions are needed by the option to make sure option values cannot be changed without the option knowing about it. This is to allow the option to validate new values. In order to modify the value of the option, either the setter from `BaseOption` can be used to pass in a new value, or `ModifiableOption`'s `modify` or `modifyIO` methods can be used to modify the current value.

## Custom configs

Custom configs can be made by extending the `net.ornithemc.osl.config.api.config.BaseConfig` class. You must register all your options in the `init` method. With the `registerOptions(String group, Option... options)` method, you can register an entire option group at once. Options can be kept in fields for easy access elsewhere.

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
		return "example-config.json";
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

The API allows you to provide custom option types, so long as you register the required serializers. Network serializers must be provided, as well as file serializers for the type your config uses. These serializers should be registered in your mod's entrypoint.

Custom options should extend either the `BaseOption` or `ModifiableOption` class. See [What is an option?](#what-is-an-option) for more details. A few examples are shown below.

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
		return new ModifiableCookie(value);
	}

	@Override
	protected Cookie unmodifiable(Cookie value) {
		return new UnmodifiableCookie(value);
	}
}
```

## Custom object serializers

Object serializers are used to serialize and deserialize objects. They can be used by other object serializers or in option serialization for serializing and deserializing option values. Object serializers are useful because they can be re-used for many different option serializers. Consider how the `String` object serializer is used by both the `UUID` and `Path` object serializers, or how all object serializers can be used by `List` serializers to serialize and deserialize individual elements.

Custom object serializers must be registered in your mod's entrypoint:

```java
package com.example;

import java.io.IOException;

import net.ornithemc.osl.config.api.serdes.JsonSerializer;
import net.ornithemc.osl.config.api.serdes.JsonSerializers;
import net.ornithemc.osl.core.api.json.JsonFile;
import net.ornithemc.osl.entrypoints.api.ModInitializer;

public class ExampleInitializer implements ModInitializer {

	@Override
	public void init() {
		JsonSerializers.register(Cookie.class, new JsonSerializer<Cookie>() {

			@Override
			public void serialize(Cookie cookie, JsonFile json) throws IOException {
				json.writeObject(_json -> {
					json.writeString("shape", cookie.getShape());
					json.writeName("baseIngredients");
					JsonSerializers.Maps.serialize(cookie.getBaseIngredients(), String.class, Integer.class, json);
					json.writeName("specialIngredients");
					JsonSerializers.Maps.serialize(cookie.getSpecialIngredients(), String.class, Integer.class, json);
					json.writeNumber("bakingTime", cookie.getBakingTime());
				});
			}

			@Override
			public Cookie deserialize(JsonFile json) throws IOException {
				Cookie cookie = new Cookie();
				json.readObject(_json -> {
					cookie.setShape(json.readString("shape");
					json.readName("baseIngredients");
					JsonSerializers.Maps.deserialize(cookie.getBaseIngredients(), String.class, Integer.class, json);
					json.readName("specialIngredients");
					JsonSerializers.Maps.deserialize(cookie.getSpecialIngredients(), String.class, Integer.class, json);
					cookie.setBakingTime(json.readNumber("bakingTime").intValue());
				});
			}
		});
	}
}
```

## Custom option serializers

Option serialization can be done in two ways: through custom object serializers or through custom option serializers. The former is recommended, but in some cases (such as generic options) the latter is preferable. See [Custom object serializers](#custom-object-serializers) for more details on object serializers.

If you're registering a custom object serializer, then registering the option serializer simplifies to a one-liner.

```java
package com.example;

import java.io.IOException;

import net.ornithemc.osl.config.api.serdes.config.option.JsonOptionSerializers;
import net.ornithemc.osl.core.api.json.JsonFile;
import net.ornithemc.osl.entrypoints.api.ModInitializer;

public class ExampleInitializer implements ModInitializer {

	@Override
	public void init() {
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
				Cookie cookie = option.get();
				json.writeObject(_json -> {
					json.writeString("shape", cookie.getShape());
					json.writeName("baseIngredients");
					JsonSerializers.Maps.serialize(cookie.getBaseIngredients(), String.class, Integer.class, json);
					json.writeName("specialIngredients");
					JsonSerializers.Maps.serialize(cookie.getSpecialIngredients(), String.class, Integer.class, json);
					json.writeNumber("bakingTime", cookie.getBakingTime());
				});
			}

			@Override
			public void deserialize(CookieOption option, SerializationSettings settings, JsonFile json) throws IOException {
				option.modifyIO(cookie -> {
					json.readObject(_json -> {
						cookie.setShape(json.readString("shape");
						json.readName("baseIngredients");
						JsonSerializers.Maps.deserialize(cookie.getBaseIngredients(), String.class, Integer.class, json);
						json.readName("specialIngredients");
						JsonSerializers.Maps.deserialize(cookie.getSpecialIngredients(), String.class, Integer.class, json);
						cookie.setBakingTime(json.readNumber("bakingTime").intValue());
					});
				});
			}
		});
	}
}
```
