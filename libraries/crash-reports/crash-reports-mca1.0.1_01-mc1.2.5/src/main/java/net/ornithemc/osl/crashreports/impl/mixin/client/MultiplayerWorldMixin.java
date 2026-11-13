package net.ornithemc.osl.crashreports.impl.mixin.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.client.world.MultiplayerWorld;

import net.ornithemc.osl.crashreports.api.crash.CrashListener;
import net.ornithemc.osl.crashreports.api.crash.CrashReport;
import net.ornithemc.osl.crashreports.api.crash.CrashReportSection;
import net.ornithemc.osl.crashreports.impl.mixin.common.WorldMixin;

@Mixin(MultiplayerWorld.class)
public class MultiplayerWorldMixin extends WorldMixin {

	@Shadow
	private Set<?> forcedEntities;
	@Shadow
	private Set<?> pendingEntities;

	@Unique
	private final List<CrashListener> crashListeners = new ArrayList<>();

	@Override
	public void addCrashListener(CrashListener listener) {
		this.crashListeners.add(listener);
	}

	@Override
	public CrashReportSection populateCrashReport(CrashReport report) {
		CrashReportSection section = super.populateCrashReport(report);

		section.addDetail("Forced entities", () -> this.forcedEntities.size() + " total; " + this.forcedEntities);
		section.addDetail("Retry entities", () -> this.pendingEntities.size() + " total; " + this.pendingEntities);

		return section;
	}
}
