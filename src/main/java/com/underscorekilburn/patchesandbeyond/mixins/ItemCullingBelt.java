package com.underscorekilburn.patchesandbeyond.mixins;

import java.util.Iterator;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.simibubi.create.content.contraptions.relays.belt.BeltRenderer;
import com.simibubi.create.content.contraptions.relays.belt.BeltSlope;
import com.simibubi.create.content.contraptions.relays.belt.BeltTileEntity;
import com.simibubi.create.content.contraptions.relays.belt.transport.TransportedItemStack;
import com.underscorekilburn.patchesandbeyond.PaBConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;

@Mixin(BeltRenderer.class)
public class ItemCullingBelt
{
	private static double itemX;
	private static double itemY;
	private static double itemZ;
	
	@Inject(method="renderItems", at=@At(value="INVOKE", target="Lcom/mojang/blaze3d/matrix/MatrixStack;translate(DDD)V", ordinal=2), locals=LocalCapture.CAPTURE_FAILHARD)
	public void renderItems(BeltTileEntity te, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffer,
			int light, int overlay, CallbackInfo ci,
			Direction beltFacing, Vector3i directionVec, Vector3d beltStartOffset, BeltSlope slope, int verticality,
			boolean slopeAlongX, boolean onContraption, Iterator<List<TransportedItemStack>> var14, TransportedItemStack transported, float offset,
			float sideOffset, float verticalMovement, Vector3d offsetVec,
			boolean onSlope, boolean tiltForward, float slopeAngle, boolean alongX)
	{
		itemX = te.getBlockPos().getX() + beltStartOffset.x + offsetVec.x + (alongX ? sideOffset : 0);
		itemY = te.getBlockPos().getY() + beltStartOffset.y + offsetVec.y;
		itemZ = te.getBlockPos().getZ() + beltStartOffset.z + offsetVec.z + (alongX ? 0 : sideOffset);
	}
	
	@ModifyVariable(method="renderItems", at=@At(value="STORE"), ordinal=4, remap=false)
	public int updateItemCount(int count)
	{
	    int dist = PaBConfig.getInstance().itemCullingBelt.get();
	    if(dist <= 0) return count;
	    
		Minecraft client = Minecraft.getInstance();
	    Vector3d cameraMC = client.gameRenderer.getMainCamera().getPosition();
	    
	    double dX = cameraMC.x - itemX;
	    double dY = cameraMC.y - itemY;
	    double dZ = cameraMC.z - itemZ;
	    
	    if(dist > 0 && dX*dX+dY*dY+dZ*dZ > (double)(dist*dist))
	    	return -1;
	    else
	    	return count;
	}
}
