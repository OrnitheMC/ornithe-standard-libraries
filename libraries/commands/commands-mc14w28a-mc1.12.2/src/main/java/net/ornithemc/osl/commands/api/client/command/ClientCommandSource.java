package net.ornithemc.osl.commands.api.client.command;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.living.player.LocalClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;

import net.ornithemc.osl.commands.api.command.CommandFailureConsumer;
import net.ornithemc.osl.commands.api.command.SharedSuggestionProvider;
import net.ornithemc.osl.commands.api.command.argument.Vec2f;
import net.ornithemc.osl.text.api.TextComponent;

public interface ClientCommandSource extends CommandFailureConsumer, SharedSuggestionProvider {

	Minecraft getMinecraft();

	ClientWorld getWorld();

	LocalClientPlayerEntity getPlayer();

	Vec3d getPosition();

	Vec2f getRotation();

	void sendSuccess(TextComponent message);

}
