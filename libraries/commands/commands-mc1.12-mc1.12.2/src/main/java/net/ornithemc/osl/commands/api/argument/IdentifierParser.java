package net.ornithemc.osl.commands.api.argument;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.minecraft.resource.Identifier;
import net.minecraft.text.TranslatableText;

import net.ornithemc.osl.resource.loader.api.ResourceUtils;

public class IdentifierParser {

	private static final SimpleCommandExceptionType INVALID_IDENTIFIER_EXCEPTION = new SimpleCommandExceptionType((Message)new TranslatableText("argument.id.invalid"));

	public static Identifier parse(StringReader reader) throws CommandSyntaxException {
		int cursor = reader.getCursor();

		while (reader.canRead() && ResourceUtils.isValidChar(reader.peek())) {
			reader.skip();
		}

		Identifier id = new Identifier(reader.getString().substring(cursor, reader.getCursor()));

		if (!ResourceUtils.isValidIdentifier(id)) {
			reader.setCursor(cursor);
			throw INVALID_IDENTIFIER_EXCEPTION.createWithContext(reader);
		}

		return id;
	}
}
