package com.underscorekilburn.patchesandbeyond.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import appeng.items.materials.MaterialItem;
import appeng.items.misc.CrystalSeedItem;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Pseudo
@Mixin({MaterialItem.class, CrystalSeedItem.class})
public class SingularityCollisionFix
{
	/**
	 * Fixes singularities and crystal seeds not falling properly when coming out of a pair of crushing wheels (and other possibly bugged interaction with Create blocks).
	 */
	@Inject(method="createEntity(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/Entity;",
			at=@At(value="RETURN"), remap=false)
	public Entity createEntity(final World w, final Entity location, final ItemStack itemstack, CallbackInfoReturnable<Entity> cir)
	{
		Entity ent = cir.getReturnValue();
		ent.getPersistentData().merge(location.getPersistentData());
		return ent;
	}
}
