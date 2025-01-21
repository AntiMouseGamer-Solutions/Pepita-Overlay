package io.github.atmgsolutions.pepita;

import io.github.atmgsolutions.pepita.manager.CommandManager;
import io.github.atmgsolutions.pepita.manager.ModuleManager;

public class PepitaOverlay {
	private static final ModuleManager MODULE_MANAGER = new ModuleManager();
	private static final CommandManager COMMAND_MANAGER = new CommandManager();

	// como fazer uma overlay que ler latestlogs.txt, google pesquisar
    public static void init() {
		System.out.println("PepitaOverlay on top");
		System.out.println("Made By suqqo & viniciusroger");
		MODULE_MANAGER.register();
		COMMAND_MANAGER.register();
	}

	public static ModuleManager getModuleManager() {
		return MODULE_MANAGER;
	}

	public static CommandManager getCommandManager() {
		return COMMAND_MANAGER;
	}
}
