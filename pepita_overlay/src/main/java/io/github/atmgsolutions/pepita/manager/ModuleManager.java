package io.github.atmgsolutions.pepita.manager;

import io.github.atmgsolutions.pepita.events.KeybindEvent;
import io.github.atmgsolutions.pepita.module.Module;
import io.github.atmgsolutions.pepita.module.impl.OverlayModule;
import net.lenni0451.asmevents.EventManager;
import net.lenni0451.asmevents.event.EventTarget;

import java.util.ArrayList;
import java.util.Arrays;

public class ModuleManager {
	private final ArrayList<Module> modules = new ArrayList<>();

	public ModuleManager() {
		EventManager.register(this);
	}

	public void register() {
		modules.addAll(Arrays.asList(
			new OverlayModule()
		));

		modules.forEach(module -> {
			module.registerSetting();
			module.registerCommand();
		});
	}

	public ArrayList<Module> getModules() {
		return modules;
	}

	@EventTarget
	public void onKeyboard(KeybindEvent event) {
		modules.forEach(module -> {
			if (module.getKeybind() == event.getKeybind())
				module.toggle();
		});
	}
}
