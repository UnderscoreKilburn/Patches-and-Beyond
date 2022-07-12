package com.underscorekilburn.patchesandbeyond.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import zmaster587.advancedRocketry.event.RocketEventHandler;

@Pseudo
@Mixin(RocketEventHandler.class)
public class DisableSpacesuitHUD
{
	@Inject(method="renderModuleSlots", at=@At("HEAD"), remap=false, cancellable=true)
	private void renderModuleSlots(ItemStack armorStack, int slot, RenderGameOverlayEvent event, CallbackInfo ci)
	{
		ci.cancel();
	}
}
