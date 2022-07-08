package com.underscorekilburn.patchesandbeyond.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.simibubi.create.content.contraptions.components.structureMovement.mounted.CartAssemblerTileEntity;

import net.minecraft.entity.Entity;

@Pseudo
@Mixin(CartAssemblerTileEntity.class)
public class MinecartContraptionFix
{
	/**
	 * Fixes minecart contraption spawning with a slight offset when assembling.
	 */
	@ModifyArg(method="assemble(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/item/minecart/AbstractMinecartEntity;)V",
			at=@At(value="INVOKE", target="Lnet/minecraft/world/World;addFreshEntity(Lnet/minecraft/entity/Entity;)Z"))
	private Entity updateEntity(Entity ent)
	{
		ent.setPos(ent.getX() + 0.5, ent.getY(), ent.getZ() + 0.5);
		return ent;
	}
}
