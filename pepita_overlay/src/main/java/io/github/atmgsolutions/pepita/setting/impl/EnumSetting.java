package io.github.atmgsolutions.pepita.setting.impl;

import io.github.atmgsolutions.pepita.setting.Setting;

public class EnumSetting<E extends Enum<?>> extends Setting<E> {
	private int ordinal;

	public EnumSetting(String name, E defaultValue) {
		super(name, defaultValue);
	}

	public E[] getEnumList() {
		return (E[]) value.getClass().getEnumConstants();
	}

	public void setValue(Enum<?> value) {
		this.value = (E) value;
	}
}
