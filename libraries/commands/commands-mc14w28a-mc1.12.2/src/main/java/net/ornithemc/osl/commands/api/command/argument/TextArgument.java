package net.ornithemc.osl.commands.api.command.argument;

import java.util.Arrays;
import java.util.Collection;

import com.google.gson.JsonParseException;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;

import net.ornithemc.osl.text.api.TextComponent;
import net.ornithemc.osl.text.api.TextComponents;

public class TextArgument implements ArgumentType<TextComponent> {

	private static final Collection<String> EXAMPLES = Arrays.asList("\"hello world\"", "\"\"", "\"{\"text\":\"hello world\"}", "[\"\"]");
	public static final DynamicCommandExceptionType INVALID_TEXT_EXCEPTION = new DynamicCommandExceptionType(text -> TextComponents.translatable("argument.component.invalid", text));

	private TextArgument() {
	}

	public static TextComponent getText(CommandContext<?> ctx, String arg) {
		return ctx.getArgument(arg, TextComponent.class);
	}

	public static TextArgument text() {
		return new TextArgument();
	}

	@Override
	public TextComponent parse(StringReader reader) throws CommandSyntaxException {
		try {
			TextComponent text = TextComponents.fromJson(reader.getRemaining());

			if (text == null) {
				throw INVALID_TEXT_EXCEPTION.createWithContext(reader, "empty");
			}

			return text;
		} catch (JsonParseException e) {
			String message = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
			throw INVALID_TEXT_EXCEPTION.createWithContext(reader, message);
		}
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
