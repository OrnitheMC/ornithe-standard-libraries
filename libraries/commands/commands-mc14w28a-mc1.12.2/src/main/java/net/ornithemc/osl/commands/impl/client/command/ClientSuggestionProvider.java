package net.ornithemc.osl.commands.impl.client.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.living.player.LocalClientPlayerEntity;
import net.minecraft.client.network.PlayerInfo;
import net.minecraft.client.network.handler.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.HitResult;

import net.ornithemc.osl.commands.api.client.command.ClientCommandSource;
import net.ornithemc.osl.commands.api.command.SharedSuggestionProvider;
import net.ornithemc.osl.commands.api.command.argument.Vec2f;
import net.ornithemc.osl.commands.impl.network.RequestCommandSuggestionsPayload;
import net.ornithemc.osl.networking.api.PacketPayload;
import net.ornithemc.osl.networking.api.client.ClientPlayNetworking;
import net.ornithemc.osl.text.api.Formatting;
import net.ornithemc.osl.text.api.TextComponent;
import net.ornithemc.osl.text.api.TextComponents;

public class ClientSuggestionProvider implements ClientCommandSource, SharedSuggestionProvider {

	private final Minecraft minecraft;
	private final ClientPlayNetworkHandler networkHandler;

	private int pendingSuggestionsId = -1;
	private CompletableFuture<Suggestions> pendingSuggestions;

	public ClientSuggestionProvider(Minecraft minecraft, ClientPlayNetworkHandler networkHandler) {
		this.minecraft = minecraft;
		this.networkHandler = networkHandler;
	}

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
	public Vec3d getPosition() {
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

	@Override
	public Collection<String> getPlayerNames() {
		List<String> playerNames = new ArrayList<>();

		for (PlayerInfo player : this.networkHandler.getOnlinePlayers()) {
			playerNames.add(player.getProfile().getName());
		}

		return playerNames;
	}

	@Override
	public Collection<String> getSelectedEntities() {
		if (this.minecraft.crosshairTarget != null && this.minecraft.crosshairTarget.type == HitResult.Type.ENTITY) {
			return Collections.singleton(this.minecraft.crosshairTarget.entity.getUuidString());
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public Collection<String> getTeams() {
		return this.minecraft.world.getScoreboard().getTeamNames();
	}

	@Override
	public boolean hasPermissions(int permissions) {
		LocalClientPlayerEntity player = this.minecraft.player;
		return player == null ? (permissions == 0) : player.hasPermissions(permissions);
	}

	@Override
	public CompletableFuture<Suggestions> requestSuggestions(CommandContext<SharedSuggestionProvider> ctx, SuggestionsBuilder builder) {
		if (this.pendingSuggestions != null) {
			this.pendingSuggestions.cancel(false);
		}

		this.pendingSuggestions = new CompletableFuture<>();

		int id = ++this.pendingSuggestionsId;
		PacketPayload payload = new RequestCommandSuggestionsPayload(id, ctx.getInput());

		ClientPlayNetworking.send(RequestCommandSuggestionsPayload.CHANNEL, payload);

		return this.pendingSuggestions;
	}

	private static String formatDouble(double value) {
		return String.format(Locale.ROOT, "%.2f", value);
	}

	private static String formatInt(int value) {
		return Integer.toString(value);
	}

	@Override
	public Collection<TextCoordinates> getRelevantCoordinates(boolean absolute) {
		if (this.minecraft.crosshairTarget == null || this.minecraft.crosshairTarget.type != HitResult.Type.BLOCK) {
			return Collections.singleton(TextCoordinates.DEFAULT_GLOBAL);
		} else if (absolute) {
			Vec3d facePos = this.minecraft.crosshairTarget.facePos;

			return Collections.singleton(new TextCoordinates(
				formatDouble(facePos.x),
				formatDouble(facePos.y),
				formatDouble(facePos.z)
			));
		} else {
			BlockPos pos = this.minecraft.crosshairTarget.getPos();

			return Collections.singleton(new TextCoordinates(
				formatInt(pos.getX()),
				formatInt(pos.getY()),
				formatInt(pos.getZ())
			));
		}
	}

	public void completeSuggestions(int id, Suggestions suggestions) {
		if (id == this.pendingSuggestionsId) {
			this.pendingSuggestions.complete(suggestions);
			this.pendingSuggestions = null;
			this.pendingSuggestionsId = -1;
		}
	}
}
