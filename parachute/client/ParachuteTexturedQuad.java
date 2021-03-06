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

import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class ParachuteTexturedQuad {

	final private float texSize = 16F;
	public PositionTextureVertex vertexPositions[];
	public int nVertices;

	public ParachuteTexturedQuad(PositionTextureVertex vertices[])
	{
		vertexPositions = vertices;
		nVertices = vertices.length;
	}

	public ParachuteTexturedQuad(PositionTextureVertex vertices[], int texU1, int texV1, int texU2, int texV2)
	{
		this(vertices);
		vertices[0] = vertices[0].setTexturePosition((float) texU2 / texSize, (float) texV1 / texSize);
		vertices[1] = vertices[1].setTexturePosition((float) texU1 / texSize, (float) texV1 / texSize);
		vertices[2] = vertices[2].setTexturePosition((float) texU1 / texSize, (float) texV2 / texSize);
		vertices[3] = vertices[3].setTexturePosition((float) texU2 / texSize, (float) texV2 / texSize);
	}

	public void flipFace()
	{
		PositionTextureVertex texCoords[] = new PositionTextureVertex[vertexPositions.length];

		for (int i = 0; i < vertexPositions.length; i++) {
			texCoords[i] = vertexPositions[vertexPositions.length - i - 1];
		}

		vertexPositions = texCoords;
	}

	public void draw(WorldRenderer worldrenderer, float scale)
	{
		Vec3 vec3 = vertexPositions[1].vector3D.subtractReverse(vertexPositions[0].vector3D);
		Vec3 vec31 = vertexPositions[1].vector3D.subtractReverse(vertexPositions[2].vector3D);
		Vec3 vec32 = vec31.crossProduct(vec3).normalize();

		float x = (float)vec32.xCoord;
        float y = (float)vec32.yCoord;
        float z = (float)vec32.zCoord;

        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL);
		for (int i = 0; i < 4; ++i) {
			PositionTextureVertex positiontexturevertex = this.vertexPositions[i];
			worldrenderer.pos(positiontexturevertex.vector3D.xCoord * (double)scale,
				positiontexturevertex.vector3D.yCoord * (double)scale,
				positiontexturevertex.vector3D.zCoord * (double)scale).tex((double)positiontexturevertex.texturePositionX,
				(double)positiontexturevertex.texturePositionY).normal(x, y, z).endVertex();
		}

		Tessellator.getInstance().draw();
	}

}
