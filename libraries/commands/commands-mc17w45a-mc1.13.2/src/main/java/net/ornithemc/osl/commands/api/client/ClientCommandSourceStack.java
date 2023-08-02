package net.ornithemc.osl.commands.api.client;

import com.mojang.brigadier.ResultConsumer;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BinaryOperator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.living.player.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.command.SuggestionProvider;
import net.minecraft.command.argument.AnchorArgument;
import net.minecraft.command.argument.AnchorArgument.Anchor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.resource.Identifier;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.server.command.source.CommandSourceStack;
import net.minecraft.text.Formatting;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;

import net.ornithemc.osl.commands.impl.interfaces.mixin.IAnchor;

public class ClientCommandSourceStack implements SuggestionProvider {

	private final CommandSource source;
	private final Vec3d pos;
	private final ClientWorld world;
	private final int permissions;
	private final String name;
	private final Text displayName;
	private final Minecraft minecraft;
	private final boolean silent;
	private final Entity entity;
	private final ResultConsumer<ClientCommandSourceStack> callback;
	private final Anchor anchor;
	private final Vec2f rotation;

	public ClientCommandSourceStack(CommandSource source, Vec3d pos, Vec2f rotation, int permissions, String name, Text displayName, Entity entity) {
		this(source, pos, rotation, permissions, name, displayName, entity, false, (ctx, success, result) -> { }, AnchorArgument.Anchor.FEET);
	}

	protected ClientCommandSourceStack(CommandSource source, Vec3d pos, Vec2f rotation, int permissions, String name, Text displayName, Entity entity, boolean silent, ResultConsumer<ClientCommandSourceStack> callback, Anchor anchor) {
		Minecraft minecraft = Minecraft.getInstance();

		this.source = source;
		this.pos = pos;
		this.world = minecraft.world;
		this.silent = silent;
		this.entity = entity;
		this.permissions = permissions;
		this.name = name;
		this.displayName = displayName;
		this.minecraft = minecraft;
		this.callback = callback;
		this.anchor = anchor;
		this.rotation = rotation;
	}

	public ClientCommandSourceStack withEntity(Entity entity) {
		if (this.entity == entity) {
			return this;
		}

		return new ClientCommandSourceStack(this.source, this.pos, this.rotation, this.permissions, entity.getName().getString(), entity.getDisplayName(), entity, this.silent, this.callback, this.anchor);
	}

	public ClientCommandSourceStack withPos(Vec3d pos) {
		if (this.pos.equals(pos)) {
			return this;
		}

		return new ClientCommandSourceStack(this.source, pos, this.rotation, this.permissions, this.name, this.displayName, this.entity, this.silent, this.callback, this.anchor);
	}

	public ClientCommandSourceStack withRotation(Vec2f rotation) {
		if (this.rotation.equals(rotation)) {
			return this;
		}

		return new ClientCommandSourceStack(this.source, this.pos, rotation, this.permissions, this.name, this.displayName, this.entity, this.silent, this.callback, this.anchor);
	}

	public ClientCommandSourceStack withCallback(ResultConsumer<ClientCommandSourceStack> callback) {
		if (this.callback.equals(callback)) {
			return this;
		}

		return new ClientCommandSourceStack(this.source, this.pos, this.rotation, this.permissions, this.name, this.displayName, this.entity, this.silent, callback, this.anchor);
	}

	public ClientCommandSourceStack withCallback(ResultConsumer<ClientCommandSourceStack> callback, BinaryOperator<ResultConsumer<ClientCommandSourceStack>> chooser) {
		return this.withCallback(chooser.apply(this.callback, callback));
	}

	public ClientCommandSourceStack silent() {
		if (this.silent) {
			return this;
		}

		return new ClientCommandSourceStack(this.source, this.pos, this.rotation, this.permissions, this.name, this.displayName, this.entity, true, this.callback, this.anchor);
	}

	public ClientCommandSourceStack withPermissions(int permissions) {
		if (permissions == this.permissions) {
			return this;
		}

		return new ClientCommandSourceStack(this.source, this.pos, this.rotation, permissions, this.name, this.displayName, this.entity, this.silent, this.callback, this.anchor);
	}

