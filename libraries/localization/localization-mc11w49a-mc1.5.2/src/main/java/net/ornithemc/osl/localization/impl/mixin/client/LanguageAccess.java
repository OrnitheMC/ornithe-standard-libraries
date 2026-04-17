package net.ornithemc.osl.localization.impl.mixin.client;

import java.util.Properties;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.locale.Language;

@Mixin(Language.class)
public interface LanguageAccess {

	@Accessor("translations")
	Properties accessTranslations();

}
