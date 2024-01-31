package net.ornithemc.osl.commands.impl.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.source.CommandSource;

import net.ornithemc.osl.commands.api.BrigadierCommandSource;
import net.ornithemc.osl.commands.api.CommandEvents;
import net.ornithemc.osl.commands.api.server.CommandSourceStack;
import net.ornithemc.osl.commands.impl.BrigadierCommandManagerImpl;

public class ServerCommandManagerImpl {

	private static final Logger LOGGER = LogManager.getLogger();

	private static MinecraftServer server;
	private static CommandDispatcher<CommandSourceStack> dispatcher;

	public static void setUp(MinecraftServer server) {
		if (ServerCommandManagerImpl.server != null) {
			throw new IllegalStateException("tried to set up server command manager while it was already set up!");
		}

		ServerCommandManagerImpl.server = server;
		ServerCommandManagerImpl.dispatcher = new CommandDispatcher<>();

		ServerCommandManagerImpl.init();
	}

	private static void init() {
		CommandEvents.REGISTER_SERVER_BRIGADIER_COMMANDS.invoker().accept(dispatcher);

		dispatcher.findAmbiguities((root, node1, node2, inputs) -> LOGGER.warn("Ambiguity between arguments {} and {} with inputs: {}", dispatcher.getPath(node1), dispatcher.getPath(node2), inputs));
		dispatcher.setConsumer((ctx, success, result) -> ctx.getSource().onCommandComplete(ctx, success, result));
	}

	public static void destroy(MinecraftServer server) {
		if (ServerCommandManagerImpl.server == null) {
			throw new IllegalStateException("tried to destroy server command manager while it was not set up!");
		}

		ServerCommandManagerImpl.server = null;
		ServerCommandManagerImpl.dispatcher = null;
	}

	public static CommandDispatcher<CommandSourceStack> getDispatcher() {
		return dispatcher;
	}

	public static boolean run(CommandSource commandSource, String command) {
		if (dispatcher == null || !(commandSource instanceof BrigadierCommandSource)) {
			return false;
		}

		BrigadierCommandSource brigadierSource = (BrigadierCommandSource)commandSource;
		CommandSourceStack source = (CommandSourceStack)brigadierSource.createCommandSourceStack();

		return BrigadierCommandManagerImpl.run(dispatcher, source, command, server.profiler, false) != null;
	}

	public static int run(CommandSourceStack source, String command) {
		if (dispatcher == null) {
			throw new IllegalStateException("cannot run server commands because the command dispatcher is not set up!");
		}
		return BrigadierCommandManagerImpl.run(dispatcher, source, command, server.profiler, true);
	}
}
