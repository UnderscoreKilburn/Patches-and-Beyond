package com.underscorekilburn.patchesandbeyond.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.simibubi.create.content.logistics.block.depot.DepotBehaviour;
import com.simibubi.create.content.logistics.block.depot.DepotRenderer;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.underscorekilburn.patchesandbeyond.PaBConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

@Mixin(DepotRenderer.class)
public class ItemCullingDepot
{
	@Inject(method="renderItemsOf", at=@At(value="HEAD"), remap=false, cancellable=true)
	private static void renderItemsOf(SmartTileEntity te, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffer,
			int light, int overlay, DepotBehaviour depotBehaviour, CallbackInfo ci)
	{
	    int dist = PaBConfig.getInstance().itemCullingDepot.get();
	    if(dist <= 0) return;
	    
		BlockPos pos = te.getBlockPos();

		Minecraft client = Minecraft.getInstance();
	    Vector3d cameraMC = client.gameRenderer.getMainCamera().getPosition();
	    
	    double dX = cameraMC.x - (double)pos.getX() - 0.5;
	    double dY = cameraMC.y - (double)pos.getY() - 0.5;
	    double dZ = cameraMC.z - (double)pos.getZ() - 0.5;
	    
	    if(dX*dX+dY*dY+dZ*dZ > (double)(dist*dist))
	    	ci.cancel();
	}
}
