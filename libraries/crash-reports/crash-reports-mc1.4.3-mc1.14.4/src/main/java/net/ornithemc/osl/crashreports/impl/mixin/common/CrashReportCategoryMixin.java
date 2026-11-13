package net.ornithemc.osl.crashreports.impl.mixin.common;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import it.unimi.dsi.fastutil.objects.Object2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.core.impl.util.MinecraftVersion;
import net.ornithemc.osl.crashreports.api.crash.CrashReportDetail;
import net.ornithemc.osl.crashreports.impl.crash.ModifiableCrashReportSection;

@Mixin(net.minecraft.util.crash.CrashReportCategory.class)
public class CrashReportCategoryMixin implements ModifiableCrashReportSection {

	private static final boolean CRASH_REPORT_DETAIL_EXISTS = MinecraftVersion.resolve().compareTo("16w15b") >= 0;
	private static final boolean ADD_RETURNS_REPORT_CATEGORY = MinecraftVersion.resolve().compareTo("1.14.4-pre4") >= 0;

	@Shadow
	private List<?> entries;

	@Unique
	private Object2ReferenceMap<String, Object> entriesByKey;

	@Conditional(minecraftVersion = @Version("<16w15b"))
	@Shadow
	private void m_16534273(String key, Callable<?> detail) { }
	@Conditional(minecraftVersion = @Version(">=16w15b <1.14.4-pre4"))
	@Shadow
	private void m_16534273(String key, net.minecraft.util.crash.CrashReportDetail<?> detail) { }
	@Conditional(minecraftVersion = @Version(">=1.14.4-pre4"))
	@Shadow
	private net.minecraft.util.crash.CrashReportCategory add(String key, net.minecraft.util.crash.CrashReportDetail<?> detail) { return null; }
	@Conditional(minecraftVersion = @Version("<1.14.4-pre4"))
	@Shadow
	private void m_32997861(String key, Object detail) { }
	@Conditional(minecraftVersion = @Version(">=1.14.4-pre4"))
	@Shadow
	private net.minecraft.util.crash.CrashReportCategory add(String key, Object detail) { return null; }

	@Inject(
		method = "<init>",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$crashreports$init(CallbackInfo ci) {
		this.entriesByKey = new Object2ReferenceOpenHashMap<>();
	}

	@Conditional(minecraftVersion = @Version("<1.14.4-pre4"))
	@Inject(
		method = "m_32997861",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$crashreports$saveEntryIndex(String key, Object detail, CallbackInfo ci) {
		this.saveLastEntry(key);
	}

	@Conditional(minecraftVersion = @Version(">=1.14.4-pre4"))
	@Inject(
		method = "m_32997861",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$crashreports$saveEntryIndex(String key, Object detail, CallbackInfoReturnable<net.minecraft.util.crash.CrashReportCategory> cir) {
		this.saveLastEntry(key);
	}

	@Unique
	private void saveLastEntry(String key) {
		this.entriesByKey.put(key, this.entries.get(this.entries.size() - 1));
	}

	@Override
	public void addDetail(String key, CrashReportDetail<String> detail) {
		if (!CRASH_REPORT_DETAIL_EXISTS) {
			this.m_16534273(key, (Callable<?>) detail::call);
		} else if (!ADD_RETURNS_REPORT_CATEGORY) {
			this.m_16534273(key, (net.minecraft.util.crash.CrashReportDetail<?>) detail::call);
		} else {
			this.add(key, detail::call);
		}
	}
	@Override
	public void addDetail(String key, Object detail) {
		if (ADD_RETURNS_REPORT_CATEGORY) {
			this.add(key, detail);
		} else {
			this.m_32997861(key, detail);
		}
	}

	@Override
	public void addDetailError(String key, Throwable error) {
		this.addDetail(key, error);
	}

	@Override
	public void removeDetail(String key) {
		Object entry = this.entriesByKey.remove(key);

		if (entry != null) {
			this.entries.remove(entry);
		}
	}

	@Override
	public void replaceDetail(String oldKey, String newKey, CrashReportDetail<String> detail) {
		Object entry = this.entriesByKey.remove(oldKey);

		if (entry != null) {
			int index = this.entries.indexOf(entry);

			this.addDetail(newKey, detail); // also updates the map
			Collections.swap(this.entries, index, this.entries.size() - 1);
			this.entries.remove(this.entries.size() - 1);
		}
	}
}
