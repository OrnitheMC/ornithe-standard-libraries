package net.ornithemc.osl.text.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.Formatting;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;

import net.ornithemc.osl.text.impl.access.StyleAccess;

@Mixin(Style.class)
public abstract class StyleMixin implements StyleAccess {

	@Shadow
	private Formatting color;
	@Shadow
	private Boolean bold;
	@Shadow
	private Boolean italic;
	@Shadow
	private Boolean underlined;
	@Shadow
	private Boolean strikethrough;
	@Shadow
	private Boolean obfuscated;
	@Shadow
	private ClickEvent clickEvent;
	@Shadow
	private HoverEvent hoverEvent;

	@Override
	public Formatting osl$text$color() {
		return this.color;
	}

	@Override
	public Boolean osl$text$bold() {
		return this.bold;
	}

	@Override
	public Boolean osl$text$italic() {
		return this.italic;
	}

	@Override
	public Boolean osl$text$underlined() {
		return this.underlined;
	}

	@Override
	public Boolean osl$text$strikethrough() {
		return this.strikethrough;
	}

	@Override
	public Boolean osl$text$obfuscated() {
		return this.obfuscated;
	}

	@Override
	public ClickEvent osl$text$clickEvent() {
		return this.clickEvent;
	}

	@Override
	public HoverEvent osl$text$hoverEvent() {
		return this.hoverEvent;
	}
}
