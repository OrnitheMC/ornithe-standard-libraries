package net.ornithemc.osl.lifecycle.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import net.minecraft.Bootstrap;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.lifecycle.api.LifecycleEvents;

@Conditional(minecraftVersion = @Version(">=13w36a"))
@Mixin(Bootstrap.class)
public class BootstrapMixin {

	@Shadow
	private static boolean initialized;

	@WrapMethod(method = "init")
	private static void osl$lifecycle$bootstrap(Operation<Void> op) {
		boolean triggerEvents = !initialized;

		if (triggerEvents) {
			LifecycleEvents.BOOTSTRAP_START.invoker().run();
		}

		op.call();

		if (triggerEvents) {
			LifecycleEvents.BOOTSTRAP_END.invoker().run();
		}
	}
}
