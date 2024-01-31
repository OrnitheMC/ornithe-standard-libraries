package net.ornithemc.osl.commands.api.server;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.BinaryOperator;

import com.mojang.brigadier.ResultConsumer;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Formatting;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

import net.ornithemc.osl.commands.api.AbstractCommandSourceStack;
import net.ornithemc.osl.commands.api.BrigadierCommandSource;
import net.ornithemc.osl.commands.api.argument.AnchorArgument;

public class CommandSourceStack extends AbstractCommandSourceStack<CommandSourceStack> {

	private final ServerWorld world;
	private final MinecraftServer server;

	public CommandSourceStack(BrigadierCommandSource source, Vec3d pos, Vec2f rotation, ServerWorld world, int permissions, String name, Text displayName, MinecraftServer server, Entity entity) {
		this(source, pos, rotation, world, permissions, name, displayName, server, entity, false, (ctx, success, result) -> { }, AnchorArgument.Anchor.FEET);
	}

	public CommandSourceStack(BrigadierCommandSource source, Vec3d pos, Vec2f rotation, ServerWorld world, int permissions, String name, Text displayName, MinecraftServer server, Entity entity, boolean silent, ResultConsumer<CommandSourceStack> callback, AnchorArgument.Anchor anchor) {
		super(source, pos, rotation, permissions, name, displayName, entity, silent, callback, anchor);

		this.world = world;
		this.server = server;
	}

	@Override
	public CommandSourceStack withEntity(Entity entity) {
		if (this.entity == entity) {
			return this;
		}

		return new CommandSourceStack(this.source, this.pos, this.rotation, this.world, this.permissions, entity.getName(), entity.getDisplayName(), this.server, entity, this.silent, this.callback, this.anchor);
	}

	@Override
	public CommandSourceStack withPos(Vec3d pos) {
		if (this.pos.equals(pos)) {
			return this;
		}

		return new CommandSourceStack(this.source, pos, this.rotation, this.world, this.permissions, this.name, this.displayName, this.server, this.entity, this.silent, this.callback, this.anchor);
	}

	@Override
	public CommandSourceStack withRotation(Vec2f rotation) {
		if (this.rotation.equals(rotation)) {
			return this;
		}

		return new CommandSourceStack(this.source, this.pos, rotation, this.world, this.permissions, this.name, this.displayName, this.server, this.entity, this.silent, this.callback, this.anchor);
	}

	@Override
	public CommandSourceStack withCallback(ResultConsumer<CommandSourceStack> callback) {
		if (this.callback.equals(callback)) {
			return this;
		}

		return new CommandSourceStack(this.source, this.pos, this.rotation, this.world, this.permissions, this.name, this.displayName, this.server, this.entity, this.silent, callback, this.anchor);
	}

	@Override
	public CommandSourceStack withCallback(ResultConsumer<CommandSourceStack> callback, BinaryOperator<ResultConsumer<CommandSourceStack>> chooser) {
		return this.withCallback(chooser.apply(this.callback, callback));
	}

	@Override
	public CommandSourceStack silent() {
		if (this.silent) {
			return this;
		}

		return new CommandSourceStack(this.source, this.pos, this.rotation, this.world, this.permissions, this.name, this.displayName, this.server, this.entity, true, this.callback, this.anchor);
	}

	@Override
	public CommandSourceStack withPermissions(int permissions) {
		if (permissions == this.permissions) {
			return this;
		}

		return new CommandSourceStack(this.source, this.pos, this.rotation, this.world, permissions, this.name, this.displayName, this.server, this.entity, this.silent, this.callback, this.anchor);
	}

	@Override
	public CommandSourceStack withMaxPermissions(int maxPermissions) {
		if (maxPermissions <= this.permissions) {
			return this;
		}

		return new CommandSourceStack(this.source, this.pos, this.rotation, this.world, maxPermissions, this.name, this.displayName, this.server, this.entity, this.silent, this.callback, this.anchor);
	}

	@Override
	public CommandSourceStack withAnchor(AnchorArgument.Anchor anchor) {
		if (anchor == this.anchor) {
			return this;
		}

		return new CommandSourceStack(this.source, this.pos, this.rotation, this.world, this.permissions, this.name, this.displayName, this.server, this.entity, this.silent, this.callback, anchor);
	}

	public CommandSourceStack withWorld(ServerWorld world) {
		if (world == this.world) {
			return this;
		}

		return new CommandSourceStack(this.source, this.pos, this.rotation, world, this.permissions, this.name, this.displayName, this.server, this.entity, this.silent, this.callback, this.anchor);
	}

	@Override
	public CommandSourceStack withFacing(Entity entity, AnchorArgument.Anchor anchor) throws CommandSyntaxException {
		return this.withFacing(anchor.apply(entity));
	}

	@Override
	public CommandSourceStack withFacing(Vec3d target) throws CommandSyntaxException {
		Vec3d anchorPos = this.anchor.apply(this);

		double dx = target.x - anchorPos.x;
		double dy = target.y - anchorPos.y;
		double dz = target.z - anchorPos.z;
		double squaredDistance = MathHelper.sqrt(dx * dx + dz * dz);

		float rotX = MathHelper.wrapDegrees((float)(-(MathHelper.fastAtan2(dy, squaredDistance) * 57.2957763671875)));
		float rotY = MathHelper.wrapDegrees((float)(MathHelper.fastAtan2(dz, dx) * 57.2957763671875) - 90.0f);

		return this.withRotation(new Vec2f(rotX, rotY));
	}

	@Override
	public ServerWorld getWorld() {
		return this.world;
	}

	public MinecraftServer getServer() {
		return this.server;
	}

	public void sendSuccess(Text message, boolean broadcastToOps) {
		if (this.source.sendCommandSuccess() && !this.silent) {
			this.source.sendMessage(message);
		}
		if (broadcastToOps && this.source.sendCommandSuccessToOps() && !this.silent) {
			this.broadcastToOps(message);
		}
	}

	private void broadcastToOps(Text message) {
		Text messageForOps = new TranslatableText("chat.type.admin", this.getDisplayName(), message).setStyle(new Style().setColor(Formatting.GRAY).setItalic(true));
		if (this.server.getSourceWorld().getGameRules().getBoolean("sendCommandFeedback")) {
			for (ServerPlayerEntity serverPlayerEntity : this.server.getPlayerManager().getAll()) {
				if (serverPlayerEntity != this.source && this.server.getPlayerManager().isOp(serverPlayerEntity.getGameProfile())) {
					serverPlayerEntity.sendMessage(messageForOps);
				}
			}
		}
		if (this.source != this.server && this.server.getSourceWorld().getGameRules().getBoolean("logAdminCommands")) {
			this.server.sendMessage(messageForOps);
		}
	}

	@Override
	public Collection<String> getPlayerNames() {
		return Arrays.asList(this.server.getPlayerNames());
	}
}
