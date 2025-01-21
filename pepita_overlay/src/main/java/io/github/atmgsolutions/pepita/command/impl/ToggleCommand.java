package io.github.atmgsolutions.pepita.command.impl;

import io.github.atmgsolutions.pepita.PepitaOverlay;
import io.github.atmgsolutions.pepita.command.Command;
import io.github.atmgsolutions.pepita.module.Module;
import io.github.atmgsolutions.pepita.util.client.ClientUtil;
import net.minecraft.util.EnumChatFormatting;

public class ToggleCommand extends Command {
	public ToggleCommand() {
		super("toggle", new String[]{"t"});
	}

	@Override
	public void onExecute(String[] args) {
		if (args.length == 0) {
			ClientUtil.sendMessage("Usage: .toggle (module name)");
			return;
		}

		if (args.length == 1) {
			for (Module module : PepitaOverlay.getModuleManager().getModules()) {
				if (module.getName().replace(" ", "").equalsIgnoreCase(args[0])) {
					module.toggle();
					ClientUtil.sendMessage(String.format("%s module has been %s%s", module.getName(), module.isEnabled() ? EnumChatFormatting.GREEN : EnumChatFormatting.RED, module.isEnabled() ? "enabled" : "disabled"));
					return;
				}
			}
		}
	}
}
