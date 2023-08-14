package net.ornithemc.osl.commands.api.argument;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;

import net.minecraft.resource.Identifier;
import net.minecraft.server.command.function.CommandFunction;
import net.minecraft.text.TranslatableText;

import net.ornithemc.osl.commands.api.server.CommandSourceStack;

public class FunctionArgument implements ArgumentType<FunctionArgument.Result> {

	private static final Collection<String> EXAMPLES = Arrays.asList("foo", "foo:bar", "#foo");
	private static final DynamicCommandExceptionType UNKNOWN_FUNCTION_EXCEPTION = new DynamicCommandExceptionType(function -> (Message)new TranslatableText("arguments.function.unknown", function));

	public static FunctionArgument functions() {
		return new FunctionArgument();
	}

	@Override
	public Result parse(StringReader reader) throws CommandSyntaxException {
		int cursor = reader.getCursor();
		Identifier key = IdentifierParser.parse(reader);
		return ctx -> {
			CommandFunction function = ctx.getSource().getServer().getFunctions().get(key);
			if (function == null) {
				reader.setCursor(cursor);
				throw UNKNOWN_FUNCTION_EXCEPTION.createWithContext(reader, key.toString());
			}
			return Collections.singleton(function);
		};
	}

	public static Collection<CommandFunction> getFunctions(CommandContext<CommandSourceStack> ctx, String arg) throws CommandSyntaxException {
		return ctx.getArgument(arg, Result.class).create(ctx);
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}

	public interface Result {

		Collection<CommandFunction> create(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException;

	}
}
