package com.underscorekilburn.patchesandbeyond.mixins;

import java.util.stream.Collectors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import mezz.jei.api.registration.IRecipeRegistration;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.common.integration.jei.ChiselJEIPlugin;
import team.chisel.common.integration.jei.ChiselRecipeCategory;

@Mixin(ChiselJEIPlugin.class)
public class ChiselJEIIntegrationFix
{
	@Shadow(remap=false)
    private ChiselRecipeCategory category;
    
	@Inject(method="registerRecipes", at=@At("HEAD"), remap=false)
	public void onRegisterRecipes(IRecipeRegistration registry, CallbackInfo ci)
	{
		registry.addRecipes(CarvingUtils.getChiselRegistry().getGroups().stream()
				.collect(Collectors.toList()), category.getUid());
	}
}
