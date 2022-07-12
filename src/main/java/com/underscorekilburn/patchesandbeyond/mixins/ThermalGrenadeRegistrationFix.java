package com.underscorekilburn.patchesandbeyond.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Constant;

import cofh.thermal.core.util.RegistrationHelper;

@Pseudo
@Mixin(RegistrationHelper.class)
public class ThermalGrenadeRegistrationFix
{
	/**
	 * Fixes Thermal grenades being registered with an incorrect name (i.e. "explosive_grenade_grenade" instead of "explosive_grenade")
	 */
	@ModifyConstant(method="registerGrenade", constant=@Constant(stringValue="_grenade"), remap=false)
	private static String removeGrenadeSuffix(String suffix)
	{
		return "";
	}
}
