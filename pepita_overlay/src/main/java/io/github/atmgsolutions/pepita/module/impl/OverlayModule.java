package io.github.atmgsolutions.pepita.module.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.atmgsolutions.pepita.events.PreUpdateEvent;
import io.github.atmgsolutions.pepita.events.Render2DEvent;
import io.github.atmgsolutions.pepita.events.TickEvent;
import io.github.atmgsolutions.pepita.events.WorldLoadEvent;
import io.github.atmgsolutions.pepita.module.Module;
import io.github.atmgsolutions.pepita.module.annotions.ModuleInfo;
import io.github.atmgsolutions.pepita.setting.impl.BooleanSetting;
import io.github.atmgsolutions.pepita.setting.impl.NumberSetting;
import io.github.atmgsolutions.pepita.util.http.MushUtil;
import io.github.atmgsolutions.pepita.util.player.PlayerInfo;
import io.github.atmgsolutions.pepita.util.render.DrawUtil;
import net.lenni0451.asmevents.event.EventTarget;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

import java.net.HttpURLConnection;
import java.util.*;
import java.util.List;

@ModuleInfo(name = "Overlay", keybind = Keyboard.KEY_O)
public class OverlayModule extends Module {

	public static HashMap<String, PlayerInfo> cachedPlayers = new HashMap<>();
	public static List<String> cachedPlayersName = new ArrayList<>();
	public static int delay = 20;

	// position sliders, fds

	private final BooleanSetting background = new BooleanSetting("Background", false);
	private final NumberSetting<Integer> posX = new NumberSetting<>("Pos-X", 4, 0, 1920);
	private final NumberSetting<Integer> posY = new NumberSetting<>("Pos-Y", 4, 0, 1080 - 9);


	@Override
	public void onDisable() {
		delay = 20;
	}

	@Override
	public void onEnable() {
		delay = 20;
	}

	public boolean isInCheckeds(String name) {
		for (String pName : cachedPlayersName) {
			if (pName.equals(name)) return true;
		}
		return false;
	}

	public static void getPlayerStats(String name){
		if (delay > 0) return;

		if (name.startsWith("§8")) return;

		delay = 20;

		try{
			HttpURLConnection apiConnection = MushUtil.OpenConnectionAndGetConnection(name);

			if (apiConnection == null) return;

			String jsonResponse = MushUtil.ReadResponse(apiConnection);

			if (jsonResponse == null) return;

			JsonParser parser = new JsonParser();
			JsonObject jsonObject = (JsonObject) parser.parse(jsonResponse);

			if (apiConnection.getResponseCode() == 429) {
				delay = 20;
				return;
			}

			if (apiConnection.getResponseCode() != 200) return;

			if (cachedPlayersName.contains(name)) return;

			PlayerInfo playerI = MushUtil.getPlayerStats(jsonObject, name);

			cachedPlayersName.add(name);
			cachedPlayers.put(playerI.getName(), playerI);
		}catch(Exception e){
			e.printStackTrace();
			delay = 20;
		}
	}

	public static List<String> getTablist() {
		List<String> tab = new ArrayList<>();
		for (NetworkPlayerInfo networkPlayerInfo : MushUtil.getTablist()) {
			if (networkPlayerInfo == null) continue;

			if (networkPlayerInfo.getGameProfile().getName().equals(mc.thePlayer.getName())) continue;

			if (!cachedPlayers.containsKey(networkPlayerInfo.getGameProfile().getName())) continue;

			tab.add(networkPlayerInfo.getGameProfile().getName());
		}
		return tab;
	}

	public static EntityPlayer getPByName(String name) {
		List<Entity> entities = mc.theWorld.getEntities(Entity.class, entity -> true);

		for (Entity entity : entities) {
			if (entity == null) continue;

			if (entity.getName() == null) continue;

			if (entity.getName().equals(name)) {
				if (entity instanceof EntityPlayer)
					return (EntityPlayer) entity;
			}
		}

		return null;
	}

	public static List<String> getTablist2() {
		List<String> tab = new ArrayList<>();
		for (NetworkPlayerInfo networkPlayerInfo : MushUtil.getTablist()) {
			if (networkPlayerInfo == null) continue;

			tab.add(networkPlayerInfo.getGameProfile().getName());
		}
		return tab;
	}

	@EventTarget
	public void onTick(TickEvent event) {
		if (delay > 0) {
			delay--;
		}
	}

	@EventTarget
	public void onPreUpdate(PreUpdateEvent event) {

		for (String current : getTablist2()) {

			if (current == null) continue;

			if (current.equals(mc.thePlayer.getName())) continue;

			if (current.startsWith("§8")) continue;

			if (!isInCheckeds(current)) {
				new Thread() {
					public void run() {
						getPlayerStats(current);
					}
				}.start();
			}
		}
	}

	@EventTarget
	public void onWorldLoad(WorldLoadEvent event) {
		cachedPlayers.clear();
		cachedPlayersName.clear();
	}

