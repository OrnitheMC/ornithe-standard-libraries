package net.ornithemc.osl.registries.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.storage.AlphaWorldStorage;
import net.minecraft.world.storage.WorldStorage;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.registries.impl.RegistriesEntrypoint;
import net.ornithemc.osl.registries.impl.access.ServerWorldAccess;
import net.ornithemc.osl.registries.impl.access.WorldStorageAccess;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World implements ServerWorldAccess {

	private ServerWorldMixin() {
		super(null, null, null, null, null, null, false);
	}

	@Conditional(minecraftVersion = @Version("<19w04a"))
	@Shadow
	private WorldStorage storage;

	@Conditional(minecraftVersion = @Version(">=19w04a"))
	@Shadow
	private AlphaWorldStorage f_63102719;

	@Override
	public WorldStorageAccess accessStorage() {
		if (RegistriesEntrypoint.OLD_WORLD_STORAGE) {
			if (this.storage instanceof WorldStorageAccess) {
				return (WorldStorageAccess) this.storage;
			}
		} else {
			if (this.f_63102719 instanceof WorldStorageAccess) {
				return (WorldStorageAccess) this.f_63102719;
			}
		}

		return null;
	}
}
