package com.underscorekilburn.patchesandbeyond.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.logistics.block.depot.EjectorRenderer;
import com.simibubi.create.content.logistics.block.depot.EjectorTileEntity;
import com.underscorekilburn.patchesandbeyond.PaBConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.math.vector.Vector3d;

@Mixin(EjectorRenderer.class)
public class ItemCullingEjector
{
	EjectorTileEntity currentEjector = null;
	
	@Inject(method="renderSafe", at=@At(value="HEAD"), remap=false)
	public void onRenderSafe(KineticTileEntity te, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffer,
			int light, int overlay, CallbackInfo ci)
	{
		currentEjector = (EjectorTileEntity)te;
	}
	
	@ModifyVariable(method="renderSafe", at=@At(value="STORE"), ordinal=4, remap=false)
	public float updateEjectedItemTime(float time)
	{
	    int dist = PaBConfig.getInstance().itemCullingEjector.get();
	    if(dist <= 0) return time;
	    
		Vector3d pos = currentEjector.getLaunchedItemLocation(time);
		
		Minecraft client = Minecraft.getInstance();
	    Vector3d cameraMC = client.gameRenderer.getMainCamera().getPosition();
	    
	    double dX = cameraMC.x - (double)pos.x;
	    double dY = cameraMC.y - (double)pos.y;
	    double dZ = cameraMC.z - (double)pos.z;

	    if(dX*dX+dY*dY+dZ*dZ > (double)(dist*dist))
	    	return 1e6f;
	    
	    return time;
	}
}
