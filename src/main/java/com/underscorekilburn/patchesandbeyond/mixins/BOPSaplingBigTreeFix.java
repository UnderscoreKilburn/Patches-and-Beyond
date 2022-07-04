package com.underscorekilburn.patchesandbeyond.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.Redirect;

import org.spongepowered.asm.mixin.injection.At;

import biomesoplenty.common.world.gen.feature.tree.BigTreeFeature;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldWriter;
import net.minecraft.world.chunk.ChunkStatus;

@Pseudo
@Mixin(BigTreeFeature.class)
public class BOPSaplingBigTreeFix
{
	/**
	 * Fixes certain saplings from Biomes O' Plenty turning their base block into dirt when growing.
	 * This prevents large oak-like trees from placing a dirt block at their base when not generated outside of world generation.
	 */
	@Redirect(method="place(Ljava/util/Set;Ljava/util/Set;Lnet/minecraft/world/IWorld;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/MutableBoundingBox;)Z",
			at=@At(value="INVOKE", target="Lbiomesoplenty/common/world/gen/feature/tree/BigTreeFeature;setBlock(Lnet/minecraft/world/IWorldWriter;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V", remap=true),
			remap=false)
	private void setBlock(BigTreeFeature tree, IWorldWriter world, BlockPos pos, BlockState state)
	{
		if(!(world instanceof IWorld && ((IWorld)world).getChunk(pos).getStatus().isOrAfter(ChunkStatus.FULL)))
			tree.setBlock(world, pos, state);
	}
}
