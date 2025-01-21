package io.github.atmgsolutions.pepita.command.impl;

import io.github.atmgsolutions.pepita.PepitaOverlay;
import io.github.atmgsolutions.pepita.command.Command;
import io.github.atmgsolutions.pepita.module.Module;
import io.github.atmgsolutions.pepita.util.client.ClientUtil;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Field;

public class BindCommand extends Command {
	public BindCommand() {
		super("bind");
	}

	@Override
	public void onExecute(String[] args) {
		if (args.length == 0) {
			ClientUtil.sendMessage("Usage: .bind (module name) (key)");
			return;
		}

		if (args.length == 2) {
			for (Module module : PepitaOverlay.getModuleManager().getModules()) {
				if (module.getName().replace(" ", "").equalsIgnoreCase(args[0])) {
					for (Field field : Keyboard.class.getDeclaredFields()) {
						if (field.getName().startsWith("KEY_")) {
							String keyName = field.getName().replace("KEY_", "");

							if (keyName.equalsIgnoreCase(args[1])) {
								try {
									module.setKeybind(field.getInt(null));
								} catch (IllegalAccessException ignored) {}

								ClientUtil.sendMessage(String.format("%s module changed keybind to: %s", module.getName(), args[1].toLowerCase()));
								return;
							}
						}
					}

					ClientUtil.sendMessage("Key name not found, setting to none");
					module.setKeybind(0);

					return;
				}
			}

			ClientUtil.sendMessage("Module name not found");
		}
	}
}
