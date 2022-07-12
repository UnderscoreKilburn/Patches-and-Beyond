package com.underscorekilburn.patchesandbeyond;

import java.util.HashMap;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;

import net.minecraftforge.common.ForgeConfigSpec;

public class PaBConfig
{
	private static final PaBConfig INSTANCE = new PaBConfig();
	
	private HashMap<String, ForgeConfigSpec.ConfigValue<Boolean>> rules;
	private final ForgeConfigSpec.Builder builder;
	private final ForgeConfigSpec spec;
	
	public ForgeConfigSpec.ConfigValue<Boolean> autoChiselTransparencyFix;
	
	public PaBConfig()
	{
		rules = new HashMap<String, ForgeConfigSpec.ConfigValue<Boolean>>();
		builder = new ForgeConfigSpec.Builder();
		
		builder.push("General");
		{
			addPatchRule(
					"dedicatedServerRegistryFix",
					"Forces registries to reload after loading an existing world on a dedicated server. Mainly fixes biome shuffling when adding or updating biome mods.\nThis has no effect on singleplayer or LAN worlds as Forge already fixes biome IDs on those.",
					true,
					"DedicatedServerRegistryFix"
			);
		}
		
		builder.push("AppliedEnergistics2");
		{
			addPatchRule(
					"crystalSizeFix",
					"Give crystal seeds from AE2 the same collision box as a standard item, fixes glitchy behaviour when dropped from a Create chute/funnel.",
					true,
					"CrystalSeedSizeFix"
			);
			addPatchRule(
					"removeQESingularityVelocity",
					"Remove the random velocity applied to quantum-entangled singularities from AE2 when created by exploding singularities.",
					true,
					"QESingularityFix"
			);
			addPatchRule(
					"singularityCrushingFix",
					"Fixes glitchy collisions from singularities and charged certus quartz when created by a pair of Create crushing wheels.",
					true,
					"SingularityCollisionFix"
			);
			builder.pop();
		}

		builder.push("Thermal");
		{
			addPatchRule(
					"dynamoDataFix",
					"Fixes dynamos from Thermal becoming waterlogged when loading a world previously played with a version of Thermal Foundation older than 1.5.0.14.",
					true,
					"DynamoBlockstateFix"
			);
			addPatchRule(
					"grenadeRegistrationFix",
					"Fixes grenades from Thermal being registered with an incorrect name on Thermal Foundation 1.5.0.14.",
					true,
					"ThermalGrenadeRegistrationFix"
			);
			
			builder.pop();
		}

		builder.push("Create");
		{
			addPatchRule(
					"hosePulleyInputFix",
					"Fixes hose pulleys from Create not consuming input fluid when receiving more than 1000 mB per tick.",
					true,
					"HosePulleyFix"
			);
			addPatchRule(
					"bulkBlastingFix",
					"Fixes bulk blasting setups destroying items when a single input stack yields more than one stack of items.",
					true,
					"FanProcessingFix"
			);
			addPatchRule(
					"minecartContraptionOffsetFix",
					"Fixes minecart contraptions spawning at a slight offset when assembled.",
					true,
					"MinecartContraptionFix"
			);
			addPatchRule(
					"ropePulleyFix",
					"Fixes pulley contraptions sometimes phasing through solid blocks when unloaded then reloaded while in motion.",
					true,
					"RopePulleyFix"
			);
			
			builder.pop();
		}
		
		builder.push("BiomesOPlenty");
		{
			addPatchRule(
					"saplingDirtFix",
					"Fixes certain saplings from BOP overwriting their base block with dirt when growing.",
					true,
					"BOPSaplingFix", "BOPSaplingBigTreeFix"
			);
			builder.pop();
		}
		
		builder.push("Chisel");
		{
			addPatchRule(
					"jeiIntegrationFix",
					"Restores JEI integration for Chisel.",
					true,
					"ChiselJEIIntegrationFix"
			);
			autoChiselTransparencyFix = addBooleanConfig(
					"autoChiselTransparencyFix",
					"Fixes the auto chisel block having incorrect transparency.",
					true
			);
			builder.pop();
		}
		
		builder.push("ChiselsAndBits");
		{
			addPatchRule(
					"bitStorageDupeFix",
					"Fixes several item duplication bugs caused by bit storage tanks.",
					true,
					"BitStorageDuplicationFix"
			);
			builder.pop();
		}
		
		builder.push("AdvancedRocketry");
		{
			addPatchRule(
					"configFix",
					"Fixes Advanced Rocketry creating backups of its config file on startup.",
					true,
					"AdvancedRocketryConfigFix"
			);
			addPatchRule(
					"disableSuitHUD",
					"When set to true, the space suit will no longer display its installed modules in the top left corner of the screen.\nMainly intended for Create: Above and Beyond as spacesuit gear cannot be customized and comes with pre-installed modules.",
					true,
					"DisableSpacesuitHUD"
			);
			addPatchRule(
					"simplifiedRocketGUI",
					"When set to true, replaces the rocket interface with a simplified version which a single inventory slot for inserting planet ID chips into the rocket's guidance computer.\nMainly intended for Create: Above and Beyond as the guidance computer is the only craftable rocket module.",
					true,
					"SimplifiedRocketGUI"
			);
			addPatchRule(
					"gravityFix",
					"Fixes gravity modifiers not working on the moon (or other planets).",
					true,
					"AdvancedRocketryGravityFix", "AdvancedRocketryGravityMiscFix"
			);
			
		}
		
		spec = builder.build();
	}
	
	private ForgeConfigSpec.ConfigValue<Boolean> addPatchRule(String name, String desc, boolean defaultValue, String... mixins)
	{
		ForgeConfigSpec.ConfigValue<Boolean> config = builder.comment(desc).define(name, defaultValue);
		
		for(String m : mixins)
		{
			rules.putIfAbsent(m, config);
		}
		return config;
	}
	
	private ForgeConfigSpec.ConfigValue<Boolean> addBooleanConfig(String name, String desc, boolean defaultValue)
	{
		return builder.comment(desc).define(name, defaultValue);
	}
	
	static public void load(String path)
	{
		CommentedFileConfig config = CommentedFileConfig.of(path);
		config.load();
		INSTANCE.spec.setConfig(config);
	}
	
	static public boolean shouldApplyMixin(String name)
	{
		ForgeConfigSpec.ConfigValue<Boolean> rule = INSTANCE.rules.get(name);
		return rule != null && rule.get() == true;
	}
	
	static public ForgeConfigSpec spec()
	{
		return INSTANCE.spec;
	}
	
	static public PaBConfig getInstance()
	{
		return INSTANCE;
	}
}
