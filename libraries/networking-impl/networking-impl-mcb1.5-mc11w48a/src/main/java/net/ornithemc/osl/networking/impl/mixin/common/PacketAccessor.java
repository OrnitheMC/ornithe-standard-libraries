package net.ornithemc.osl.networking.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.network.packet.Packet;

@Mixin(Packet.class)
public interface PacketAccessor {

	@Invoker("register")
	public static void register(int id, boolean s2c, boolean c2s, Class<? extends Packet> type) {
		throw new AssertionError();
	}
}
