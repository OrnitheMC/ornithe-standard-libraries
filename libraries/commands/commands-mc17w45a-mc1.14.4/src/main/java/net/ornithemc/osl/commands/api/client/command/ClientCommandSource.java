package net.ornithemc.osl.commands.api.client.command;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.living.player.LocalClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.command.SuggestionProvider;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

import net.ornithemc.osl.text.api.TextComponent;

public interface ClientCommandSource extends SuggestionProvider {

	Minecraft getMinecraft();

	ClientWorld getWorld();

	LocalClientPlayerEntity getPlayer();

	Vec3d getPos();

	Vec2f getRotation();

	void sendSuccess(TextComponent message);

	void sendFailure(TextComponent message);

}
