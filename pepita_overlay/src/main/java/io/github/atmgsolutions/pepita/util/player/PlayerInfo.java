package io.github.atmgsolutions.pepita.util.player;

import java.awt.image.BufferedImage;

// se tu se perguntar praq essa classe, nem eu sei - rogeriolima24415717104, https://media.discordapp.net/attachments/1167967396502437908/1329557457034285186/watermark.gif?ex=678ebadf&is=678d695f&hm=ee628095685bd3bc97ed38c20507e71dcc76d7394fd797779a1bacc47e51b7fa&=&width=304&height=676
public class PlayerInfo {
	private final boolean staff;
	private final boolean nicked;
	private final int winstreak;
	private final float fkdr;
	private final BufferedImage head;
	private final String name;
	private final String level;

	public PlayerInfo(int winstreak, float fkdr, String level, String name, BufferedImage head, boolean staff, boolean nicked) {
		this.winstreak = winstreak;
		this.fkdr = fkdr;
		this.level = level;
		this.name = name;
		this.head = head;
		this.staff = staff;
		this.nicked = nicked;
	}

	public String getName() {
		return name;
	}

	public boolean isNicked() {
		return nicked;
	}

	public boolean isStaff() {
		return staff;
	}

	public int getWinstreak() {
		return winstreak;
	}

	public float getFkdr() {
		return fkdr;
	}

	public BufferedImage getHead() {
		return head;
	}

	public String getLevel() {
		return level;
	}
}