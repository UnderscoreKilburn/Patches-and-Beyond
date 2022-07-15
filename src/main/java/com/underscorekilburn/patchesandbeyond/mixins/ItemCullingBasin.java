package com.underscorekilburn.patchesandbeyond.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.simibubi.create.content.contraptions.processing.BasinRenderer;
import com.simibubi.create.content.contraptions.processing.BasinTileEntity;
import com.underscorekilburn.patchesandbeyond.PaBConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

@Mixin(BasinRenderer.class)
public class ItemCullingBasin
{
	@Inject(method="renderSafe", at=@At(value="INVOKE", target="Lcom/simibubi/create/content/contraptions/processing/BasinRenderer;renderFluids(Lcom/simibubi/create/content/contraptions/processing/BasinTileEntity;FLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;II)F", shift=At.Shift.AFTER, remap=false), cancellable=true, remap=false)
	void onRenderFluids(BasinTileEntity basin, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffer, int light, int overlay, CallbackInfo ci)
	{
	    int dist = PaBConfig.getInstance().itemCullingBasin.get();
	    if(dist <= 0) return;
	    
		BlockPos pos = basin.getBlockPos();

		Minecraft client = Minecraft.getInstance();
	    Vector3d cameraMC = client.gameRenderer.getMainCamera().getPosition();
	    
	    double dX = cameraMC.x - (double)pos.getX() - 0.5;
	    double dY = cameraMC.y - (double)pos.getY() - 0.5;
	    double dZ = cameraMC.z - (double)pos.getZ() - 0.5;
	    
	    if(dX*dX+dY*dY+dZ*dZ > (double)(dist*dist))
	    	ci.cancel();
	}
}
