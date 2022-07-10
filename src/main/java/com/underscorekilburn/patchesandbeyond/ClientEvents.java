package com.underscorekilburn.patchesandbeyond;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import team.chisel.common.init.ChiselTileEntities;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@Mod.EventBusSubscriber(modid = PatchesAndBeyond.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents
{
	@SubscribeEvent
	public static void init(final FMLClientSetupEvent event)
	{
		if(ModList.get().isLoaded("chisel") && PaBConfig.getInstance().autoChiselTransparencyFix.get())
			RenderTypeLookup.setRenderLayer(ChiselTileEntities.AUTO_CHISEL.get(), RenderType.cutout());
	}
}
