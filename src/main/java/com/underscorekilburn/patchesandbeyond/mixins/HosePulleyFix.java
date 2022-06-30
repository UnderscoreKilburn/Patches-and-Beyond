package com.underscorekilburn.patchesandbeyond.mixins;

import com.simibubi.create.content.contraptions.fluids.actors.HosePulleyFluidHandler;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

@Pseudo
@Mixin(HosePulleyFluidHandler.class)
public class HosePulleyFix
{
	/**
	 * Fixes Hose Pulleys not consuming input fluid when receiving more than 1000 mB per tick.
	 * (this is particularly easy to achieve using Fluid Cells from Thermal)
	 */
	@Inject(method="fill", at={
		@At(value="RETURN", ordinal=2),
		@At(value="RETURN", ordinal=4)
	}, remap=false, cancellable=true, locals=LocalCapture.CAPTURE_FAILHARD)
	public void onFillReturn(FluidStack resource, FluidAction action, CallbackInfoReturnable<Integer> cir, int diff, int totalAmountAfterFill, FluidStack remaining)
	{
		if(remaining.getAmount() < resource.getAmount() && resource.getAmount() > 1000)
			cir.setReturnValue(cir.getReturnValue() + 1000);
	}
}
