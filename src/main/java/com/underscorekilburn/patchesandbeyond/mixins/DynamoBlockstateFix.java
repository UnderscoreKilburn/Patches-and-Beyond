package com.underscorekilburn.patchesandbeyond.mixins;

import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import cofh.core.tileentity.TileCoFH;
import cofh.thermal.lib.block.TileBlockDynamo;
import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.block.Block;

@Pseudo
@Mixin(TileBlockDynamo.class)
public class DynamoBlockstateFix
{
	/**
	 * Fixes Dynamos from Thermal becoming waterlogged when loading an older world due
	 * to their waterlogged state defaulting to true.
	 */
	@Inject(method="<init>(Lnet/minecraft/block/AbstractBlock$Properties;Ljava/util/function/Supplier;)V", at=@At("TAIL"), remap=false)
	private void init(Properties builder, Supplier<? extends TileCoFH> supplier, CallbackInfo info)
	{
		Block self = (Block)(Object)this;
		self.defaultBlockState = self.defaultBlockState.setValue(BlockStateProperties.WATERLOGGED, false);
	}
}
