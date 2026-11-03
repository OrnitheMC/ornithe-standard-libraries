package net.ornithemc.osl.commands.api.client;

import com.mojang.brigadier.CommandDispatcher;

import net.ornithemc.osl.commands.api.client.command.ClientCommandSource;
import net.ornithemc.osl.core.api.events.Event;

/**
 * Events related to the client commands lifecycle.
 */
public final class ClientCommandEvents {

	/**
	 * This event is invoked upon world-load, once the connection to the server has been established.
	 * 
	 * <p>
	 * Custom command registration should be done in a listener of this event.
	 * Helper methods for constructing commands can be found in the {@linkplain
	 * ClientCommands} class.
	 * 
	 * <p>
	 * Listeners to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * ClientCommandEvents.REGISTER_COMMANDS.register((commands, selection, context) -> {
	 * 	commands.register(ClientCommands.literal("example"));
	 * });
	 * }
	 * </pre>
	 * 
	 * @see ClientCommands
	 */
	public static final Event<RegisterCommandsListener> REGISTER_COMMANDS = Event.of(listeners -> (commands, selection, context) -> {
		for (RegisterCommandsListener listener : listeners) {
			listener.registerCommands(commands, selection, context);
		}
	});

	/**
	 * A listener to the {@linkplain ClientCommandEvents#REGISTER_COMMANDS} event.
	 */
	public interface RegisterCommandsListener {

		/**
		 * Callback to register client commands.
		 * 
		 * @param commands  the command dispatcher to register commands to.
		 * @param selection the command selection for the current game.
		 * @param context   the build context for the current game.
		 */
		void registerCommands(CommandDispatcher<ClientCommandSource> commands, ClientCommands.Selection selection, ClientCommands.BuildContext context);

	}
}
