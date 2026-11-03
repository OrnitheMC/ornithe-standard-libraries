package net.ornithemc.osl.commands.api.server;

import com.mojang.authlib.GameProfile;

public interface MinecraftServerExtension {

	int getPermissions(GameProfile profile);

}
