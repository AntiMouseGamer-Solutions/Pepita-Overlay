package io.github.atmgsolutions.pepita.mixin.impl.client;

import io.github.atmgsolutions.pepita.PepitaOverlay;
import io.github.atmgsolutions.pepita.events.KeybindEvent;
import io.github.atmgsolutions.pepita.events.TickEvent;
import io.github.atmgsolutions.pepita.events.WorldLoadEvent;
import io.github.atmgsolutions.pepita.util.Util;
import io.github.atmgsolutions.pepita.util.client.ClientUtil;
import io.github.atmgsolutions.pepita.util.http.MushUtil;
import net.lenni0451.asmevents.EventManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.atmgsolutions.pepita.util.Util.mc;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Shadow
	public WorldClient theWorld;

	@Inject(method = "startGame", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;ingameGUI:Lnet/minecraft/client/gui/GuiIngame;", shift = At.Shift.AFTER))
    public void onStartGame(CallbackInfo ci) {
        PepitaOverlay.init();
    }

	@Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;dispatchKeypresses()V"))
	public void onRunTick(CallbackInfo ci) {
		int k = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey();

		if (Keyboard.getEventKeyState()) {
			EventManager.call(new KeybindEvent(k));
		}

	}
	@Inject(method = "runTick", at = @At(value = "HEAD"))
	public void onRunTick1(CallbackInfo ci) {
		if (mc.currentScreen == null || mc.currentScreen.allowUserInput) {
			EventManager.call(new TickEvent());
		}
	}

	@Inject(method = "loadWorld(Lnet/minecraft/client/multiplayer/WorldClient;)V", at = @At("HEAD"))
	public void onWorldLoadEvent(WorldClient worldClientIn, CallbackInfo ci) {
		if (worldClientIn != null) {
			EventManager.call(new WorldLoadEvent());
		}
	}
}
