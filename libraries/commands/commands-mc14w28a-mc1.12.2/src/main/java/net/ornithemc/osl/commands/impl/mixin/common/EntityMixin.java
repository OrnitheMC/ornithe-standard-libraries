package net.ornithemc.osl.commands.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.entity.Entity;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.ornithemc.osl.commands.api.command.argument.Vec2f;
import net.ornithemc.osl.commands.api.entity.EntityExtension;
import net.ornithemc.osl.commands.api.server.command.CommandSourceStack;

@Mixin(Entity.class)
public abstract class EntityMixin implements CommandSource, EntityExtension {

	@Shadow
	private World world;
	@Shadow
	private double x;
	@Shadow
	private double y;
	@Shadow
	private double z;
	@Shadow
	private float pitch;
	@Shadow
	private float yaw;

	@Override
	public int getPermissions() {
		return 0;
	}

	@Override
	public boolean hasPermissions(int permissions) {
		return permissions == 0 || this.getPermissions() >= permissions;
	}

	@Override
	public CommandSourceStack createCommandSourceStack() {
		return new CommandSourceStack(
			this,
			((ServerWorld) this.world).getServer(),
			(ServerWorld) this.world,
			(Entity) (Object) this,
			new Vec3d(this.x, this.y, this.z),
			new Vec2f(this.pitch, this.yaw),
			this.getName(),
			this.getDisplayName(),
			this.getPermissions()
		);
	}
}
