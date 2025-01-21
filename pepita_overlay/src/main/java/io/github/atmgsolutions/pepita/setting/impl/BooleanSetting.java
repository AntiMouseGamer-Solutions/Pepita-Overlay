package io.github.atmgsolutions.pepita.setting.impl;

import io.github.atmgsolutions.pepita.setting.Setting;

public class BooleanSetting extends Setting<Boolean> {
	public BooleanSetting(String name, Boolean defaultValue) {
		super(name, defaultValue);
	}

	public void toggle() {
		setValue(!value);
	}
}
