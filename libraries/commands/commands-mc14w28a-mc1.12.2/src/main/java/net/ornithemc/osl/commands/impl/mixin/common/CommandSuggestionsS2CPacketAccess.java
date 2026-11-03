package net.ornithemc.osl.commands.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.network.packet.s2c.play.CommandSuggestionsS2CPacket;

@Mixin(CommandSuggestionsS2CPacket.class)
public interface CommandSuggestionsS2CPacketAccess {

	@Accessor("suggestions")
	String[] accessSuggestions();

}
