package com.underscorekilburn.patchesandbeyond.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Constant;

import appeng.core.api.definitions.ApiItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

@Pseudo
@Mixin(ApiItems.class)
public class CrystalSeedSizeFix
{
	/**
	 * Fixes crystal seeds from AE2 spawning inside of chutes and funnels when dropped down from one.
	 * The increased height was originally a fix for an awkward looking interaction with annihiliation planes
	 * but reverting it shouldn't break anything (hopefully).
	 */
	@ModifyConstant(method="*(Lnet/minecraft/entity/EntityType$Builder;)V", constant=@Constant(floatValue=0.4f), remap=false)
	private static float modifyCrystalHeight(float h, EntityType.Builder<Entity> builder)
	{
		return 0.25f;
	}
}
