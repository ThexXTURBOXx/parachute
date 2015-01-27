//  
//  =====GPL=============================================================
//  This program is free software; you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation; version 2 dated June, 1991.
// 
//  This program is distributed in the hope that it will be useful, 
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
// 
//  You should have received a copy of the GNU General Public License
//  along with this program;  if not, write to the Free Software
//  Foundation, Inc., 675 Mass Ave., Cambridge, MA 02139, USA.
//  =====================================================================
//
//
// Copyright 2011-2015 Michael Sheppard (crackedEgg)
//
package com.parachute.client;

import com.parachute.common.ConfigHandler;
import com.parachute.common.EntityParachute;
import com.parachute.common.Parachute;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderParachute extends Render {

	private static String curColor = ConfigHandler.getChuteColor();
	protected static ModelBase modelParachute = new ModelParachute();
	private static ResourceLocation parachuteTexture = null;
	private static final Random rand = new Random(System.currentTimeMillis());

	public RenderParachute(RenderManager rm)
	{
		super(rm);
		shadowSize = 0.0F;
	}

	public void renderParachute(EntityParachute entityparachute, double x, double y, double z, float rotationYaw, float unused)
	{
		GlStateManager.pushMatrix();

		GlStateManager.translate((float) x, (float) y, (float) z);
		GlStateManager.rotate(180.0F - rotationYaw, 0.0F, 1.0F, 0.0F);

		if (!bindEntityTexture(entityparachute)) {
			return;
		}
		modelParachute.render(entityparachute, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);

		if (entityparachute.riddenByEntity != null) {
			EntityPlayer rider = (EntityPlayer) entityparachute.riddenByEntity;
			renderParachuteCords(rider, unused);
		}

		GlStateManager.popMatrix();
		super.doRender(entityparachute, x, y, z, rotationYaw, unused);
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float rotation, float unused)
	{
		renderParachute((EntityParachute) entity, x, y, z, rotation, unused);
	}

	public void renderParachuteCords(EntityPlayer rider, float unused)
	{
		float zOffset = 3.0F;
		float x = 0.0F;
		float y = (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) ? 1.25f : 1.5F;
		float zl = -zOffset;
		float zr = zOffset;
		float b = rider.getBrightness(unused);

		GlStateManager.pushMatrix();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);

		GlStateManager.scale(0.0625F, -1.0F, 0.0625F);

		GL11.glBegin(GL11.GL_LINES);
		// left side
		GlStateManager.color(b * 0.5F, b * 0.5F, b * 0.65F); // slightly blue

		GL11.glVertex3f(-8F, 0.25F, -23.5F);
		GL11.glVertex3f(x, y, zl);

		GL11.glVertex3f(8F, 0.25F, -23.5F);
		GL11.glVertex3f(x, y, zl);

		// front
		GL11.glVertex3f(-8F, 0F, -8F);
		GL11.glVertex3f(x, y, zl);

		GL11.glVertex3f(8F, 0F, -8F);
		GL11.glVertex3f(x, y, zl);

		// right side
		GlStateManager.color(b * 0.65F, b * 0.5F, b * 0.5F); // slightly red

		GL11.glVertex3f(-8F, 0.25F, 23.5F);
		GL11.glVertex3f(x, y, zr);

		GL11.glVertex3f(8F, 0.25F, 23.5F);
		GL11.glVertex3f(x, y, zr);

		// back
		GL11.glVertex3f(-8F, 0F, 8F);
		GL11.glVertex3f(x, y, zr);

		GL11.glVertex3f(8F, 0F, 8F);
		GL11.glVertex3f(x, y, zr);
		GL11.glEnd();

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GlStateManager.popMatrix();
	}

	public static void setParachuteColor(String color)
	{
		if (color.equalsIgnoreCase("random")) {
			if (rand.nextBoolean()) {
				parachuteTexture = new ResourceLocation("textures/blocks/wool_colored_" + getRandomColor() + ".png");
			} else {
				parachuteTexture = new ResourceLocation(Parachute.modid + ":textures/blocks/" + getRandomCustomColor() + ".png");
			}
		} else if (color.toLowerCase().startsWith("custom")) {
			parachuteTexture = new ResourceLocation(Parachute.modid + ":textures/blocks/" + color + ".png");
		} else {
			parachuteTexture = new ResourceLocation("textures/blocks/wool_colored_" + color + ".png");
		}
		curColor = color;
	}

	protected static ResourceLocation getParachuteColor(String color)
	{
		if (parachuteTexture == null) {
			if (color.equalsIgnoreCase("random")) {
				if (rand.nextBoolean()) {
					parachuteTexture = new ResourceLocation("textures/blocks/wool_colored_" + getRandomColor() + ".png");
				} else {
					parachuteTexture = new ResourceLocation(Parachute.modid + ":textures/blocks/" + getRandomCustomColor() + ".png");
				}
			} else if (color.toLowerCase().startsWith("custom")) {
				parachuteTexture = new ResourceLocation(Parachute.modid + ":textures/blocks/" + color + ".png");
			} else {
				parachuteTexture = new ResourceLocation("textures/blocks/wool_colored_" + color + ".png");
			}
			curColor = color;
		}
		return parachuteTexture;
	}

	protected static String getRandomColor()
	{
		String[] colors = {
			"black",
			"blue",
			"brown",
			"cyan",
			"gray",
			"green",
			"light_blue",
			"lime",
			"magenta",
			"orange",
			"pink",
			"purple",
			"red",
			"silver",
			"white",
			"yellow"
		};

		return colors[rand.nextInt(16)];
	}

	// return the string 'custom' and append a random digit 
	// between zero and nine
	protected static String getRandomCustomColor()
	{
		return "custom" + rand.nextInt(10);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity)
	{
		parachuteTexture = getParachuteColor(curColor);
		return parachuteTexture;
	}

}
