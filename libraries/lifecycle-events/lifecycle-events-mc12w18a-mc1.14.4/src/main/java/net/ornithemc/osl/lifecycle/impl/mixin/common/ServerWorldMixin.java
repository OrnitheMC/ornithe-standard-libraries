package net.ornithemc.osl.lifecycle.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.world.ServerWorld;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.lifecycle.api.server.ServerWorldEvents;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {

	@Conditional(minecraftVersion = @Version("<19w08a"))
	@Inject(
		method = "m_77531466",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$lifecycle$startTick1(CallbackInfo ci) {
		ServerWorldEvents.TICK_START.invoker().accept((ServerWorld)(Object)this);
	}

	@Conditional(minecraftVersion = @Version("<19w08a"))
	@Inject(
		method = "m_77531466",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$lifecycle$endTick1(CallbackInfo ci) {
		ServerWorldEvents.TICK_END.invoker().accept((ServerWorld)(Object)this);
	}

	@Conditional(minecraftVersion = @Version(">=19w08a"))
	@Inject(
		method = "tick",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$lifecycle$startTick2(CallbackInfo ci) {
		ServerWorldEvents.TICK_START.invoker().accept((ServerWorld)(Object)this);
	}

	@Conditional(minecraftVersion = @Version(">=19w08a"))
	@Inject(
		method = "tick",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$lifecycle$endTick2(CallbackInfo ci) {
		ServerWorldEvents.TICK_END.invoker().accept((ServerWorld)(Object)this);
	}
}
