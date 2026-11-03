package net.ornithemc.osl.commands.api.command.argument;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.ornithemc.osl.commands.api.server.command.CommandSourceStack;
import net.ornithemc.osl.text.api.TextComponent;
import net.ornithemc.osl.text.api.TextComponents;

public class MessageArgument implements ArgumentType<MessageArgument.Message> {

	private static final Collection<String> EXAMPLES = Arrays.asList("Hello world!", "foo", "@e", "Hello @p :)");

	public static MessageArgument message() {
		return new MessageArgument();
	}

	public static TextComponent getMessage(CommandContext<CommandSourceStack> ctx, String arg) throws CommandSyntaxException {
		return ctx.getArgument(arg, Message.class).toText(ctx.getSource(), ctx.getSource().hasPermissions(2));
	}

	@Override
	public Message parse(StringReader reader) throws CommandSyntaxException {
		return Message.parseText(reader, true);
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}

	public static class Part {

		private final int start;
		private final int end;
		private final EntitySelector selector;

		public Part(int start, int end, EntitySelector selector) {
			this.start = start;
			this.end = end;
			this.selector = selector;
		}

		public int getStart() {
			return this.start;
		}

		public int getEnd() {
			return this.end;
		}

		public TextComponent getText(CommandSourceStack source) throws CommandSyntaxException {
			return EntitySelector.joinNames(this.selector.findEntities(source));
		}
	}

	public static class Message {

		private final String text;
		private final Part[] parts;

		public Message(String text, Part[] parts) {
			this.text = text;
			this.parts = parts;
		}

		public TextComponent toText(CommandSourceStack source, boolean op) throws CommandSyntaxException {
			if (this.parts.length == 0 || !op) {
				return TextComponents.literal(this.text);
			}

			TextComponent text = TextComponents.literal(this.text.substring(0, this.parts[0].getStart()));
			int start = this.parts[0].getStart();

			for (Part part : this.parts) {
				TextComponent sibling = part.getText(source);

				if (start < part.getStart()) {
					text.append(this.text.substring(start, part.getStart()));
				}
				if (sibling != null) {
					text.append(sibling);
				}

				start = part.getEnd();
			}

			if (start < this.text.length()) {
				text.append(this.text.substring(start, this.text.length()));
			}

			return text;
		}

		public static Message parseText(StringReader reader, boolean op) throws CommandSyntaxException {
			String value = reader.getString().substring(reader.getCursor(), reader.getTotalLength());

			if (!op) {
				reader.setCursor(reader.getTotalLength());
				return new Message(value, new Part[0]);
			}

			ArrayList<Part> parts = new ArrayList<>();
			int startCursor = reader.getCursor();

			while (reader.canRead()) {
				if (reader.peek() == '@') {
					int selectorStartCursor = reader.getCursor();

					EntitySelector selector;

					try {
						selector = new EntitySelectorParser(reader).parse();
					} catch (CommandSyntaxException e) {
						if (e.getType() == EntitySelectorParser.SELECTOR_MISSING_EXCEPTION || e.getType() == EntitySelectorParser.UNKNOWN_SELECTOR_EXCEPTION) {
							reader.setCursor(selectorStartCursor + 1);
							continue;
						} else {
							throw e;
						}
					}

					parts.add(new Part(selectorStartCursor - startCursor, reader.getCursor() - startCursor, selector));
				} else {
					reader.skip();
				}
			}
			return new Message(value, parts.toArray(new Part[parts.size()]));
		}
	}
}
