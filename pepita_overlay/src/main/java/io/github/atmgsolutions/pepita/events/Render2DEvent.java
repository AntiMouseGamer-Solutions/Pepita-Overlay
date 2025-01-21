package io.github.atmgsolutions.pepita.events;

import net.lenni0451.asmevents.event.IEvent;

public class Render2DEvent implements IEvent {
	private final float partialTicks;

	public Render2DEvent(float partialTicks) {
		this.partialTicks = partialTicks;
	}

	public float getPartialTicks() {
		return partialTicks;
	}
}
