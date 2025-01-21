package io.github.atmgsolutions.pepita.setting;

public abstract class Setting<T> {
	private final String name;
	protected T value;

	public Setting(String name, T defaultValue) {
		this.name = name;
		value = defaultValue;
	}

	public String getName() {
		return name;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}
}
