# Commands API

The Commands API provides events for registering commands, a framework for client commands, and support for Brigadier commands in 1.12 and below.

## Registering Commands

Command registration should be done in a listener to the `REGISTER_COMMANDS` event. The `ServerCommands` class provides utility methods for creating commands.

An example is shown below.

```java
package com.example;

import net.ornithemc.osl.commands.api.server.ServerCommandEvents;
import net.ornithemc.osl.entrypoints.api.ModInitializer;

public class ExampleInitializer implements ModInitializer {

	@Override
	public void init() {
		ServerCommandEvents.REGISTER_COMMANDS.register((commands, selection, context) -> {
			ExampleCommand.register(commands);
		});
	}
}
```

```java
package com.example;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.server.command.source.CommandSourceStack;

import net.ornithemc.osl.text.api.TextComponents;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static net.ornithemc.osl.commands.api.server.ServerCommands.literal;

public final class ExampleCommand {

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(
			literal("example")
			.executes(context -> {
				context.getSource().sendSuccess(TextComponents.literal("Successfully ran example command!"), false);
				return SINGLE_SUCCESS
			}));
	}
}
```

## Registering Client Commands

The Commands API provides its own framework for client commands, using Brigadier, though no custom argument types are provided in the API. Command registration should be done in a listener to the `REGISTER_COMMANDS` event. The `ClientCommands` class provides utility methods for creating commands.

An example is shown below.

```java
package com.example;

import net.ornithemc.osl.commands.api.client.ClientCommandEvents;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;

public class ExampleInitializer implements ClientModInitializer {

	@Override
	public void initClient() {
		ClientCommandEvents.REGISTER_COMMANDS.register((commands, selection, context) -> {
			ExampleClientCommand.register(commands);
		});
	}
}
```

```java
package com.example;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.text.LiteralText;

import net.ornithemc.osl.commands.api.client.command.ClientCommandSource;
import net.ornithemc.osl.text.api.TextComponents;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static net.ornithemc.osl.commands.api.client.ClientCommands.literal;

public final class ExampleCommand {

	public static void register(CommandDispatcher<ClientCommandSource> dispatcher) {
		dispatcher.register(
			literal("example")
			.executes(context -> {
				context.getSource().sendSuccess(TextComponents.literal("Successfully ran example command!"));
				return SINGLE_SUCCESS
			}));
	}
}
```

## Registering Legacy Commands

In 1.12.2 and below, Minecraft used its own framework for commands. This framework is still usable if you prefer it over Brigadier. Command registration should be done in a listener to the `REGISTER_LEGACY_COMMANDS` event.

An example is shown below.

```java
package com.example;

import net.ornithemc.osl.commands.api.server.ServerCommandEvents;
import net.ornithemc.osl.entrypoints.api.ModInitializer;

public class ExampleInitializer implements ModInitializer {

	@Override
	public void init() {
		ServerCommandEvents.REGISTER_LEGACY_COMMANDS.register((commands, selection, context) -> {
			commands.accept(new ExampleCommand());
		});
	}
}
```

```java
package com.example;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.AbstractCommand;
import net.minecraft.server.command.exception.CommandException;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.text.LiteralText;

import net.ornithemc.osl.text.TextComponents;

public class ExampleCommand extends AbstractCommand {

	@Override
	public String getName() {
		return "example";
	}

	@Override
	public String getUsage(CommandSource source) {
		return "/example";
	}

	@Override
	public void run(MinecraftServer server, CommandSource source, String[] args) throws CommandException {
		source.sendMessage(TextComponents.literal("Successfully ran example command!"));
	}
}
```
