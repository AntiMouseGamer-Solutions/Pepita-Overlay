package io.github.atmgsolutions.pepita.mixin.impl.entity;

import io.github.atmgsolutions.pepita.events.PreUpdateEvent;
import io.github.atmgsolutions.pepita.events.SendMessageEvent;
import net.lenni0451.asmevents.EventManager;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.atmgsolutions.pepita.util.Util.mc;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP {

	@Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
	public void onSendChatMessage(String message, CallbackInfo ci) {
		if (EventManager.call(new SendMessageEvent(message)).isCancelled())
			ci.cancel();
	}

	@Inject(method = "onUpdate", at = @At("HEAD"))
	public void onPreUpdate(CallbackInfo ci) {
		if (mc.theWorld.isBlockLoaded(new BlockPos(mc.thePlayer.posX, 0.0D, mc.thePlayer.posZ))) {
			EventManager.call(new PreUpdateEvent());
		}
	}
}
