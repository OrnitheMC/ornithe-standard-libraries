package net.ornithemc.osl.crashreports.impl.mixin.common;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldData;
import net.minecraft.world.chunk.ChunkSource;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.core.impl.util.MinecraftVersion;
import net.ornithemc.osl.crashreports.api.crash.CrashListener;
import net.ornithemc.osl.crashreports.api.crash.CrashReport;
import net.ornithemc.osl.crashreports.api.crash.CrashReportSection;
import net.ornithemc.osl.crashreports.api.world.WorldExtension;
import net.ornithemc.osl.crashreports.impl.CrashReports;

@Mixin(World.class)
public class WorldMixin implements WorldExtension {

	private static final boolean WORLD_DATA_EXISTS = MinecraftVersion.resolve().compareTo("b1.3") >= 0;

	@Conditional(minecraftVersion = @Version("<b1.3"))
	@Shadow
	private String f_43322585;
	@Conditional(minecraftVersion = @Version(">=b1.3"))
	@Shadow
	private WorldData data;
	@Shadow
	private List<Entity> entities;
	@Shadow
	private List<PlayerEntity> players;
	@Shadow
	private ChunkSource chunkSource;

	@Unique
	private final List<CrashListener> crashListeners = new ArrayList<>();

	@Override
	public void addCrashListener(CrashListener listener) {
		this.crashListeners.add(listener);
	}

	@Override
	public CrashReportSection populateCrashReport(CrashReport report) {
		CrashReportSection section = report.addSection("Affected level");

		if (WORLD_DATA_EXISTS) {
			section.addDetail("Level name", () -> this.data == null ? "????" : this.data.getName());
		} else {
			section.addDetail("Level name", () -> this.f_43322585 == null ? "????" : this.f_43322585);
		}
		section.addDetail("All entities", () -> this.entities.size() + " total; " + this.entities);
		section.addDetail("All players", () -> this.players.size() + " total; " + this.players);
		section.addDetail("Chunk stats", () -> this.chunkSource.getDebugInfo());

		CrashReports.notifyListeners(report, section, this.crashListeners);

		return section;
	}
}
