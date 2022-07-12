package com.underscorekilburn.patchesandbeyond.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Constant;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import zmaster587.advancedRocketry.dimension.DimensionManager;

@Mixin({TNTEntity.class, ItemEntity.class, AbstractMinecartEntity.class, FallingBlockEntity.class})
public class AdvancedRocketryGravityMiscFix
{
	/**
	 * Fixes Advanced Rocketry not applying gravity modifiers (minecarts, TNT, items).
	 */
	@ModifyConstant(method="tick", constant=@Constant(doubleValue=-0.04))
	public double modifyGravity(double a)
	{
		Entity self = (Entity)(Object)this;
		double mult = (double)DimensionManager.getInstance().getDimensionProperties(self.level.dimension().location()).gravitationalMultiplier;
		return a * mult;
	}
}
