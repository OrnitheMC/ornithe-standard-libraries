package net.ornithemc.osl.commands.api;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.Command;

import net.ornithemc.osl.commands.api.client.ClientCommandSourceStack;
import net.ornithemc.osl.commands.api.server.CommandSourceStack;
import net.ornithemc.osl.core.api.events.Event;

public class CommandEvents {

	public static final Event<BiConsumer<MinecraftServer, Consumer<Command>>>  REGISTER_SERVER_COMMANDS           = Event.biConsumer();
	public static final Event<Consumer<CommandDispatcher<CommandSourceStack>>> REGISTER_SERVER_BRIGADIER_COMMANDS = Event.consumer();

	public static final Event<Consumer<CommandDispatcher<ClientCommandSourceStack>>> REGISTER_CLIENT_BRIGADIER_COMMANDS = Event.consumer();

}
