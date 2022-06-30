package com.underscorekilburn.patchesandbeyond.mixins;

import com.simibubi.create.content.contraptions.processing.InWorldProcessing;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;

@Pseudo
@Mixin(InWorldProcessing.class)
public class FanProcessingFix
{
	/**
	 * Fixes bulk blasting setups destroying items if a stack of items yields more than one output.
	 * In particular, this fixes unprocessed invar ingots getting destroyed when attempting to process more than one nickel compound ingot in world at a time.
	 */
	private static ItemEntity copyCreateData(ItemEntity src, ItemEntity dst)
	{
		if(src.getPersistentData().contains("CreateData"))
		{
			CompoundNBT nbt = dst.getPersistentData();
			if(!nbt.contains("CreateData"))
				nbt.put("CreateData", new CompoundNBT());
			nbt.getCompound("CreateData").merge(src.getPersistentData().getCompound("CreateData"));
		}
		return dst;
	}
	
	@ModifyVariable(method="applyProcessing(Lnet/minecraft/entity/item/ItemEntity;Lcom/simibubi/create/content/contraptions/processing/InWorldProcessing$Type;)V",
			at=@At(value="INVOKE", target="Lnet/minecraft/world/World;addFreshEntity(Lnet/minecraft/entity/Entity;)Z"), name="entityIn")
	private static ItemEntity applyProcessing(ItemEntity entityIn, ItemEntity entity, InWorldProcessing.Type type)
	{
		return copyCreateData(entity, entityIn);
	}
	
	@ModifyVariable(method="applyRecipeOn(Lnet/minecraft/entity/item/ItemEntity;Lnet/minecraft/item/crafting/IRecipe;)V",
			at=@At(value="INVOKE", target="Lnet/minecraft/world/World;addFreshEntity(Lnet/minecraft/entity/Entity;)Z"), name="entityIn")
	private static ItemEntity applyRecipeOn(ItemEntity entityIn, ItemEntity entity, IRecipe<?> recipe)
	{
		return copyCreateData(entity, entityIn);
	}
}
