package net.ornithemc.osl.commands.api.client.command;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Iterables;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.CommandNode;

import net.ornithemc.osl.commands.api.client.ClientCommands;
import net.ornithemc.osl.text.api.TextComponents;

public final class HelpCommand {

	private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(TextComponents.translatable("commands.help.failed"));

	public static void register(CommandDispatcher<ClientCommandSource> dispatcher) {
		LiteralArgumentBuilder<ClientCommandSource> command = ClientCommands
			.literal("help")
			.executes(HelpCommand::giveHelp)
			.then(ClientCommands
				.argument("command", StringArgumentType.greedyString())
				.executes(HelpCommand::giveCommandHelp));

		dispatcher.register(command);
	}

	private static int giveHelp(CommandContext<ClientCommandSource> ctx) {
		return giveCommandHelp(ctx, ClientCommands.dispatcher().getRoot());
	}

	private static int giveCommandHelp(CommandContext<ClientCommandSource> ctx) throws CommandSyntaxException {
		String commandName = StringArgumentType.getString(ctx, "command");

		ParseResults<ClientCommandSource> parseResults = ClientCommands.dispatcher().parse(commandName, ctx.getSource());
		List<ParsedCommandNode<ClientCommandSource>> commands = parseResults.getContext().getNodes();

		if (commands.isEmpty()) {
			throw ERROR_FAILED.create();
		}

		return giveCommandHelp(ctx, Iterables.getLast(commands).getNode());
	}

	private static int giveCommandHelp(CommandContext<ClientCommandSource> ctx, CommandNode<ClientCommandSource> startNode) {
		Map<CommandNode<ClientCommandSource>, String> commands = ClientCommands.dispatcher().getSmartUsage(startNode, ctx.getSource());

		for (String command : commands.values()) {
			ctx.getSource().sendSuccess(TextComponents.literal("/" + command));
		}

		return commands.size();
	}
}
