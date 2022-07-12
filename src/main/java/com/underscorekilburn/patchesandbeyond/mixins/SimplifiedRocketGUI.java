package com.underscorekilburn.patchesandbeyond.mixins;

import java.util.LinkedList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import zmaster587.advancedRocketry.entity.EntityRocket;
import zmaster587.advancedRocketry.inventory.TextureResources;
import zmaster587.advancedRocketry.tile.TileGuidanceComputer;
import zmaster587.libVulpes.LibVulpes;
import zmaster587.libVulpes.inventory.GuiHandler;
import zmaster587.libVulpes.inventory.modules.ModuleBase;
import zmaster587.libVulpes.inventory.modules.ModuleButton;

import zmaster587.libVulpes.inventory.modules.ModuleTexturedSlotArray;

@Pseudo
@Mixin(EntityRocket.class)
public class SimplifiedRocketGUI
{
	@Inject(method="getModules", at=@At("HEAD"), cancellable=true, remap=false)
	public void getModules(int ID, PlayerEntity player, CallbackInfoReturnable<List<ModuleBase>> cir)
	{
		if(ID == GuiHandler.guiId.MODULAR.ordinal())
		{
			List<ModuleBase> modules = new LinkedList<ModuleBase>();
			EntityRocket self = (EntityRocket)(Object)this;
			
			// Only expose the guidance computer if it is present
			List<TileEntity> tiles = self.storage.getGUItiles();
			for(int i = 0; i < tiles.size(); i++)
			{
				if(tiles.get(i) instanceof TileGuidanceComputer)
				{
					modules.add(new ModuleTexturedSlotArray(80, 40, (IInventory)tiles.get(i), 0, 1, TextureResources.idChip));
					break;
				}
			}
			
			// Disassemble
			ModuleButton disassembleButton = new ModuleButton(53, 60, LibVulpes.proxy.getLocalizedString("msg.entity.rocket.disass"), self, zmaster587.libVulpes.inventory.TextureResources.buttonBuild, 70, 20);
			disassembleButton.setAdditionalData(0);
			disassembleButton.setColor(0xFFFF2222);
			modules.add(disassembleButton);
			
			cir.setReturnValue(modules);
		}
	}
}
