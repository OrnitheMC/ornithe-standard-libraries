package net.ornithemc.osl.text.api;

import java.lang.reflect.Type;
import java.util.Objects;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public final class Style {
	
	public static final Style EMPTY = new Style(null, null, null, null, null, null, null, null, null);

	private final TextColor color;
	private final Boolean bold;
	private final Boolean italic;
	private final Boolean underlined;
	private final Boolean strikethrough;
	private final Boolean obfuscated;
	private final String insertion;
	private final ClickEvent clickEvent;
	private final HoverEvent hoverEvent;

	private Style(
		TextColor color,
		Boolean bold,
		Boolean italic,
		Boolean underlined,
		Boolean strikethrough,
		Boolean obfuscated,
		String insertion,
		ClickEvent clickEvent,
		HoverEvent hoverEvent
	) {
		this.color = color;
		this.bold = bold;
		this.italic = italic;
		this.underlined = underlined;
		this.strikethrough = strikethrough;
		this.obfuscated = obfuscated;
		this.insertion = insertion;
		this.clickEvent = clickEvent;
		this.hoverEvent = hoverEvent;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Style)) {
			return false;
		}
		Style style = (Style) o;
		return this.color == style.color
			&& this.bold == style.bold
			&& this.italic == style.italic
			&& this.obfuscated == style.obfuscated
			&& this.strikethrough == style.strikethrough
			&& this.underlined == style.underlined
			&& Objects.equals(this.insertion, style.insertion)
			&& Objects.equals(this.clickEvent, style.clickEvent)
			&& Objects.equals(this.hoverEvent, style.hoverEvent);
	}

	public TextColor getColor() {
		return this.color;
	}

	public boolean isBold() {
		return this.bold == Boolean.TRUE;
	}

	public boolean isItalic() {
		return this.italic == Boolean.TRUE;
	}

	public boolean isStrikethrough() {
		return this.strikethrough == Boolean.TRUE;
	}

	public boolean isUnderlined() {
		return this.underlined == Boolean.TRUE;
	}

	public boolean isObfuscated() {
		return this.obfuscated == Boolean.TRUE;
	}

	public String getInsertion() {
		return this.insertion;
	}

	public ClickEvent getClickEvent() {
		return this.clickEvent;
	}

	public HoverEvent getHoverEvent() {
		return this.hoverEvent;
	}

	public boolean isEmpty() {
		return this == EMPTY;
	}

	private static <T> Style checkEmptyAfterChange(Style style, T oldValue, T newValue) {
		return oldValue != null && newValue == null && style.equals(EMPTY) ? EMPTY : style;
	}

	public Style withColor(int color) {
		return this.withColor(TextColor.of(color));
	}

	public Style withColor(Formatting formatting) {
		return this.withColor(TextColor.fromFormatting(formatting));
	}

	public Style withColor(TextColor color) {
		return Objects.equals(this.color, color)
			? this
			: checkEmptyAfterChange(
				new Style(
					color,
					this.bold,
					this.italic,
					this.underlined,
					this.strikethrough,
					this.obfuscated,
					this.insertion,
					this.clickEvent,
					this.hoverEvent
				),
				this.color,
				color
			);
	}

	public Style withBold(Boolean bold) {
		return Objects.equals(this.bold, bold)
			? this
			: checkEmptyAfterChange(
				new Style(
					this.color,
					bold,
					this.italic,
					this.underlined,
					this.strikethrough,
					this.obfuscated,
					this.insertion,
					this.clickEvent,
					this.hoverEvent
				),
				this.bold,
				bold
			);
	}

	public Style withItalic(Boolean italic) {
		return Objects.equals(this.italic, italic)
			? this
			: checkEmptyAfterChange(
				new Style(
					this.color,
					this.bold,
					italic,
					this.underlined,
					this.strikethrough,
					this.obfuscated,
					this.insertion,
					this.clickEvent,
					this.hoverEvent
				),
				this.italic,
				italic
			);
	}

	public Style withUnderlined(Boolean underlined) {
		return Objects.equals(this.underlined, underlined)
			? this
			: checkEmptyAfterChange(
				new Style(
					this.color,
					this.bold,
					this.italic,
					underlined,
					this.strikethrough,
					this.obfuscated,
					this.insertion,
					this.clickEvent,
					this.hoverEvent
				),
				this.underlined,
				underlined
			);
	}

	public Style withStrikethrough(Boolean strikethrough) {
		return Objects.equals(this.strikethrough, strikethrough)
			? this
			: checkEmptyAfterChange(
				new Style(
					this.color,
					this.bold,
					this.italic,
					this.underlined,
					strikethrough,
					this.obfuscated,
					this.insertion,
					this.clickEvent,
					this.hoverEvent
				),
				this.strikethrough,
				strikethrough
			);
	}

	public Style withObfuscated(Boolean obfuscated) {
		return Objects.equals(this.obfuscated, obfuscated)
			? this
			: checkEmptyAfterChange(
				new Style(
					this.color,
					this.bold,
					this.italic,
					this.underlined,
					this.strikethrough,
					obfuscated,
					this.insertion,
					this.clickEvent,
					this.hoverEvent
				),
				this.obfuscated,
				obfuscated
			);
	}

	public Style withInsertion(String insertion) {
		return Objects.equals(this.insertion, insertion)
			? this
			: checkEmptyAfterChange(
				new Style(
					this.color,
					this.bold,
					this.italic,
					this.underlined,
					this.strikethrough,
					this.obfuscated,
					insertion,
					this.clickEvent,
					this.hoverEvent
				),
				this.insertion,
				insertion
			);
	}

	public Style withClickEvent(ClickEvent clickEvent) {
		return Objects.equals(this.clickEvent, clickEvent)
			? this
			: checkEmptyAfterChange(
				new Style(
					this.color,
					this.bold,
					this.italic,
					this.underlined,
					this.strikethrough,
					this.obfuscated,
					this.insertion,
					clickEvent,
					this.hoverEvent
				),
				this.clickEvent,
				clickEvent
			);
	}

	public Style withHoverEvent(HoverEvent hoverEvent) {
		return Objects.equals(this.hoverEvent, hoverEvent)
			? this
			: checkEmptyAfterChange(
				new Style(
					this.color,
					this.bold,
					this.italic,
					this.underlined,
					this.strikethrough,
					this.obfuscated,
					this.insertion,
					this.clickEvent,
					hoverEvent
				),
				this.hoverEvent,
				hoverEvent
			);
	}

	/**
	 * Applies the given formattings on top of this style and returns the result.
	 * @param formattings
	 * @return a new style with the given formattings applied on top of this style.
	 */
	public Style withFormatting(Formatting... formattings) {
		TextColor color = this.color;
		Boolean bold = this.bold;
		Boolean italic = this.italic;
		Boolean strikethrough = this.strikethrough;
		Boolean underlined = this.underlined;
		Boolean obfuscated = this.obfuscated;

		for (Formatting formatting : formattings) {
			switch (formatting) {
			case RESET:
				return EMPTY;
			case OBFUSCATED:
				obfuscated = true;
				break;
			case BOLD:
				bold = true;
				break;
			case STRIKETHROUGH:
				strikethrough = true;
				break;
			case UNDERLINED:
				underlined = true;
				break;
			case ITALIC:
				italic = true;
				break;
			default:
				if (formatting.isColor()) {
					color = TextColor.fromFormatting(formatting);
				}
			}
		}

		return new Style(
			color,
			bold,
			italic,
			underlined,
			strikethrough,
			obfuscated,
			this.insertion,
			this.clickEvent,
			this.hoverEvent
		);
	}

	/**
	 * Applies the given style on top of this style and returns the result.
	 * @param style
	 * @return a new style with the given style applied on top of this style.
	 */
	public Style withStyle(Style style) {
		if (style == EMPTY) {
			return this;
		}
		if (this == EMPTY) {
			return style;
		}
		return new Style(
			style.color != null ? style.color : this.color,
			style.bold != null ? style.bold : this.bold,
			style.italic != null ? style.italic : this.italic,
			style.underlined != null ? style.underlined : this.underlined,
			style.strikethrough != null ? style.strikethrough : this.strikethrough,
			style.obfuscated != null ? style.obfuscated : this.obfuscated,
			style.insertion != null ? style.insertion : this.insertion,
			style.clickEvent != null ? style.clickEvent : this.clickEvent,
			style.hoverEvent != null ? style.hoverEvent : this.hoverEvent
		);
	}

	/**
	 * Appends this style as formatting codes to the given {@code StringBuilder}.
	 * @param sb
	 */
	public void apply(StringBuilder sb) {
		if (this.color != null) {
			Formatting formatting = this.color.getFormatting();

			if (formatting != null) {
				sb.append(formatting);
			}
		}
		if (this.bold != null) {
			sb.append(Formatting.BOLD);
		}
		if (this.italic != null) {
			sb.append(Formatting.ITALIC);
		}
		if (this.underlined != null) {
			sb.append(Formatting.UNDERLINED);
		}
		if (this.strikethrough != null) {
			sb.append(Formatting.STRIKETHROUGH);
		}
		if (this.obfuscated != null) {
			sb.append(Formatting.OBFUSCATED);
		}
	}

	public static class Serializer implements JsonDeserializer<Style>, JsonSerializer<Style> {

		private static final String COLOR = "color";
		private static final String BOLD = "bold";
		private static final String ITALIC = "italic";
		private static final String UNDERLINED = "underlined";
		private static final String STRIKETHROUGH = "strikethrough";
		private static final String OBFUSCATED = "obfuscated";
		private static final String INSERTION = "insertion";
		private static final String CLICK_EVENT = "clickEvent";
		private static final String HOVER_EVENT = "hoverEveant";
		private static final String ACTION = "action";
		private static final String VALUE = "value";

		public Style deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
			if (!json.isJsonObject()) {
				return null;
			}

			JsonObject styleJson = json.getAsJsonObject();

			if (styleJson == null) {
				return null;
			}

			TextColor color = null;
			Boolean bold = null;
			Boolean italic = null;
			Boolean underlined = null;
			Boolean strikethrough = null;
			Boolean obfuscated = null;
			String insertion = null;
			ClickEvent clickEvent = null;
			HoverEvent hoverEvent = null;

			if (styleJson.has(COLOR)) {
				color = TextColor.fromFormatting(context.deserialize(styleJson.get(COLOR), Formatting.class));
			}
			if (styleJson.has(BOLD)) {
				bold = styleJson.get(BOLD).getAsBoolean();
			}
			if (styleJson.has(ITALIC)) {
				italic = styleJson.get(ITALIC).getAsBoolean();
			}
			if (styleJson.has(UNDERLINED)) {
				underlined = styleJson.get(UNDERLINED).getAsBoolean();
			}
			if (styleJson.has(STRIKETHROUGH)) {
				strikethrough = styleJson.get(STRIKETHROUGH).getAsBoolean();
			}
			if (styleJson.has(OBFUSCATED)) {
				obfuscated = styleJson.get(OBFUSCATED).getAsBoolean();
			}
			if (styleJson.has(INSERTION)) {
				insertion = styleJson.get(INSERTION).getAsString();
			}
			if (styleJson.has(CLICK_EVENT)) {
				JsonObject ceJson = styleJson.getAsJsonObject(CLICK_EVENT);
				if (ceJson.has(ACTION) && ceJson.has(VALUE)) {
					String actionName = ceJson.get(ACTION).getAsString();
					String value = ceJson.get(VALUE).getAsString();

					ClickEvent.Action action = ClickEvent.Action.byName(actionName);

					if (action != null) {
						clickEvent = new ClickEvent(action, value);
					}
				}
			}
			if (styleJson.has(HOVER_EVENT)) {
				JsonObject heJson = styleJson.getAsJsonObject(HOVER_EVENT);
				if (heJson.has(ACTION) && heJson.has(VALUE)) {
					String actionName = heJson.get(ACTION).getAsString();
					String value = heJson.get(VALUE).getAsString();

					HoverEvent.Action action = HoverEvent.Action.byName(actionName);

					if (action != null) {
						hoverEvent = new HoverEvent(action, value);
					}
				}
			}

			return new Style(
				color,
				bold,
				italic,
				underlined,
				strikethrough,
				obfuscated,
				insertion,
				clickEvent,
				hoverEvent
			);
		}

		public JsonElement serialize(Style style, Type type, JsonSerializationContext context) {
			if (style.isEmpty()) {
				return null;
			}

			JsonObject json = new JsonObject();

			if (style.bold != null) {
				json.addProperty(BOLD, style.bold);
			}
			if (style.italic != null) {
				json.addProperty(ITALIC, style.italic);
			}
			if (style.underlined != null) {
				json.addProperty(UNDERLINED, style.underlined);
			}
			if (style.strikethrough != null) {
				json.addProperty(STRIKETHROUGH, style.strikethrough);
			}
			if (style.obfuscated != null) {
				json.addProperty(OBFUSCATED, style.obfuscated);
			}
			if (style.color != null) {
				json.add(COLOR, context.serialize(style.color));
			}
			if (style.insertion != null) {
				json.add(INSERTION, context.serialize(style.insertion));
			}
			if (style.clickEvent != null) {
				JsonObject ceJson = new JsonObject();

				ceJson.addProperty(ACTION, style.clickEvent.getAction().name().toLowerCase());
				ceJson.addProperty(VALUE, style.clickEvent.getValue());

				json.add(CLICK_EVENT, ceJson);
			}

			if (style.hoverEvent != null) {
				JsonObject heJson = new JsonObject();

				heJson.addProperty(ACTION, style.hoverEvent.getAction().name().toLowerCase());
				heJson.add(VALUE, context.serialize(style.hoverEvent.getValue()));

				json.add(HOVER_EVENT, heJson);
			}

			return json;
		}
	}
}
