package net.ornithemc.osl.commands.api.argument;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import net.ornithemc.osl.commands.api.AbstractCommandSourceStack;

public class MessageArgument implements ArgumentType<MessageArgument.Message> {

	private static final Collection<String> EXAMPLES = Arrays.asList("Hello world!", "foo", "@e", "Hello @p :)");

	public static MessageArgument message() {
		return new MessageArgument();
	}

	public static Text getMessage(CommandContext<AbstractCommandSourceStack<?>> ctx, String arg) throws CommandSyntaxException {
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

		public Text getText(AbstractCommandSourceStack<?> source) throws CommandSyntaxException {
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

		public Text toText(AbstractCommandSourceStack<?> source, boolean op) throws CommandSyntaxException {
			if (this.parts.length == 0 || !op) {
				return new LiteralText(this.text);
			}
			LiteralText text = new LiteralText(this.text.substring(0, this.parts[0].getStart()));
			int start = this.parts[0].getStart();
			for (Part part : this.parts) {
				Text text2 = part.getText(source);
				if (start < part.getStart()) {
					text.append(this.text.substring(start, part.getStart()));
				}
				if (text2 != null) {
					text.append(text2);
				}
				start = part.getEnd();
			}
			if (start < this.text.length()) {
				text.append(this.text.substring(start, this.text.length()));
			}
			return text;
		}

		public static Message parseText(StringReader reader, boolean op) throws CommandSyntaxException {
			String string = reader.getString().substring(reader.getCursor(), reader.getTotalLength());
			if (!op) {
				reader.setCursor(reader.getTotalLength());
				return new Message(string, new Part[0]);
			}
			ArrayList<Part> parts = new ArrayList<>();
			int start = reader.getCursor();
			while (reader.canRead()) {
				if (reader.peek() == '@') {
					EntitySelector selector;
					int cursor = reader.getCursor();
					try {
						EntitySelectorParser parser = new EntitySelectorParser(reader);
						selector = parser.parse();
					} catch (CommandSyntaxException e) {
						if (e.getType() == EntitySelectorParser.SELECTOR_MISSING_EXCEPTION || e.getType() == EntitySelectorParser.UNKNOWN_SELECTOR_EXCEPTION) {
							reader.setCursor(cursor + 1);
							continue;
						}
						throw e;
					}
					parts.add(new Part(cursor - start, reader.getCursor() - start, selector));
					continue;
				}
				reader.skip();
			}
			return new Message(string, parts.toArray(new Part[parts.size()]));
		}
	}
}
