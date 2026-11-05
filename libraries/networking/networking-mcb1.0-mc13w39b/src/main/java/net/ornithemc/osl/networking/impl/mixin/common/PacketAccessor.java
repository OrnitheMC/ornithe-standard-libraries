package net.ornithemc.osl.networking.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.network.packet.Packet;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

@Conditional(minecraftVersion = @Version("<11w49a"))
@Mixin(Packet.class)
public interface PacketAccessor {

	@Conditional(minecraftVersion = @Version("<b1.5"))
	@Invoker("m_06518219")
	public static void register(int id, Class<? extends Packet> type) {
		throw new AssertionError();
	}

	@Conditional(minecraftVersion = @Version(">=b1.5"))
	@Invoker("register")
	public static void register(int id, boolean s2c, boolean c2s, Class<? extends Packet> type) {
		throw new AssertionError();
	}
}
