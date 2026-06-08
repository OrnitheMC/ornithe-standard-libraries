package net.ornithemc.osl.commands.api.command.argument;

import java.util.Arrays;
import java.util.Collection;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;

import net.minecraft.resource.Identifier;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.text.api.TextComponents;

public class IdentifierArgument implements ArgumentType<NamespacedIdentifier> {

	private static final Collection<String> EXAMPLES = Arrays.asList("foo", "foo:bar", "012");
	public static final DynamicCommandExceptionType UNKNOWN_ID_EXCEPTION = new DynamicCommandExceptionType(id -> TextComponents.translatable("argument.id.unknown", id));

	public static IdentifierArgument identifier() {
		return new IdentifierArgument();
	}

	public static Identifier getIdentifier(CommandContext<?> ctx, String arg) {
		return new Identifier(getNamespacedIdentifier(ctx, arg).toString());
	}

	public static NamespacedIdentifier getNamespacedIdentifier(CommandContext<?> ctx, String arg) {
		return ctx.getArgument(arg, NamespacedIdentifier.class);
	}

	@Override
	public NamespacedIdentifier parse(StringReader reader) throws CommandSyntaxException {
		return IdentifierParser.parse(reader);
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
