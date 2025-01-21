package io.github.atmgsolutions.pepita.module;

import io.github.atmgsolutions.pepita.PepitaOverlay;
import io.github.atmgsolutions.pepita.command.Command;
import io.github.atmgsolutions.pepita.module.annotions.ModuleInfo;
import io.github.atmgsolutions.pepita.setting.Setting;
import io.github.atmgsolutions.pepita.setting.impl.BooleanSetting;
import io.github.atmgsolutions.pepita.setting.impl.EnumSetting;
import io.github.atmgsolutions.pepita.setting.impl.NumberSetting;
import io.github.atmgsolutions.pepita.setting.impl.StringSetting;
import io.github.atmgsolutions.pepita.util.client.ClientUtil;
import net.lenni0451.asmevents.EventManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.math.NumberUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Module {
	private String name;

	private int keybind;

	private boolean enabled;

	private final ArrayList<Setting<?>> settings = new ArrayList<>();

	protected static final Minecraft mc = Minecraft.getMinecraft();

	protected final FontRenderer fr = mc.fontRendererObj;

	public Module() {
		if (getClass().isAnnotationPresent(ModuleInfo.class)) {
			ModuleInfo info = getClass().getAnnotation(ModuleInfo.class);

			name = info.name();
			keybind = info.keybind();
		}
	}

	public String getName() {
		return name;
	}

	public int getKeybind() {
		return keybind;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setKeybind(int keybind) {
		this.keybind = keybind;
	}

	public void registerSetting() {
		try {
			for (Field field : getClass().getDeclaredFields()) {
				if (!field.isAccessible())
					field.setAccessible(true);

				if (Setting.class.isAssignableFrom(field.getType())) {
					settings.add((Setting<?>) field.get(this));
				}
			}
		} catch (Exception e) {
			System.out.println("Failed to register settings");
		}
	}

	public void enable() {
		onEnable();
		EventManager.register(this);
		enabled = true;
	}

	public void disable() {
		onDisable();
		EventManager.unregister(this);
		enabled = false;
	}

	public void toggle() {
		if (!enabled)
			enable();
		else
			disable();
	}

	public void registerCommand() {
		PepitaOverlay.getCommandManager().addCommand(new Command(name) {
			@Override
			public void onExecute(String[] args) {
				if (args.length == 0) {
					ClientUtil.sendRawMessage(String.format("Module Bind: %s%s", EnumChatFormatting.GRAY, keybind));

					if (settings.isEmpty()) {
						ClientUtil.sendRawMessage(name + " module has no settings");
					} else {
						for (Setting<?> setting : settings) {
							if (setting instanceof BooleanSetting) {
								ClientUtil.sendRawMessage(String.format("%s: %s", setting.getName().replace(" ", "-"), (Boolean) setting.getValue() ? EnumChatFormatting.GREEN + "true" : EnumChatFormatting.RED + "false"));
							} if (setting instanceof StringSetting) {
								ClientUtil.sendRawMessage(String.format("%s: %s", setting.getName().replace(" ", "-"), setting.getValue()));
							} else {
								ClientUtil.sendRawMessage(String.format("%s: %s%s", setting.getName().replace(" ", "-"), EnumChatFormatting.GRAY, setting.getValue()));
							}
						}
					}

					return;
				}

				if (args.length == 1) {
					for (Setting<?> setting : settings) {
						if (setting.getName().replace(" ", "-").equalsIgnoreCase(args[0])) {
							if (setting instanceof BooleanSetting) {
								((BooleanSetting) setting).toggle();
								ClientUtil.sendMessage(String.format("%s setting changed to: %s%s", setting.getName(), (Boolean) setting.getValue() ? EnumChatFormatting.GREEN : EnumChatFormatting.RED, (Boolean) setting.getValue() ? "true" : "false"));
							} else if (setting instanceof EnumSetting) {
								StringBuilder temp = new StringBuilder();
								EnumSetting<? extends Enum<?>> set = (EnumSetting<? extends Enum<?>>) setting;

								for (Enum<?> enumm : set.getEnumList()) {
									temp.append(enumm.name()).append(" ");
								}

								String temp1 = temp.toString();
								String temp2 = ((Enum<?>) setting.getValue()).name();

								if (temp1.contains(temp2))
									temp1 = temp1.replace(temp2, EnumChatFormatting.GREEN + temp2 + EnumChatFormatting.GRAY);

								ClientUtil.sendMessage(String.format("%s setting values: %s%s", setting.getName(), EnumChatFormatting.GRAY, temp1));
							} else if (setting instanceof StringSetting){
								ClientUtil.sendMessage(String.format("%s setting value: %s", setting.getName(), setting.getValue()));
							} else {
								ClientUtil.sendMessage(String.format("%s setting value: %s%s", setting.getName(), EnumChatFormatting.GRAY, setting.getValue()));
							}

							return;
						}
					}

					return;
				}

				for (Setting<?> setting : settings) {
					if (setting.getName().replace(" ", "").equalsIgnoreCase(args[0])) {
						if (setting instanceof EnumSetting) {
							EnumSetting<? extends Enum<?>> set = (EnumSetting<? extends Enum<?>>) setting;

							for (Enum<?> enumm : set.getEnumList()) {
								if (enumm.name().equalsIgnoreCase(args[1])) {
									set.setValue(enumm);
								}
							}
						} else if (setting instanceof StringSetting) {
							StringSetting set = (StringSetting) setting;

							set.setValue(args[1]);
						} else if (setting instanceof NumberSetting) {
							NumberSetting<Number> set = (NumberSetting<Number>) setting;

							try {
								set.setValue(NumberUtils.createNumber(args[1]));
								ClientUtil.sendMessage(String.format("%s setting value changed to: %s", set.getName(), set.getValue()));
							} catch (Exception ignored) {
								ClientUtil.sendMessage("Invalid Value");
							}
						} else if (setting instanceof StringSetting) {
							// se mexer nisso aqui, vai morder o proprio pau, só avisando (sou belzebur e confirmo totalmente - belzebur, Amapá)
							List<String> fodaseTemp = Arrays.asList(Arrays.copyOfRange(args, 1, args.length));
							StringSetting set = (StringSetting) setting;
							StringBuilder vaisefuderArgs = new StringBuilder();

							for (String ata : fodaseTemp) {
								vaisefuderArgs.append(ata);

								if (fodaseTemp.size() > 1)
									vaisefuderArgs.append(" ");
							}

							set.setValue(vaisefuderArgs.substring(0, vaisefuderArgs.length() - 1));
							ClientUtil.sendMessage(String.format("%s setting value changed to: %s", set.getName(), set.getValue()));
						}
					}
				}
			}
		});
	}

	protected void onEnable() {}

	protected void onDisable() {}
}
