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

package parachute.common;

import parachute.client.RenderParachute;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemParachute extends Item {
	
	public ItemParachute(int i, EnumToolMaterial enumtoolmaterial) {
		super(i);
		maxStackSize = 16;
		setMaxDamage(enumtoolmaterial.getMaxUses()); // this damage is for number of uses only
		setCreativeTab(CreativeTabs.tabTransport); // place in the transportation tab in creative mode
	}

	public ItemParachute(int i) {
		super(i);
		maxStackSize = 16;
		setMaxDamage(EnumToolMaterial.WOOD.getMaxUses()); // this damage is for number of uses only
		setCreativeTab(CreativeTabs.tabTransport); // place in the transportation tab in creative mode
	}

	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		return deployParachute(itemstack, world, entityplayer);
	}

	public ItemStack deployParachute(ItemStack itemstack, World world,	EntityPlayer entityplayer) {
		// don't deploy if entityplayer is null or if player is not falling or if already on a parachute.
		if (entityplayer == null || !isFalling(entityplayer) || entityplayer.ridingEntity != null) {
			return itemstack;
		}

		world.playSoundAtEntity(entityplayer, "step.cloth", 1.0F, 0.5F/* / (itemRand.nextFloat() * 0.4F + 0.8F)*/);

		double x = entityplayer.prevPosX + (entityplayer.posX - entityplayer.prevPosX);
		double y = (entityplayer.prevPosY + (entityplayer.posY - entityplayer.prevPosY) + 1.62D) - (double) entityplayer.yOffset;
		double z = entityplayer.prevPosZ + (entityplayer.posZ - entityplayer.prevPosZ);

		EntityParachute chute = new EntityParachute(world, (float) x, (float) y - 2.5F, (float) z);
		chute.rotationYaw = (float)(((MathHelper.floor_double((double)(entityplayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) - 1) * 90);
		if (!world.isRemote) {
			world.spawnEntityInWorld(chute);
		}
		entityplayer.mountEntity(chute);

		if (!entityplayer.capabilities.isCreativeMode) {
			itemstack.damageItem(2, entityplayer);
		}
		
		return itemstack;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister iconReg) {
		itemIcon = iconReg.registerIcon("parachutemod:Parachute");
	}
	
	public boolean isFalling(EntityPlayer entity) {
		return (entity.fallDistance > 0.0F && !entity.onGround && !entity.isOnLadder());
	}
	
}
