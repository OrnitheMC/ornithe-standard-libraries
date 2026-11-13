package net.ornithemc.osl.commands.api.server.command;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.function.BinaryOperator;

import com.mojang.brigadier.ResultConsumer;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import net.ornithemc.osl.commands.api.command.CommandFailureConsumer;
import net.ornithemc.osl.commands.api.command.SharedSuggestionProvider;
import net.ornithemc.osl.commands.api.command.argument.AnchorArgument.Anchor;
import net.ornithemc.osl.text.api.TextColor;
import net.ornithemc.osl.text.api.TextComponent;
import net.ornithemc.osl.text.api.TextComponents;
import net.ornithemc.osl.commands.api.command.argument.Vec2f;

public class CommandSourceStack implements CommandFailureConsumer, SharedSuggestionProvider {

	public static final SimpleCommandExceptionType NOT_PLAYER_EXCEPTION = new SimpleCommandExceptionType(TextComponents.translatable("permissions.requires.player"));
	public static final SimpleCommandExceptionType NOT_ENTITY_EXCEPTION = new SimpleCommandExceptionType(TextComponents.translatable("permissions.requires.entity"));

	private final CommandSource source;
	private final MinecraftServer server;
	private final ServerWorld world;
	private final Entity entity;
	private final Vec3d pos;
	private final Vec2f rotation;
	private final Anchor anchor;
	private final String name;
	private final TextComponent displayName;
	private final int permissions;
	private final boolean silent;
	private final ResultConsumer<CommandSourceStack> callback;

	public CommandSourceStack(CommandSource source, MinecraftServer server, ServerWorld world, Entity entity, Vec3d pos, Vec2f rotation, String name, Text displayName,  int permissions) {
		this(source, server, world, entity, pos, rotation, name, TextComponents.resolve(displayName), permissions);
	}

	public CommandSourceStack(CommandSource source, MinecraftServer server, ServerWorld world, Entity entity, Vec3d pos, Vec2f rotation, String name, TextComponent displayName,  int permissions) {
		this(source, server, world, entity, pos, rotation, Anchor.FEET, name, displayName, permissions, false, (ctx, success, result) -> { });
	}

	protected CommandSourceStack(CommandSource source, MinecraftServer server, ServerWorld world, Entity entity, Vec3d pos, Vec2f rotation, Anchor anchor, String name,  TextComponent displayName, int permissions, boolean silent, ResultConsumer<CommandSourceStack> callback) {
		this.source = source;
		this.server = server;
		this.world = world;
		this.entity = entity;
		this.pos = pos;
		this.rotation = rotation;
		this.anchor = anchor;
		this.name = name;
		this.displayName = displayName;
		this.permissions = permissions;
		this.silent = silent;
		this.callback = callback;
	}

	public CommandSourceStack withEntity(Entity entity) {
		return this.entity == entity
			? this
			: new CommandSourceStack(this.source, this.server, this.world, entity, this.pos, this.rotation, this.anchor, entity.getName(), TextComponents.resolve(entity.getDisplayName()), this.permissions, this.silent, this.callback);
	}

	public CommandSourceStack withPos(Vec3d pos) {
		return this.pos.equals(pos)
			? this
			: new CommandSourceStack(this.source, this.server, this.world, this.entity, pos, this.rotation, this.anchor, this.name, this.displayName, this.permissions, this.silent, this.callback);
	}

	public CommandSourceStack withRotation(Vec2f rotation) {
		return this.rotation.equals(rotation)
			? this
			: new CommandSourceStack(this.source, this.server, this.world, this.entity, this.pos, rotation, this.anchor, this.name, this.displayName, this.permissions, this.silent, this.callback);
	}

	public CommandSourceStack withCallback(ResultConsumer<CommandSourceStack> callback) {
		return this.callback.equals(callback)
			? this
			: new CommandSourceStack(this.source, this.server, this.world, this.entity, this.pos, this.rotation, this.anchor, this.name, this.displayName, this.permissions, this.silent, callback);
	}

	public CommandSourceStack withCallback(ResultConsumer<CommandSourceStack> callback, BinaryOperator<ResultConsumer<CommandSourceStack>> chooser) {
		return this.withCallback(chooser.apply(this.callback, callback));
	}

	public CommandSourceStack silent() {
		return this.silent
			? this
			: new CommandSourceStack(this.source, this.server, this.world, this.entity, this.pos, this.rotation, this.anchor, this.name, this.displayName, this.permissions, true, this.callback);
	}

	public CommandSourceStack withPermissions(int permissions) {
		return permissions == this.permissions
			? this
			: new CommandSourceStack(this.source, this.server, this.world, this.entity, this.pos, this.rotation, this.anchor, this.name, this.displayName, permissions, this.silent, this.callback);
	}

