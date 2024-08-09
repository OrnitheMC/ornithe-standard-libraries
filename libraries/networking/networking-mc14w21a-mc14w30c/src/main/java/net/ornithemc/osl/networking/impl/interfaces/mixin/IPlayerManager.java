package net.ornithemc.osl.networking.impl.interfaces.mixin;

import java.util.List;

import net.minecraft.server.entity.living.player.ServerPlayerEntity;

public interface IPlayerManager {

	// a getAll method does exist but only in 1.8.1-pre3 and above

	List<ServerPlayerEntity> osl$networking$getAll();

}
