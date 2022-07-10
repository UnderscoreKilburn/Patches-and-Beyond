package com.underscorekilburn.patchesandbeyond.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mod.chiselsandbits.block.entities.BitStorageBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;

@Pseudo
@Mixin(BitStorageBlockEntity.class)
abstract public class BitStorageDuplicationFix
{
	/**
	 * Fixes bit storage tanks not consuming input blocks, allowing for infinite duplication of certain resources.
	 */
	@Shadow(remap=false)
	protected abstract void saveAndUpdate();
	
	@Inject(method="insertItem(ILnet/minecraft/item/ItemStack;Z)Lnet/minecraft/item/ItemStack;", at=@At(value="CONSTANT", args="intValue=4096", shift=At.Shift.BY, by=2),
			cancellable=true, remap=false)
	public void insertItem(int slot, ItemStack stack, boolean simulate, CallbackInfoReturnable<ItemStack> cir)
	{
		ItemStack newStack = stack.copy();
		newStack.shrink(1);
		saveAndUpdate();
		cir.setReturnValue(newStack);
	}

	/**
	 * Fixes being able to insert mismatched bits into a bit storage tank, converting the entire contents of the tank into the newly inserted block.
	 */
	@Inject(method="attemptSolidBitStackInsertion(Lnet/minecraft/item/ItemStack;ZLnet/minecraft/block/BlockState;)Lnet/minecraft/item/ItemStack;",
			at=@At(value="INVOKE", target="getBlockBitStack(Lnet/minecraft/block/BlockState;I)Lnet/minecraft/item/ItemStack;", shift=At.Shift.AFTER),
			cancellable=true, remap=false)
	public void attemptSolidBitStackInsertion(ItemStack stack, boolean simulate, BlockState blk, CallbackInfoReturnable<ItemStack> cir)
	{
		BitStorageBlockEntity self = (BitStorageBlockEntity)(Object)this;
		if(self.getBits() > 0 && !self.getState().getBlock().equals(blk.getBlock()))
			cir.setReturnValue(stack);
	}

	@Inject(method="attemptFluidBitStackInsertion(Lnet/minecraft/item/ItemStack;ZLnet/minecraft/block/BlockState;)Lnet/minecraft/item/ItemStack;",
			at=@At(value="INVOKE", target="getFluidBitStack(Lnet/minecraft/fluid/Fluid;I)Lnet/minecraft/item/ItemStack;", shift=At.Shift.AFTER),
			cancellable=true, remap=false)
	public void attemptFluidBitStackInsertion(ItemStack stack, boolean simulate, BlockState blk, CallbackInfoReturnable<ItemStack> cir)
	{
		BitStorageBlockEntity self = (BitStorageBlockEntity)(Object)this;
		if(self.getBits() > 0 && (self.getMyFluid() == null || !self.getMyFluid().defaultFluidState().createLegacyBlock().getBlock().equals(blk.getBlock())))
			cir.setReturnValue(stack);
	}
}
