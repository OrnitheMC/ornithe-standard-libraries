package net.ornithemc.osl.crashreports.impl.mixin.client;

import java.util.concurrent.Callable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.crash.CrashReport;

import net.ornithemc.osl.crashreports.impl.mixin.common.WorldMixin;

@Mixin(ClientWorld.class)
public class ClientWorldMixin extends WorldMixin {

	@WrapOperation(
		method = "populateCrashReport",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/util/crash/CrashReport;add(Ljava/lang/String;Ljava/util/concurrent/Callable;)V"
		)
	)
	private void osl$crashreports$redirectDetailsToSection(CrashReport report, String key, Callable<String> detail, Operation<Void> op) {
		this.currentCrashReportSection.addDetail(key, detail::call);
	}
}
