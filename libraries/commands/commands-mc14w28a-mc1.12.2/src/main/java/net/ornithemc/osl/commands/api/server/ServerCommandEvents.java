package net.ornithemc.osl.commands.api.server;

import java.util.function.Consumer;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.server.command.Command;

import net.ornithemc.osl.commands.api.server.command.CommandSourceStack;
import net.ornithemc.osl.core.api.events.Event;

/**
 * Events related to the server commands lifecycle.
 */
public final class ServerCommandEvents {

	/**
	 * This event is invoked upon world-load, after Vanilla command registration is finished.
	 * 
	 * <p>
	 * Custom command registration should be done in a listener of this event.
	 * Helper methods for constructing commands can be found in the {@linkplain
	 * ServerCommands} class.
	 * 
	 * <p>
	 * Listeners to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * ServerCommandEvents.REGISTER_COMMANDS.register((commands, selection, context) -> {
	 * 	commands.register(ServerCommands.literal("example"));
	 * });
	 * }
	 * </pre>
	 * 
	 * @see ServerCommands
	 */
	public static final Event<RegisterCommandsListener> REGISTER_COMMANDS = Event.of(listeners -> (commands, selection, context) -> {
		for (RegisterCommandsListener listener : listeners) {
			listener.registerCommands(commands, selection, context);
		}
	});

	/**
	 * This event is invoked upon world-load, after Vanilla command registration is finished.
	 * 
	 * <p>
	 * Custom command registration should be done in a listener of this event.
	 * 
	 * <p>
	 * Listeners to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * ServerCommandEvents.REGISTER_LEGACY_COMMANDS.register((commands, selection, context) -> {
	 * 	commands.accept(new ExampleCommand();
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<RegisterLegacyCommandsListener> REGISTER_LEGACY_COMMANDS = Event.of(listeners -> (commands, selection, context) -> {
		for (RegisterLegacyCommandsListener listener : listeners) {
			listener.registerLegacyCommands(commands, selection, context);
		}
	});

	/**
	 * A listener to the {@linkplain ServerCommandEvents#REGISTER_COMMANDS} event.
	 */
	public interface RegisterCommandsListener {

		/**
		 * Callback to register server commands.
		 * 
		 * @param commands  the command dispatcher to register commands to.
		 * @param selection the command selection for the current server environment.
		 * @param context   the build context for the current server environment.
		 */
		void registerCommands(CommandDispatcher<CommandSourceStack> commands, ServerCommands.Selection selection, ServerCommands.BuildContext context);

	}

	/**
	 * A listener to the {@linkplain ServerCommandEvents#REGISTER_LEGACY_COMMANDS} event.
	 */
	public interface RegisterLegacyCommandsListener {

		/**
		 * Callback to register server commands.
		 * 
		 * @param commands  the command registry to register commands to.
		 * @param selection the command selection for the current server environment.
		 * @param context   the build context for the current server environment.
		 */
		void registerLegacyCommands(Consumer<Command> commands, ServerCommands.Selection selection, ServerCommands.BuildContext context);

	}
}
