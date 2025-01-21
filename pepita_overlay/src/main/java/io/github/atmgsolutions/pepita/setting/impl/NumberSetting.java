package io.github.atmgsolutions.pepita.setting.impl;

import io.github.atmgsolutions.pepita.setting.Setting;

public class NumberSetting<T extends Number> extends Setting<T> {
	private final T min, max;

	public NumberSetting(String name, T defaultValue, T min, T max) {
		super(name, defaultValue);
		this.min = min;
		this.max = max;
	}

	public T getMin() {
		return min;
	}

	public T getMax() {
		return max;
	}

	@Override
	public void setValue(T value) {
		if (value.doubleValue() > max.doubleValue()) {
			this.value = max;
		} else if (value.doubleValue() < min.doubleValue()) {
			this.value = min;
		} else {
			this.value = value;
		}
	}
}
