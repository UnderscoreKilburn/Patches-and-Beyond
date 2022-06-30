package com.underscorekilburn.patchesandbeyond.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.Redirect;

import org.spongepowered.asm.mixin.injection.At;

import biomesoplenty.common.world.gen.feature.tree.BasicTreeFeature;
import biomesoplenty.common.world.gen.feature.tree.MahoganyTreeFeature;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.ChunkStatus;

@Pseudo
@Mixin({BasicTreeFeature.class, MahoganyTreeFeature.class})
public class BOPSaplingFix
{
	/**
	 * Fixes certain saplings from Biomes O' Plenty turning their base block into dirt when growing.
	 * This is done by preventing IForgeBlock::onPlantGrow from being called if a tree is generated outside of world generation (i.e. from a sapling).
	 */
	@SuppressWarnings("deprecation")
	@Redirect(method="place(Ljava/util/Set;Ljava/util/Set;Lnet/minecraft/world/IWorld;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/MutableBoundingBox;)Z",
			at=@At(value="INVOKE", target="Lnet/minecraft/block/Block;onPlantGrow(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/IWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;)V"),
			remap=false)
	private void onPlantGrow(Block block, BlockState state, IWorld world, BlockPos pos, BlockPos source)
	{
		if(!world.getChunk(pos).getStatus().isOrAfter(ChunkStatus.FULL))
			block.onPlantGrow(state, world, pos, source);
	}
}
