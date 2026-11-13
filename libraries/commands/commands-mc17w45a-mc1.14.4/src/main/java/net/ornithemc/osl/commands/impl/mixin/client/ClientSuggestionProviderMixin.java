package net.ornithemc.osl.commands.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.Minecraft;
import net.minecraft.client.command.ClientSuggestionProvider;
import net.minecraft.client.entity.living.player.LocalClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

import net.ornithemc.osl.commands.api.client.command.ClientCommandSource;
import net.ornithemc.osl.text.api.Formatting;
import net.ornithemc.osl.text.api.TextComponent;
import net.ornithemc.osl.text.api.TextComponents;

@Mixin(ClientSuggestionProvider.class)
public abstract class ClientSuggestionProviderMixin implements ClientCommandSource {

	@Shadow
	private Minecraft minecraft;

	@Override
	public Minecraft getMinecraft() {
		return this.minecraft;
	}

	@Override
	public ClientWorld getWorld() {
		return this.minecraft.world;
	}

	@Override
	public LocalClientPlayerEntity getPlayer() {
		return this.minecraft.player;
	}

	@Override
	public Vec3d getPos() {
		return new Vec3d(this.minecraft.player.x, this.minecraft.player.y, this.minecraft.player.z);
	}

	@Override
	public Vec2f getRotation() {
		return new Vec2f(this.minecraft.player.pitch, this.minecraft.player.yaw);
	}

	@Override
	public void sendSuccess(TextComponent message) {
		this.minecraft.gui.getChat().addMessage(TextComponents.convert(message, Text.class));
	}

	@Override
	public void sendFailure(TextComponent message) {
		this.minecraft.gui.getChat().addMessage(TextComponents.convert(message.copy().format(Formatting.RED), Text.class));
	}
}
