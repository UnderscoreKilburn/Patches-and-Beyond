package com.underscorekilburn.patchesandbeyond.mixins;

import java.io.File;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.underscorekilburn.patchesandbeyond.PatchesAndBeyond;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.server.Main;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.storage.SaveFormat;
import net.minecraftforge.fml.FMLWorldPersistenceHook;

@Mixin(Main.class)
public class DedicatedServerRegistryFix
{
	static private SaveFormat.LevelSave levelSave = null;
	
	@ModifyVariable(method="main", at=@At(value="INVOKE", target="Lnet/minecraft/resources/DataPackRegistries;updateGlobals()V", shift=At.Shift.BY, by=-1))
	static private SaveFormat.LevelSave CaptureLevelSave(SaveFormat.LevelSave save)
	{
		levelSave = save;
		return save;
	}
	
	@ModifyVariable(method="main", at=@At(value="INVOKE", target="Lnet/minecraft/resources/DataPackRegistries;updateGlobals()V"))
	static private DynamicRegistries.Impl updateDynamicRegistries(DynamicRegistries.Impl oldRegistry)
	{
		if(levelSave != null)
		{
			PatchesAndBeyond.LOGGER.info("Loading an existing world, force reloading dynamic registries");
			File f = levelSave.getWorldDir().resolve("level.dat").toFile();
			try
			{
				if(f.exists())
				{
					CompoundNBT tag = CompressedStreamTools.readCompressed(f);
					(new FMLWorldPersistenceHook()).readData(levelSave, null, tag.getCompound("fml"));
				}
				else
				{
					PatchesAndBeyond.LOGGER.info("{} not found", f.getAbsolutePath());
					return oldRegistry;
				}
			}
			catch (Exception exception)
			{
				PatchesAndBeyond.LOGGER.info("Failed to read {}, skipping", f.getAbsolutePath());
				return oldRegistry;
			}
			
			DynamicRegistries.Impl newRegistry = DynamicRegistries.builtin();
			PatchesAndBeyond.LOGGER.info("Done reloading dynamic registries");
			return newRegistry;
		}
		
		return oldRegistry;
	}
}
