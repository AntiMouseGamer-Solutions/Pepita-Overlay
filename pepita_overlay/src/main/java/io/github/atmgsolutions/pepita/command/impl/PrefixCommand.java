package io.github.atmgsolutions.pepita.command.impl;

import io.github.atmgsolutions.pepita.command.Command;
import io.github.atmgsolutions.pepita.manager.CommandManager;
import io.github.atmgsolutions.pepita.util.client.ClientUtil;

public class PrefixCommand extends Command {
	public PrefixCommand() {
		super("prefix");
	}

	@Override
	public void onExecute(String[] args) {
		if (args.length == 0) {
			ClientUtil.sendMessage(String.format("Usage: %sprefix (new prefix)", CommandManager.PREFIX));
			return;
		}

		CommandManager.PREFIX = args[0];
		ClientUtil.sendMessage("Changed command prefix to: " + args[0]);
	}
}
