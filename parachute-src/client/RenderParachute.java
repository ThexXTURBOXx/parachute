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


package parachute.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;

import parachute.common.EntityParachute;
import parachute.common.Parachute;

public class RenderParachute extends Render {

	private static int curIndex = Parachute.instance.getChuteColor();
	protected static ModelBase modelParachute;
	private static ResourceLocation parachuteTexture = null;
	
	public RenderParachute() {
		shadowSize = 0.0F;
		modelParachute = new ModelParachute();
	}
	
	public void renderParachute(EntityParachute entityparachute, double x, double y, double z, float rotation, float center) {
		GL11.glPushMatrix();

		GL11.glTranslatef((float) x, (float) y, (float) z);
		GL11.glRotatef(180.0F - rotation, 0.0F, 1.0F, 0.0F);

		// rock the parachute when hit
		float time = (float) entityparachute.getTimeSinceHit() - center;
		float damage = (float) entityparachute.getDamageTaken() - center;

		if (damage < 0.0F) {
			damage = 0.0F;
		}

		if (time > 0.0F) {
			GL11.glRotatef(MathHelper.sin(time) * time * damage / 20.0F	* (float) entityparachute.getForwardDirection(), 0.0F,	0.0F, 1.0F);
		}

		func_110777_b(entityparachute); // bind the parachute canopy texture
		modelParachute.render(entityparachute, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);

		if (entityparachute.riddenByEntity != null) {
			EntityPlayer rider = (EntityPlayer) entityparachute.riddenByEntity;
			if (Parachute.instance.getCanopyType()) {
				renderSmallParachuteCords(rider, center);
			} else { 
				renderLargeParachuteCords(rider, center);
			}
		}

		GL11.glPopMatrix();
	}

    @Override
	public void doRender(Entity entity, double x, double y, double z, float rotation, float center) {
		renderParachute((EntityParachute) entity, x, y, z, rotation, center);
	}

	public void renderLargeParachuteCords(EntityPlayer rider, float center) {
        float zOffset = 3.0F;
		float x = -5.0F;
		float y = 2.5F;
		if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
			y = 1.5F;
		}
		float zl = -zOffset;
        float zr = zOffset;

		float b = rider.getBrightness(center);

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);

		GL11.glScalef(0.0625F, -1.0F, 0.0625F);

		GL11.glBegin(GL11.GL_LINES);
		// left end
		GL11.glColor3f(b * 0.5F, b * 0.5F, b * 0.65F); // slightly blue

		GL11.glVertex3f(-8F, 0.37F, -31.5F); 	// top - front
		GL11.glVertex3f(x, y, zl); 				// bottom

		GL11.glVertex3f(8F, 0.37F, -31.5F);     // ...back
		GL11.glVertex3f(x, y, zl);
		
		// left middle
		GL11.glVertex3f(-8F, 0.12F, -16F);
		GL11.glVertex3f(x, y, zl);

		GL11.glVertex3f(8F, 0.12F, -16F);
		GL11.glVertex3f(x, y, zl);

		// right end
		GL11.glColor3f(b * 0.65F, b * 0.5F, b * 0.5F); // slightly red

		GL11.glVertex3f(-8F, 0.37F, 31.5F);
		GL11.glVertex3f(x, y, zr);

		GL11.glVertex3f(8F, 0.37F, 31.5F);
		GL11.glVertex3f(x, y, zr);

		// right middle
		GL11.glVertex3f(-8F, 0.12F, 16F);
		GL11.glVertex3f(x, y, zr);

		GL11.glVertex3f(8F, 0.12F, 16F);
		GL11.glVertex3f(x, y, zr);
		
		// center
		GL11.glColor3f(b * 0.5F, b * 0.65F, b * 0.5F); // slightly green
		
		GL11.glVertex3f(-8F, 0F, 0F);
		GL11.glVertex3f(x, y, zl); // first center cord goes to the left 

		GL11.glVertex3f(8F, 0F, 0F);
		GL11.glVertex3f(x, y, zl);
        
        GL11.glVertex3f(-8F, 0F, 0F); // second center cord goes to the right
		GL11.glVertex3f(x, y, zr);

		GL11.glVertex3f(8F, 0F, 0F);
		GL11.glVertex3f(x, y, zr);

		GL11.glEnd();
		
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	public void renderSmallParachuteCords(EntityPlayer rider, float center) {
        float zOffset = 3.0F;
		float x = -5.0F;
		float y = 1.5F;
		if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
			y = 1.25F;
		}
		float zl = -zOffset;
        float zr = zOffset;

		float b = rider.getBrightness(center);

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);

		GL11.glScalef(0.0625F, -1.0F, 0.0625F);

		GL11.glBegin(GL11.GL_LINES);
		// left side
		GL11.glColor3f(b * 0.5F, b * 0.5F, b * 0.65F); // slightly blue

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
		GL11.glColor3f(b * 0.65F, b * 0.5F, b * 0.5F); // slightly red

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
	}
	
	public static void setParachuteColor(int index) {
		parachuteTexture = getParachuteColor(index);
	}
	
	protected static ResourceLocation getParachuteColor(int idx) {
		if (parachuteTexture == null || idx != curIndex) {
			parachuteTexture = new ResourceLocation("parachutemod", "textures/blocks/parachute_canopy" + idx + ".png");
			curIndex = idx;
		}
		return parachuteTexture;
	}

	@Override
	protected ResourceLocation func_110775_a(Entity entity) {
		parachuteTexture = getParachuteColor(curIndex);
		return parachuteTexture;
	}
	
}