	public ClientCommandSourceStack withMaxPermissions(int maxPermissions) {
		if (maxPermissions <= this.permissions) {
			return this;
		}

		return new ClientCommandSourceStack(this.source, this.pos, this.rotation, maxPermissions, this.name, this.displayName, this.entity, this.silent, this.callback, this.anchor);
	}

	public ClientCommandSourceStack withAnchor(Anchor anchor) {
		if (anchor == this.anchor) {
			return this;
		}

		return new ClientCommandSourceStack(this.source, this.pos, this.rotation, this.permissions, this.name, this.displayName, this.entity, this.silent, this.callback, anchor);
	}

	public ClientCommandSourceStack withFacing(Entity entity, Anchor anchor) throws CommandSyntaxException {
		return this.withFacing(anchor.apply(entity));
	}

	public ClientCommandSourceStack withFacing(Vec3d target) throws CommandSyntaxException {
		IAnchor anchor = (IAnchor)(Object)this.anchor;
		Vec3d anchorPos = anchor.osl$commands$apply(this);

		double dx = target.x - anchorPos.x;
		double dy = target.y - anchorPos.y;
		double dz = target.z - anchorPos.z;
		double squaredDistance = MathHelper.sqrt(dx * dx + dz * dz);

		float rotX = MathHelper.wrapDegrees((float)(-(MathHelper.fastAtan2(dy, squaredDistance) * 57.2957763671875)));
		float rotY = MathHelper.wrapDegrees((float)(MathHelper.fastAtan2(dz, dx) * 57.2957763671875) - 90.0f);

		return this.withRotation(new Vec2f(rotX, rotY));
	}

	public Text getDisplayName() {
		return this.displayName;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public boolean hasPermissions(int permissions) {
		return this.permissions >= permissions;
	}

	public Vec3d getPos() {
		return this.pos;
	}

	public ClientWorld getWorld() {
		return this.world;
	}

	public Entity getEntity() {
		return this.entity;
	}

	public Entity getEntityOrThrow() throws CommandSyntaxException {
		if (this.entity == null) {
			throw CommandSourceStack.NOT_ENTITY_EXCEPTION.create();
		}

		return this.entity;
	}

	public ClientPlayerEntity getPlayerOrThrow() throws CommandSyntaxException {
		if (!(this.entity instanceof ClientPlayerEntity)) {
			throw CommandSourceStack.NOT_PLAYER_EXCEPTION.create();
		}

		return (ClientPlayerEntity)this.entity;
	}

	public Vec2f getRotation() {
		return this.rotation;
	}

	public Minecraft getMinecraft() {
		return this.minecraft;
	}

	public Anchor getAnchor() {
		return this.anchor;
	}

	public void sendSuccess(Text message) {
		if (this.source.canUseCommand() && !this.silent) {
			this.source.sendMessage(message);
		}
	}

	public void sendFailure(Text message) {
		if (this.source.sendCommandFailure() && !this.silent) {
			this.source.sendMessage(new LiteralText("").append(message).setFormatting(Formatting.RED));
		}
	}

	public void onCommandComplete(CommandContext<ClientCommandSourceStack> ctx, boolean success, int result) {
		if (this.callback != null) {
			this.callback.onCommandComplete(ctx, success, result);
		}
	}

	@Override
	public Collection<String> getPlayerNames() {
		List<String> names = new ArrayList<>();
		for (PlayerEntity player : this.world.players) {
			names.add(player.getGameProfile().getName());
		}
		return names;
	}

	@Override
	public Collection<String> getTeams() {
		return this.world.getScoreboard().getTeamNames();
	}

	@Override
	public Collection<Identifier> getAvailableSounds() {
		return Registry.SOUND_EVENT.keySet();
	}

	@Override
	public Collection<Identifier> getAvailableRecipes() {
		return this.world.getCraftingManager().getLocations();
	}

	@Override
	public CompletableFuture<Suggestions> suggest(CommandContext<SuggestionProvider> ctx, SuggestionsBuilder builder) {
		return null;
	}

	@Override
	public Collection<Coordinate> getCoordinates(boolean absolute) {
		return Collections.singleton(Coordinate.DEFAULT_GLOBAL);
	}
}
