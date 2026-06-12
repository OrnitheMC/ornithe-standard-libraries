package net.ornithemc.osl.localization.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.ornithemc.osl.localization.api.language.Language;

@Mixin(net.minecraft.client.resource.language.Language.class)
public class LanguageMixin implements Language {

	@Shadow
	private String code;
	@Shadow
	private String region;
	@Shadow
	private String name;
	@Shadow
	private boolean bidirectional;

	@Override
	public String code() {
		return this.code;
	}

	@Override
	public String name() {
		return this.name;
	}

	@Override
	public String region() {
		return this.region;
	}

	@Override
	public boolean bidirectional() {
		return this.bidirectional;
	}
}
