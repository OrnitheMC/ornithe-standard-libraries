package net.ornithemc.osl.networking.impl.interfaces.mixin;

public interface ICustomPayloadPacket {

	// channel/data getters do exist in the Vanilla classes,
	// but only in 13w41b and above, not in 13w41a

	String osl$networking$getChannel();

	byte[] osl$networking$getData();

}
