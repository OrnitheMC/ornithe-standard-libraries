package net.ornithemc.osl.commands.impl.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.BuiltInExceptionProvider;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.client.Minecraft;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Formatting;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TextUtils;
import net.minecraft.text.TranslatableText;

import net.ornithemc.osl.commands.api.CommandEvents;
import net.ornithemc.osl.commands.api.client.ClientCommandSourceStack;
import net.ornithemc.osl.commands.impl.interfaces.mixin.IEntity;

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
		CommandEvents.REGISTER_CLIENT_COMMANDS.invoker().accept(dispatcher);

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
		if (dispatcher == null || minecraft.player == null) {
			return false;
		}

		IEntity player = (IEntity)minecraft.player;
		ClientCommandSourceStack source = player.osl$commands$createClientCommandSourceStack();

		return ClientCommandManagerImpl.run(source, command, false) != null;
	}

	public static int run(ClientCommandSourceStack source, String command) {
		return run(source, command, true);
	}

	public static Integer run(ClientCommandSourceStack source, String command, boolean force) {
		if (dispatcher == null) {
			throw new IllegalStateException("cannot run client commands because the command dispatcher is not set up!");
		}
		StringReader reader = new StringReader(command);
		if (reader.canRead() && reader.peek() == '/') {
			reader.skip();
		}
		minecraft.profiler.push(command);
		try {
			return dispatcher.execute(reader, source);
		} catch (CommandSyntaxException e) {
			if (!force && shouldIgnore(e.getType())) {
				return null;
			}
			source.sendFailure(TextUtils.fromMessage(e.getRawMessage()));
			if (e.getInput() != null && e.getCursor() >= 0) {
				Text message = new LiteralText("")
					.withStyle(style -> style
						.setColor(Formatting.GRAY)
						.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)));
				int cursor = Math.min(e.getInput().length(), e.getCursor());
				if (cursor > 10) {
					message.append("...");
				}
				message.append(e.getInput().substring(Math.max(0, cursor - 10), cursor));
				if (cursor < e.getInput().length()) {
					message.append(new LiteralText(e.getInput().substring(cursor))
						.setFormatting(Formatting.RED, Formatting.UNDERLINE));
				}
				message.append(new TranslatableText("command.context.here").setFormatting(Formatting.RED,
						Formatting.ITALIC));
				source.sendFailure(message);
			}
			return 0;
		} catch (Exception e) {
			LiteralText message = new LiteralText(e.getMessage() == null ? e.getClass().getName() : e.getMessage());
			if (LOGGER.isDebugEnabled()) {
				StackTraceElement[] stackTrace = e.getStackTrace();
				for (StackTraceElement element : stackTrace) {
					message
						.append("\n\n")
						.append(element.getMethodName())
						.append("\n ")
						.append(element.getFileName())
						.append(":")
						.append(String.valueOf(element.getLineNumber()));
				}
			}
			source.sendFailure(new TranslatableText("command.failed")
				.withStyle(style -> style.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, message))));
			return 0;
		} finally {
			minecraft.profiler.pop();
		}
	}

	private static boolean shouldIgnore(CommandExceptionType type) {
		BuiltInExceptionProvider exceptions = CommandSyntaxException.BUILT_IN_EXCEPTIONS;
		return type == exceptions.dispatcherUnknownCommand() || type == exceptions.dispatcherParseException();
	}
}
