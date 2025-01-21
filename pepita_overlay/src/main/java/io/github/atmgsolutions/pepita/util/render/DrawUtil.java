package io.github.atmgsolutions.pepita.util.render;

import io.github.atmgsolutions.pepita.util.Util;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.lwjgl.opengl.GL11.GL_BLEND;

public class DrawUtil extends Util {
	public static void drawRectStaff(float x, float y, float width, float height) {
		drawRect(x, y, x + width, y + height, new Color(70, 0, 35, 166).getRGB());
	}

	public static void drawRectNicked(float x, float y, float width, float height) {
		drawRect(x, y, x + width, y + height, new Color(90, 0, 0, 140).getRGB());
	}

	public static void drawRectBackground(float x, float y, float width, float height) {
		drawRect(x, y, x + width, y + height, new Color(0, 0, 0, 140).getRGB());
	}

	public static void drawRectHeader(float x, float y, float width, float height) {
		drawRect(x, y, x + width, y + height, new Color(0, 0, 0, 165).getRGB());
	}

	public static void drawRect(float left, float top, float right, float bottom, int color)
	{
		if (left < right)
		{
			float i = left;
			left = right;
			right = i;
		}

		if (top < bottom)
		{
			float j = top;
			top = bottom;
			bottom = j;
		}

		float f3 = (float)(color >> 24 & 255) / 255.0F;
		float f = (float)(color >> 16 & 255) / 255.0F;
		float f1 = (float)(color >> 8 & 255) / 255.0F;
		float f2 = (float)(color & 255) / 255.0F;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(f, f1, f2, f3);
		worldrenderer.begin(7, DefaultVertexFormats.POSITION);
		worldrenderer.pos((double)left, (double)bottom, 0.0D).endVertex();
		worldrenderer.pos((double)right, (double)bottom, 0.0D).endVertex();
		worldrenderer.pos((double)right, (double)top, 0.0D).endVertex();
		worldrenderer.pos((double)left, (double)top, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}

	public static void drawImage(ResourceLocation res, float x, float y, float width, float height, int color) {
		GL11.glDisable(2929);
		GL11.glEnable(3042);
		GL11.glDepthMask(false);
		GL14.glBlendFuncSeparate(770, 771, 1, 0);
		int red = color >> 16 & 0xFF;
		int green = color >> 8 & 0xFF;
		int blue = color & 0xFF;
		int alpha = color >> 24 & 0xFF;
		GL11.glColor4f((float) red / 255.0f, (float) green / 255.0f, (float) blue / 255.0f, (float) alpha / 255.0f);
		mc.getTextureManager().bindTexture(res);
		drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
		GL11.glDepthMask(true);
		GL11.glDisable(GL_BLEND);
		GL11.glEnable(2929);
	}

	public static void drawModalRectWithCustomSizedTexture(float x, float y, float u, float v, float width, float height, float textureWidth, float textureHeight) {
		float f = 1.0f / textureWidth;
		float f1 = 1.0f / textureHeight;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		worldrenderer.pos(x, y + height, 0.0).tex(u * f, (v + height) * f1).endVertex();
		worldrenderer.pos(x + width, y + height, 0.0).tex((u + width) * f, (v + height) * f1).endVertex();
		worldrenderer.pos(x + width, y, 0.0).tex((u + width) * f, v * f1).endVertex();
		worldrenderer.pos(x, y, 0.0).tex(u * f, v * f1).endVertex();
		tessellator.draw();
	}

	public static void drawImage2(BufferedImage res, float x, float y, float width, float height, int color) {
		GL11.glDisable(2929);
		GL11.glEnable(3042);
		GL11.glDepthMask(false);
		GL14.glBlendFuncSeparate(770, 771, 1, 0);
		int red = color >> 16 & 0xFF;
		int green = color >> 8 & 0xFF;
		int blue = color & 0xFF;
		int alpha = color >> 24 & 0xFF;
		GL11.glColor4f((float) red / 255.0f, (float) green / 255.0f, (float) blue / 255.0f, (float) alpha / 255.0f);
		DynamicTexture texture = new DynamicTexture(res);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getGlTextureId());
		drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
		GL11.glDepthMask(true);
		GL11.glDisable(GL_BLEND);
		GL11.glEnable(2929);
	}

}
