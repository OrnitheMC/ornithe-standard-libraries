package net.ornithemc.osl.crashreports.impl.mixin.client;

import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.world.MultiplayerWorld;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.crashreports.api.crash.CrashReport;
import net.ornithemc.osl.crashreports.api.crash.CrashReportSection;
import net.ornithemc.osl.crashreports.impl.mixin.common.WorldMixin;

@Conditional(minecraftVersion = @Version(">=a1.0.13"))
@Mixin(MultiplayerWorld.class)
public class MultiplayerWorldMixin extends WorldMixin {

	@Shadow
	private Set<?> forcedEntities;
	@Shadow
	private Set<?> pendingEntities;

	@Override
	public CrashReportSection populateCrashReport(CrashReport report) {
		CrashReportSection section = super.populateCrashReport(report);

		section.addDetail("Forced entities", () -> this.forcedEntities.size() + " total; " + this.forcedEntities);
		section.addDetail("Retry entities", () -> this.pendingEntities.size() + " total; " + this.pendingEntities);

		return section;
	}
}
