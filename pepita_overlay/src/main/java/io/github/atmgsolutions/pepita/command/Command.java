package io.github.atmgsolutions.pepita.command;

public abstract class Command {
	private final String name;

	private final String[] alias;

	public Command(String name, String[] alias) {
		this.name = name;
		this.alias = alias;
	}

	public Command(String name) {
		this.name = name;
		this.alias = null;
	}

	public String getName() {
		return name;
	}

	public String[] getAlias() {
		return alias;
	}

	public String getNameAlias() {
		StringBuilder temp = new StringBuilder();

		if (alias == null || alias.length == 0)
			return name;

		for (String name1 : alias) {
			temp.append(name1).append("/");
		}

		return temp.substring(0, temp.length() - 1);
	}

	public void onExecute(String[] args) {}
}
