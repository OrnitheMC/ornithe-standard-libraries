package net.ornithemc.osl.commands.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import net.ornithemc.osl.commands.api.client.ClientCommandSourceStack;
import net.ornithemc.osl.commands.impl.interfaces.mixin.IEntity;

@Mixin(Entity.class)
public abstract class EntityMixin implements CommandSource, IEntity {

	@Shadow private World world;
	@Shadow private double x;
	@Shadow private double y;
	@Shadow private double z;

	@Shadow private Vec2f getRotation() { return null; }

	@Shadow private int getPermissions() { return 0; }

	@Shadow private Text getName() { return null; }

	@Shadow private Text getDisplayName() { return null; }

	@Override
	public ClientCommandSourceStack osl$commands$createClientCommandSourceStack() {
		if (!(world instanceof ClientWorld)) {
			throw new IllegalStateException("cannot create a client command source stack on the server!");
		}

		return new ClientCommandSourceStack(this, new Vec3d(x, y, z), getRotation(), getPermissions(), getName().getString(), getDisplayName(), (Entity)(Object)this);
	}
}
