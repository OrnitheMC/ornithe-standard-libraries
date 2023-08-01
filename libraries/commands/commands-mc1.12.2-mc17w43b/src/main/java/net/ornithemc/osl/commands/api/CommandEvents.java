package net.ornithemc.osl.commands.api;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.Command;

import net.ornithemc.osl.core.api.events.Event;

public class CommandEvents {

	public static final Event<BiConsumer<MinecraftServer, Consumer<Command>>> REGISTER_SERVER_COMMANDS = Event.biConsumer();

}
