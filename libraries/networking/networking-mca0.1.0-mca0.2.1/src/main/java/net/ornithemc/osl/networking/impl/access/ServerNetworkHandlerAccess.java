package net.ornithemc.osl.networking.impl.access;

import net.minecraft.server.entity.mob.player.ServerPlayerEntity;

import net.ornithemc.osl.networking.impl.server.ServerConnectionContext;

public interface ServerNetworkHandlerAccess extends NetworkHandlerAccess {

	ServerPlayerEntity osl$networkin$player();

	ServerConnectionContext osl$networking$connectionContext();

}
