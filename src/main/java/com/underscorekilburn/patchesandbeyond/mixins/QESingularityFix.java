package com.underscorekilburn.patchesandbeyond.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import appeng.entity.SingularityEntity;
import net.minecraft.entity.Entity;

@Pseudo
@Mixin(SingularityEntity.class)
public class QESingularityFix
{
	/**
	 * Forces the velocity of quantum entangled singularities to zero when created by exploding a singularity.
	 * This was originally handled by a KubeJS script but also resulted in quantum entangled singularities immediately
	 * dropping to the ground when thrown by a player or a mechanical belt.
	 */
	@ModifyArg(method="*(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)V",
			at=@At(value="INVOKE", target="Lnet/minecraft/world/World;addFreshEntity(Lnet/minecraft/entity/Entity;)Z"), index=0)
	private Entity updateEntity(Entity ent)
	{
		ent.setDeltaMovement(0.0, 0.0, 0.0);
		return ent;
	}
}
