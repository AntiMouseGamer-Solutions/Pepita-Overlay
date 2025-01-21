package io.github.atmgsolutions.pepita.events;

import net.lenni0451.asmevents.event.wrapper.CancellableEvent;

public class SendMessageEvent extends CancellableEvent {
	private final String message;

    public SendMessageEvent(String message) {
		this.message = message;
    }

	public String getMessage() {
		return message;
	}
}
