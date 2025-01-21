package io.github.atmgsolutions.pepita.util.http;

import com.google.gson.JsonObject;
import io.github.atmgsolutions.pepita.util.client.ClientUtil;
import io.github.atmgsolutions.pepita.util.player.PlayerInfo;
import net.minecraft.client.network.NetworkPlayerInfo;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.List;

import static io.github.atmgsolutions.pepita.util.Util.mc;

public class MushUtil {

	// abre a conexão e retorna ela.
	public static HttpURLConnection OpenConnectionAndGetConnection(String playerName) {
		try {
			String url = "https://mush.com.br/api/player/" + playerName;
			URL urlC = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) urlC.openConnection();

			conn.setRequestMethod("GET");

			return conn;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	// pega os status do player e retorna um PlayerInfo com as informações obtidas.
	public static PlayerInfo getPlayerStats(JsonObject playerObj, String name) {
		if (playerObj.get("error_code").getAsString().equals("404")) {
			ClientUtil.sendMessage("§7A Nicked Player is in your Queue: §e" + name);
			return new PlayerInfo(0, 0, "§7[0§l✫§7]", name, null, false, true);
		}

		JsonObject stats = null;
		JsonObject account = null;
		JsonObject bedwars = null;
		JsonObject levelBadge = null;
		JsonObject response = (JsonObject) playerObj.get("response");

		if (response.has("banned")) {
			if (response.get("banned").getAsBoolean()) {
				// staff aqui
				ClientUtil.sendMessage("§7A Staff Member is in your Queue: §e" + name);
				return new PlayerInfo(0, 0, "§7[0§l✫§7]", name, null, true, false);
			}
		}

		if (response.has("stats"))
			stats = (JsonObject) response.get("stats");

		if (playerObj.has("account"))
			account = (JsonObject) playerObj.get("account");

		if (stats != null) {
			if (stats.has("bedwars"))
				bedwars = (JsonObject) stats.get("bedwars");
		}

		if (bedwars != null) {
			if (bedwars.has("level_badge")) {
				levelBadge = (JsonObject) bedwars.get("level_badge");
			}
		}

		String level = "§7[0§l✫§7]";

		if (levelBadge != null) {
			level = levelBadge.get("format").getAsString().replace("&", "§");
		}

		String uniqueID = "-";

		if (account != null) {
			if (account.has("unique_id"))
				uniqueID = account.get("unique_id").getAsString();
		}

		int finalDeaths = 0;
		int finalKills = 0;
		int winstreak = 0;

		if (bedwars != null) {
			if (bedwars.has("final_deaths"))
				finalDeaths = bedwars.get("final_deaths").getAsInt();
		}

		if (bedwars != null) {
			if (bedwars.has("final_kills"))
				finalKills = bedwars.get("final_kills").getAsInt();
		}

		if (bedwars != null) {
			if (bedwars.has("winstreak"))
				winstreak = bedwars.get("winstreak").getAsInt();
		}
		float fkdr = 0;
		if (finalDeaths != 0 && finalKills != 0)
			fkdr = ((float) finalKills / finalDeaths);

		return new PlayerInfo(winstreak, (float) ClientUtil.round(fkdr, 2), level, name, readImage(uniqueID), false, false);
	}

	// pega a conexão e transforma a resposta em String
	public static String ReadResponse(HttpURLConnection connection) {
		try {
			if (connection == null) return null;

			StringBuilder resultJson = new StringBuilder();

			Scanner scanner = new Scanner(connection.getInputStream());

			while (scanner.hasNext()) {
				resultJson.append(scanner.nextLine());
			}

			scanner.close();

			return resultJson.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	// pega a head do player
	public static BufferedImage readImage(String UUID) {
		try {
			URL url = new URL("https://crafatar.com/avatars/" + UUID + "?overlay=true&size=8");

			return ImageIO.read(url);

		} catch (IOException ignored) {
		}
		return null;
	}

	public static List<NetworkPlayerInfo> getTablist() {
		final ArrayList<NetworkPlayerInfo> list = new ArrayList<>(mc.getNetHandler().getPlayerInfoMap());
		removeDuplicates(list);
		if (mc.thePlayer != null)
			list.remove(mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()));
		return list;
	}

	public static void removeDuplicates(final ArrayList list) {
		final HashSet set = new HashSet(list);
		list.clear();
		list.addAll(set);
	}
}
