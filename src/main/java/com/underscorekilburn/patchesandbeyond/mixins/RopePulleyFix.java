package com.underscorekilburn.patchesandbeyond.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.nbt.CompoundNBT;

import com.simibubi.create.content.contraptions.components.structureMovement.pulley.PulleyTileEntity;

@Pseudo
@Mixin(PulleyTileEntity.class)
public class RopePulleyFix
{
	/**
	 * Fixes pulley contraptions sometimes phasing through solid blocks when unloaded then reloaded while in motion
	 */
	@Inject(method="write(Lnet/minecraft/nbt/CompoundNBT;Z)V", at=@At(value="HEAD"), remap=false)
	public void write(CompoundNBT compound, boolean clientPacket, CallbackInfo ci)
	{
		compound.putBoolean("NeedsContraption", ((PulleyTileEntity)(Object)this).needsContraption);
	}
}
