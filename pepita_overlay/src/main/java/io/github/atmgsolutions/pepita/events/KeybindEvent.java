package io.github.atmgsolutions.pepita.events;

import net.lenni0451.asmevents.event.IEvent;

public class KeybindEvent implements IEvent {
	private final int keybind;

	public KeybindEvent(int keybind) {
		this.keybind = keybind;
	}

	public int getKeybind() {
		return keybind;
	}
}
