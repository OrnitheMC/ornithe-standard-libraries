package net.ornithemc.osl.commands.impl;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.BuiltInExceptionProvider;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.Formatting;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.profiler.Profiler;

import net.ornithemc.osl.commands.api.AbstractCommandSourceStack;

public class BrigadierCommandManagerImpl {

	public static <S extends AbstractCommandSourceStack<S>> Integer run(CommandDispatcher<S> dispatcher, S source, String command, Profiler profiler, boolean force) {
		StringReader reader = new StringReader(command);
		if (reader.canRead() && reader.peek() == '/') {
			reader.skip();
		}
		profiler.push(command);
		try {
			return dispatcher.execute(reader, source);
		} catch (CommandSyntaxException e) {
			if (!force && shouldIgnore(e.getType())) {
				return null;
			}
			if (e.getRawMessage() instanceof Text) {
				source.sendFailure((Text)e.getRawMessage());
			} else {
				source.sendFailure(new LiteralText(e.getRawMessage().getString()));
			}
			if (e.getInput() != null && e.getCursor() >= 0) {
				Text message = new LiteralText("")
					.setStyle(new Style()
						.setColor(Formatting.GRAY)
						.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)));
				int cursor = Math.min(e.getInput().length(), e.getCursor());
				if (cursor > 10) {
					message.append("...");
				}
				message.append(e.getInput().substring(Math.max(0, cursor - 10), cursor));
				if (cursor < e.getInput().length()) {
					message.append(new LiteralText(e.getInput().substring(cursor))
						.setStyle(new Style()
							.setColor(Formatting.RED)
							.setUnderlined(true)));
				}
				message.append(new TranslatableText("command.context.here")
					.setStyle(new Style()
						.setColor(Formatting.RED)
						.setItalic(true)));
				source.sendFailure(message);
			}
			return 0;
		} catch (Exception e) {
			LiteralText message = new LiteralText(e.getMessage() == null ? e.getClass().getName() : e.getMessage());
			/*if (LOGGER.isDebugEnabled()) {
				StackTraceElement[] stackTrace = e.getStackTrace();
				for (StackTraceElement element : stackTrace) {
					message
						.append("\n\n")
						.append(element.getMethodName())
						.append("\n ")
						.append(element.getFileName())
						.append(":")
						.append(String.valueOf(element.getLineNumber()));
				}
			}*/
			source.sendFailure(new TranslatableText("command.failed")
				.setStyle(new Style().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, message))));
			return 0;
		} finally {
			profiler.pop();
		}
	}

	private static boolean shouldIgnore(CommandExceptionType type) {
		BuiltInExceptionProvider exceptions = CommandSyntaxException.BUILT_IN_EXCEPTIONS;
		return type == exceptions.dispatcherUnknownCommand() || type == exceptions.dispatcherParseException();
	}
}
