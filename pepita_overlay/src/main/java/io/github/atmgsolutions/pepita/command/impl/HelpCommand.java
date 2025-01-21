package io.github.atmgsolutions.pepita.command.impl;

import io.github.atmgsolutions.pepita.PepitaOverlay;
import io.github.atmgsolutions.pepita.command.Command;
import io.github.atmgsolutions.pepita.manager.CommandManager;
import io.github.atmgsolutions.pepita.module.Module;
import io.github.atmgsolutions.pepita.util.client.ClientUtil;

public class HelpCommand extends Command {
	public HelpCommand() {
		super("help", new String[]{"h", "list"});
	}

	@Override
	public void onExecute(String[] args) {
		for (Module module : PepitaOverlay.getModuleManager().getModules()) {
			ClientUtil.sendMessage(CommandManager.PREFIX + module.getName());
		}
	}
}
