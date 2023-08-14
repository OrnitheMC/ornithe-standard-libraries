package net.ornithemc.osl.commands.api;

import java.util.function.Predicate;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class BrigadierCommandManager {

	public static Predicate<String> validator(Parser parser) {
		return s -> {
			try {
				parser.parse(new StringReader(s));
				return true;
			} catch (CommandSyntaxException e) {
				return false;
			}
		};
	}

	@FunctionalInterface
	public interface Parser {

		void parse(StringReader reader) throws CommandSyntaxException;

	}
}
