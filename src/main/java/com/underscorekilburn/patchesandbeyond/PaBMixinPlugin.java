package com.underscorekilburn.patchesandbeyond;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class PaBMixinPlugin implements IMixinConfigPlugin
{
	private static final String PREFIX = "com.underscorekilburn.patchesandbeyond.mixins";
	
	@Override
	public void onLoad(String mixinPackage)
	{
		PaBConfig.load("config/" + PatchesAndBeyond.MODID + ".toml");
	}
	
	@Override
	public String getRefMapperConfig() {return null;}
	
	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName)
	{
		if(mixinClassName.startsWith(PREFIX))
		{
			return PaBConfig.shouldApplyMixin(mixinClassName.substring(PREFIX.length()+1));
		}
		
		return false;
	}
	
	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}
	
	@Override
	public List<String> getMixins() {return null;}
	
	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
	
	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}
