package net.ornithemc.osl.commands.api.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.minecraft.resource.Identifier;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.core.impl.util.NamespacedIdentifierParseException;
import net.ornithemc.osl.text.api.TextComponents;

public class IdentifierParser {

	private static final SimpleCommandExceptionType INVALID_IDENTIFIER_EXCEPTION = new SimpleCommandExceptionType(TextComponents.translatable("argument.id.invalid"));

	public static NamespacedIdentifier parse(StringReader reader) throws CommandSyntaxException {
		int cursor = reader.getCursor();

		while (reader.canRead() && NamespacedIdentifiers.isCharAllowed(reader.peek())) {
			reader.skip();
		}

		try {
			return NamespacedIdentifiers.parse(reader.getString().substring(cursor, reader.getCursor()));
		} catch (NamespacedIdentifierParseException e) {
			reader.setCursor(cursor);
			throw INVALID_IDENTIFIER_EXCEPTION.createWithContext(reader);
		}
	}

	public static Identifier parseIdentifier(StringReader reader) throws CommandSyntaxException {
		NamespacedIdentifier identifier = parse(reader);
		return new Identifier(identifier.toString());
	}
}
