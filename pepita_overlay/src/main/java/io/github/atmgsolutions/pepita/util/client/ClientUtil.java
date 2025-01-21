package io.github.atmgsolutions.pepita.util.client;

import io.github.atmgsolutions.pepita.util.Util;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.awt.*;

public class ClientUtil extends Util {

	public static void sendMessage(String message) {
		String realMano = String.format("%s[%sPepita%s]%s %s",
				EnumChatFormatting.GRAY,
				EnumChatFormatting.GOLD,
				EnumChatFormatting.GRAY,
				EnumChatFormatting.RESET,
				message
		);

		mc.thePlayer.addChatMessage(new ChatComponentText(realMano));
	}

	public static void sendRawMessage(String message) {
		mc.thePlayer.addChatMessage(new ChatComponentText(message));
	}

	public static boolean nullCheck() {
		return mc.thePlayer != null && mc.theWorld != null;
	}

	public static Color getColor(String colorStr) {
		return new Color(
				Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
				Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
				Integer.valueOf( colorStr.substring( 5, 7 ), 16 ) );
	}

	public static double round(double n, int d) {
		if (d == 0) {
			return (double) Math.round(n);
		}
		else {
			double p = Math.pow(10.0D, (double) d);
			return (double) Math.round(n * p) / p;
		}
	}
}