	@EventTarget
	public void onRender2D(Render2DEvent event) {
		int HnamePos = posX.getValue() + (fr.getStringWidth("[-100]3333333333333") / 2);
		int HDistPos = HnamePos + fr.getStringWidth("[-100]3333333333333") + 15;
		int HFKDRPos = HDistPos + fr.getStringWidth("Dist.") + 15;
		int HWsPos = HFKDRPos + fr.getStringWidth("FK/D") + 15;
		int HTeamPos = HWsPos + fr.getStringWidth("WS") + 15 + (fr.getStringWidth("Amarelo") / 2);
		int width = (fr.getStringWidth("[-100]3333333333333") / 2) + fr.getStringWidth("[-100]3333333333333") + 15 + fr.getStringWidth("Dist.") + 15 + fr.getStringWidth("FK/D") + 15 + (fr.getStringWidth("Amarelo") / 2) + fr.getStringWidth("WS") + 15 + fr.getStringWidth("Amarelo") + 15;

		DrawUtil.drawRectHeader(posX.getValue(), posY.getValue(), width, 14);
		fr.drawStringWithShadow("Players §7" + cachedPlayersName.size(), HnamePos, posY.getValue() + (14 / 2F) - 4.5F, -1);
		fr.drawStringWithShadow("Dist.", HDistPos, posY.getValue() + (14 / 2F) - 4.5F, -1);
		fr.drawStringWithShadow("FK/D", HFKDRPos, posY.getValue() + (14 / 2F) - 4.5F, -1);
		fr.drawStringWithShadow("WS", HWsPos, (posY.getValue() + 14 / 2F) - 4.5F, -1);
		fr.drawStringWithShadow("Team", HTeamPos, posY.getValue() + (14 / 2F) - 4.5F, -1); // rogerio lima deveria, e deve ser morto

		float y = posY.getValue() + 14;
		for (int i = 0; i < getTablist().size(); i++) {

			String name = getTablist().get(i);

			if (name.equals(mc.thePlayer.getName())) continue;

			if (!cachedPlayers.containsKey(name)) continue;

			PlayerInfo player = cachedPlayers.get(name);

			DrawUtil.drawRectBackground(posX.getValue(), y, width, 1.0F);

			if (!player.isNicked()) {
				if (!player.isStaff())
					DrawUtil.drawRectBackground(posX.getValue(), y + 1.0F, width, 10F);
				else
					DrawUtil.drawRectStaff(posX.getValue(), y + 1.0F, width, 10F);
			} else {
				DrawUtil.drawRectNicked(posX.getValue(), y + 1.0F, width, 10F);
			}

			DrawUtil.drawRectBackground(posX.getValue(), y + 1.0F + 10F, width, 1.0F);

			if (i == getTablist().size() - 1) {
				DrawUtil.drawRectBackground(posX.getValue(), y + 2.0F + 10F, width, 1.0F);
			}

			String level = player.getLevel();

			String fkdr = String.valueOf(player.getFkdr());

			String dist = "";

			EntityPlayer playerMan = getPByName(name);

			String team = "";

			if (playerMan != null) {
				dist = (int) mc.thePlayer.getDistance(playerMan.posX, playerMan.posY, playerMan.posZ) + "m";

				if (playerMan.getDisplayName().getUnformattedText().startsWith("§d")) {
					team = "§d" + "Rosa";
				} else if (playerMan.getDisplayName().getUnformattedText().startsWith("§f")) {
					team = "§f" + "Branco";
				} else if (playerMan.getDisplayName().getUnformattedText().startsWith("§a")) {
					team = "§a" + "Verde";
				} else if (playerMan.getDisplayName().getUnformattedText().startsWith("§b")) {
					team = "§b" + "Aqua";
				} else if (playerMan.getDisplayName().getUnformattedText().startsWith("§6")) {
					team = "§6" + "Laranja";
				} else if (playerMan.getDisplayName().getUnformattedText().startsWith("§c")) {
					team = "§c" + "Vermelho";
				} else if (playerMan.getDisplayName().getUnformattedText().startsWith("§9")) {
					team = "§9" + "Azul";
				} else if (playerMan.getDisplayName().getUnformattedText().startsWith("§8")) {
					team = "§8" + "Cinza";
				} else if (playerMan.getDisplayName().getUnformattedText().startsWith("§e")) {
					team = "§e" + "Amarelo";
				}
			}

			String ws = String.valueOf(player.getWinstreak());

			float textY = y + (2.0F) + (10F / 2F) - (4.5F);
			float textX = posX.getValue() + 12;

			fr.drawStringWithShadow(level + " " + name, textX, textY, -1);
			fr.drawStringWithShadow("§7" + dist, HDistPos + (fr.getStringWidth("Dist.") / 2F) - (fr.getStringWidth(dist) / 2F), textY, -1);
			fr.drawStringWithShadow("§7" + fkdr, HFKDRPos + (fr.getStringWidth("FK/D") / 2F) - (fr.getStringWidth(fkdr) / 2F), textY, -1);
			fr.drawStringWithShadow("§7" + ws, HWsPos + (fr.getStringWidth("WS") / 2F) - (fr.getStringWidth(ws) / 2F), textY, -1);
			fr.drawStringWithShadow("§7" + team, HTeamPos + (fr.getStringWidth("Team") / 2F) - (fr.getStringWidth(team) / 2F), textY, -1);

			y += 12;
		}
	}
}
