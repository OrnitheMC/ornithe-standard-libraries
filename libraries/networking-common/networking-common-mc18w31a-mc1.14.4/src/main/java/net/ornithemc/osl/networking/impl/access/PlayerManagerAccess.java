package net.ornithemc.osl.networking.impl.access;

import java.util.List;

import net.minecraft.server.entity.living.player.ServerPlayerEntity;

public interface PlayerManagerAccess {

	List<ServerPlayerEntity> osl$networking$getAll();

}
