package com.underscorekilburn.patchesandbeyond;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.blackgear.cavebiomes.core.registries.CaveBiomes;

import biomesoplenty.api.biome.BOPBiomes;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLModIdMappingEvent;
import net.minecraftforge.fml.event.lifecycle.FMLModIdMappingEvent.ModRemapping;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryManager;
import net.minecraftforge.server.ServerMain;
import net.minecraft.client.Minecraft;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;

@Mod(PatchesAndBeyond.MODID)
public class PatchesAndBeyond
{
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MODID = "patchesandbeyond";
	
	public PatchesAndBeyond()
	{
		MinecraftForge.EVENT_BUS.addListener(PatchesAndBeyond::registerCommands);
		MinecraftForge.EVENT_BUS.addListener(PatchesAndBeyond::onIdMappingChanged);
	}
	
	static public void registerCommands(RegisterCommandsEvent e)
	{
		e.getDispatcher().register(Commands.literal("printbiome").executes(ctx -> {
			Entity src = ctx.getSource().getEntity();
			if(src != null)
			{
				World level = src.level;
			
				Biome biome = level.getBiome(new BlockPos(src.getX(), src.getY(), src.getZ()));
				int id = level.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getId(biome);
				
				String name = level.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getKey(biome).toString();
				
				PatchesAndBeyond.LOGGER.info("printbiome {} ({})", name, id);
				ctx.getSource().sendSuccess(new StringTextComponent(name + " (" + id + ")"), false);
			}
			
			MutableRegistry<Biome> biomes = ctx.getSource().getServer().registryAccess().registryOrThrow(Registry.BIOME_REGISTRY);
			PatchesAndBeyond.LOGGER.info("{} -> {}", 154, biomes.byId(154).getRegistryName().toString());
			PatchesAndBeyond.LOGGER.info("{} -> {}", 159, biomes.byId(159).getRegistryName().toString());
			
			int idFromBiome = biomes.getId(CaveBiomes.CAVE.get());
			int idFromRegistryManager = RegistryManager.ACTIVE.getRegistry(Registry.BIOME_REGISTRY).getID(CaveBiomes.CAVE.get());
			PatchesAndBeyond.LOGGER.info("cave biome: id from registry access ({}), id from registry manager ({})", idFromBiome, idFromRegistryManager);
			
			Biome bopBiome = ForgeRegistries.BIOMES.getValue(BOPBiomes.meadow.location());
			idFromBiome = biomes.getId(bopBiome);
			idFromRegistryManager = RegistryManager.ACTIVE.getRegistry(Registry.BIOME_REGISTRY).getID(bopBiome);
			PatchesAndBeyond.LOGGER.info("BOP meadow biome: id from registry access ({}), id from registry manager ({})", idFromBiome, idFromRegistryManager);
			
			return 1;
		}));
	}
	
	static public void onIdMappingChanged(FMLModIdMappingEvent e)
	{
		LOGGER.info("Mod ID mappings changed (frozen={})", e.isFrozen);
		for(ModRemapping m : e.getRemaps(ForgeRegistries.Keys.BIOMES.location()))
		{
			LOGGER.info("  {}: {} -> {}", m.key.toString(), m.oldId, m.newId);
		}
	}
}
