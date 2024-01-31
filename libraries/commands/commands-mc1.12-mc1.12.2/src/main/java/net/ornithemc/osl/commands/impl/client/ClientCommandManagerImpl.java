package net.ornithemc.osl.commands.impl.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.client.Minecraft;
import net.minecraft.server.command.source.CommandSource;

import net.ornithemc.osl.commands.api.BrigadierCommandSource;
import net.ornithemc.osl.commands.api.CommandEvents;
import net.ornithemc.osl.commands.api.client.ClientCommandSourceStack;
import net.ornithemc.osl.commands.impl.BrigadierCommandManagerImpl;

public class ClientCommandManagerImpl {

	private static final Logger LOGGER = LogManager.getLogger();

	private static Minecraft minecraft;
	private static CommandDispatcher<ClientCommandSourceStack> dispatcher;

	public static void setUp(Minecraft minecraft) {
		if (ClientCommandManagerImpl.minecraft != null) {
			throw new IllegalStateException("tried to set up client command manager while it was already set up!");
		}

		ClientCommandManagerImpl.minecraft = minecraft;
		ClientCommandManagerImpl.dispatcher = new CommandDispatcher<>();

		ClientCommandManagerImpl.init();
	}

	private static void init() {
		CommandEvents.REGISTER_CLIENT_BRIGADIER_COMMANDS.invoker().accept(dispatcher);

		dispatcher.findAmbiguities((root, node1, node2, inputs) -> LOGGER.warn("Ambiguity between arguments {} and {} with inputs: {}", dispatcher.getPath(node1), dispatcher.getPath(node2), inputs));
		dispatcher.setConsumer((ctx, success, result) -> ctx.getSource().onCommandComplete(ctx, success, result));
	}

	public static void destroy(Minecraft minecraft) {
		if (ClientCommandManagerImpl.minecraft == null) {
			throw new IllegalStateException("tried to destroy client command manager while it was not set up!");
		}

		ClientCommandManagerImpl.minecraft = null;
		ClientCommandManagerImpl.dispatcher = null;
	}

	public static CommandDispatcher<ClientCommandSourceStack> getDispatcher() {
		return dispatcher;
	}

	public static boolean run(String command) {
		return run(minecraft.player, command);
	}

	public static boolean run(CommandSource commandSource, String command) {
		if (dispatcher == null || !(commandSource instanceof BrigadierCommandSource)) {
			return false;
		}

		BrigadierCommandSource brigadierSource = (BrigadierCommandSource)commandSource;
		ClientCommandSourceStack source = (ClientCommandSourceStack)brigadierSource.createCommandSourceStack();

		return BrigadierCommandManagerImpl.run(dispatcher, source, command, minecraft.profiler, false) != null;
	}

	public static int run(ClientCommandSourceStack source, String command) {
		if (dispatcher == null) {
			throw new IllegalStateException("cannot run client commands because the command dispatcher is not set up!");
		}
		return BrigadierCommandManagerImpl.run(dispatcher, source, command, minecraft.profiler, true);
	}
}
