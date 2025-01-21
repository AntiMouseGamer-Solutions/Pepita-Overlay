package io.github.atmgsolutions.pepita.manager;

import io.github.atmgsolutions.pepita.command.Command;
import io.github.atmgsolutions.pepita.command.impl.BindCommand;
import io.github.atmgsolutions.pepita.command.impl.HelpCommand;
import io.github.atmgsolutions.pepita.command.impl.PrefixCommand;
import io.github.atmgsolutions.pepita.command.impl.ToggleCommand;
import io.github.atmgsolutions.pepita.events.SendMessageEvent;
import net.lenni0451.asmevents.EventManager;
import net.lenni0451.asmevents.event.EventTarget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class CommandManager {
	private final ArrayList<Command> commands = new ArrayList<>();
	public static String PREFIX = ".";

	public CommandManager() {
		EventManager.register(this);
	}

	public void register() {
		commands.addAll(Arrays.asList(
				new PrefixCommand(),
				new ToggleCommand(),
				new BindCommand(),
				new HelpCommand()
		));
	}

	public void addCommand(Command command) {
		Objects.requireNonNull(command);

		commands.add(command);
	}

	public ArrayList<Command> getCommands() {
		return commands;
	}

	@EventTarget
	public void onSendMessage(SendMessageEvent event) {
		if (event.getMessage().startsWith(PREFIX)) {
			event.setCancelled(true);

			String[] temp = event.getMessage().split(" ");
			String name = temp[0].substring(PREFIX.length());
			String[] args = Arrays.copyOfRange(temp, 1, temp.length);

			commands.forEach(command -> {
				if (name.equalsIgnoreCase(command.getName())) { // ta errado, o certo é Objects.equals(name, command.getName()); - suqqo & boru
					command.onExecute(args);
				} else {
					if (command.getAlias() == null || command.getAlias().length == 0)
						return;

					for (String aliasName : command.getAlias()) {
						if (name.equalsIgnoreCase(aliasName))
							command.onExecute(args);
					}
				}
			});
		}
	}
}
