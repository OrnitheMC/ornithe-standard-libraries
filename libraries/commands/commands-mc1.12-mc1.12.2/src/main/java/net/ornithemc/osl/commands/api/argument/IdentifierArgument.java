package net.ornithemc.osl.commands.api.argument;

import java.util.Arrays;
import java.util.Collection;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;

import net.minecraft.resource.Identifier;
import net.minecraft.text.TranslatableText;

public class IdentifierArgument implements ArgumentType<Identifier> {

	private static final Collection<String> EXAMPLES = Arrays.asList("foo", "foo:bar", "012");
	public static final DynamicCommandExceptionType UNKNOWN_ID_EXCEPTION = new DynamicCommandExceptionType(id -> (Message)new TranslatableText("argument.id.unknown", id));

	public static IdentifierArgument identifier() {
		return new IdentifierArgument();
	}

	public static Identifier getId(CommandContext<?> ctx, String arg) {
		return ctx.getArgument(arg, Identifier.class);
	}

	@Override
	public Identifier parse(StringReader reader) throws CommandSyntaxException {
		return IdentifierParser.parse(reader);
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