	public CommandSourceStack withMaxPermissions(int maxPermissions) {
		return maxPermissions <= this.permissions
			? this
			: new CommandSourceStack(this.source, this.server, this.world, this.entity, this.pos, this.rotation, this.anchor, this.name, this.displayName, maxPermissions, this.silent, this.callback);
	}

	public CommandSourceStack withAnchor(Anchor anchor) {
		return anchor == this.anchor
			? this
			: new CommandSourceStack(this.source, this.server, this.world, this.entity, this.pos, this.rotation, anchor, this.name, this.displayName, this.permissions, this.silent, this.callback);
	}

	public CommandSourceStack withWorld(ServerWorld world) {
		return world == this.world
			? this
			: new CommandSourceStack(this.source, this.server, world, this.entity, this.pos, this.rotation, this.anchor, this.name, this.displayName, this.permissions, this.silent, this.callback);
	}

	public CommandSourceStack withFacing(Entity entity, Anchor anchor) throws CommandSyntaxException {
		return this.withFacing(anchor.apply(entity));
	}

	public CommandSourceStack withFacing(Vec3d rotation) throws CommandSyntaxException {
		Vec3d pos = this.anchor.apply(this);

		double dx = rotation.x - pos.x;
		double dy = rotation.y - pos.y;
		double dz = rotation.z - pos.z;
		double ds = MathHelper.sqrt(dx * dx + dz * dz);

		float pitch = MathHelper.wrapDegrees((float) (-(MathHelper.fastAtan2(dy, ds) * 180.0F / (float) Math.PI)));
		float yaw = MathHelper.wrapDegrees((float) (MathHelper.fastAtan2(dz, dx) * 180.0F / (float) Math.PI) - 90.0F);

		return this.withRotation(new Vec2f(pitch, yaw));
	}

	public MinecraftServer getServer() {
		return this.server;
	}

	public ServerWorld getWorld() {
		return this.world;
	}

	public Entity getEntity() {
		return this.entity;
	}

	public Entity getEntityOrThrow() throws CommandSyntaxException {
		if (this.entity == null) {
			throw NOT_ENTITY_EXCEPTION.create();
		}

		return this.entity;
	}

	public ServerPlayerEntity getPlayerOrThrow() throws CommandSyntaxException {
		if (!(this.entity instanceof ServerPlayerEntity)) {
			throw NOT_PLAYER_EXCEPTION.create();
		}

		return (ServerPlayerEntity) this.entity;
	}

	public Vec3d getPos() {
		return this.pos;
	}

	public Vec2f getRotation() {
		return this.rotation;
	}

	public Anchor getAnchor() {
		return this.anchor;
	}

	public String getName() {
		return this.name;
	}

	public TextComponent getDisplayName() {
		return this.displayName;
	}

	@Override
	public boolean hasPermissions(int permissions) {
		return this.permissions >= permissions;
	}

	public void sendSuccess(TextComponent message, boolean broadcastToOps) {
		if (/*this.source.sendCommandSuccess() && */!this.silent) {
			this.source.sendMessage(message);
		}

		if (broadcastToOps && this.source.sendCommandSuccessToOps() && !this.silent) {
			this.broadcastToOps(message);
		}
	}

	private void broadcastToOps(TextComponent message) {
		TextComponent adminMessage = TextComponents.translatable("chat.type.admin", this.getDisplayName(), message).format(style -> style.withColor(TextColor.GRAY).withItalic(true));

		if (this.world.getGameRules().getBoolean("sendCommandFeedback")) {
			for (ServerPlayerEntity player : this.server.getPlayerManager().getAll()) {
				if (player != this.source && this.server.getPlayerManager().isOp(player.getGameProfile())) {
					player.sendMessage(adminMessage);
				}
			}
		}

		if (this.source != this.server && this.world.getGameRules().getBoolean("logAdminCommands")) {
			this.server.sendMessage(adminMessage);
		}
	}

	@Override
	public void sendFailure(TextComponent message) {
		if (/*this.source.sendCommandFailure() && */!this.silent) {
			this.source.sendMessage(message.copy().format(style -> style.withColor(TextColor.RED)));
		}
	}

	public void onCommandComplete(CommandContext<CommandSourceStack> ctx, boolean success, int result) {
		if (this.callback != null) {
			this.callback.onCommandComplete(ctx, success, result);
		}
	}

	@Override
	public Collection<String> getPlayerNames() {
		return Arrays.asList(this.server.getPlayerNames());
	}

	@Override
	public Collection<String> getTeams() {
		return this.world.getScoreboard().getTeamNames();
	}

	@Override
	public CompletableFuture<Suggestions> requestSuggestions(CommandContext<SharedSuggestionProvider> ctx, SuggestionsBuilder builder) {
		return null;
	}

	@Override
	public Collection<TextCoordinates> getRelevantCoordinates(boolean absolute) {
		return Collections.singleton(TextCoordinates.DEFAULT_GLOBAL);
	}
}
