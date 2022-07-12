package com.underscorekilburn.patchesandbeyond.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.entity.LivingEntity;
import zmaster587.advancedRocketry.dimension.DimensionManager;

@Mixin(LivingEntity.class)
public class AdvancedRocketryGravityFix
{
	/**
	 * Fixes Advanced Rocketry not applying gravity modifiers (players and mobs).
	 */
	@ModifyVariable(method="travel", at=@At(value="INVOKE_ASSIGN", target="Lnet/minecraft/entity/ai/attributes/ModifiableAttributeInstance;getValue()D", ordinal=0))
	public double modifyGravity(double a)
	{
		LivingEntity self = (LivingEntity)(Object)this;
		double mult = (double)DimensionManager.getInstance().getDimensionProperties(self.level.dimension().location()).gravitationalMultiplier;
		return a * mult;
	}
}
