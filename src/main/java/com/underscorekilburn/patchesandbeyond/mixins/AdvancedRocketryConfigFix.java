package com.underscorekilburn.patchesandbeyond.mixins;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import zmaster587.advancedRocketry.api.ARConfiguration;

@Pseudo
@Mixin(ARConfiguration.class)
public class AdvancedRocketryConfigFix
{
	/**
	 * Fixes Advanced Rocketry creating backups of its config file on startup due to some incorrectly defined configs.
	 */
	@Redirect(method="<init>(Lnet/minecraftforge/common/ForgeConfigSpec$Builder;)V",
			slice=@Slice(
					from=@At(value="FIELD", target="blackHolePowerMultiplier", opcode=Opcodes.PUTFIELD, shift=At.Shift.BY, by=-1, remap=false)
				),
			at=@At(value="INVOKE", target="Lnet/minecraftforge/common/ForgeConfigSpec$Builder;define(Ljava/lang/String;Ljava/lang/Object;)Lnet/minecraftforge/common/ForgeConfigSpec$ConfigValue;",
				ordinal=0, remap=false),
			remap=false)
	public ConfigValue<Object> fixBlackholeConfigDef(ForgeConfigSpec.Builder builder, String name, Object defaultValue)
	{
		return builder.define(name, 1.0);
	}

	@Redirect(method="<init>(Lnet/minecraftforge/common/ForgeConfigSpec$Builder;)V",
			slice=@Slice(
					from=@At(value="FIELD", target="breakableTorches", opcode=Opcodes.PUTSTATIC, shift=At.Shift.BY, by=-1, remap=false)
				),
			at=@At(value="INVOKE", target="Lnet/minecraftforge/common/ForgeConfigSpec$Builder;define(Ljava/lang/String;Ljava/lang/Object;Ljava/util/function/Predicate;)Lnet/minecraftforge/common/ForgeConfigSpec$ConfigValue;",
				ordinal=0, remap=false),
			remap=false)
	public ConfigValue<List<? extends Object>> fixBreakableTorchesConfig(ForgeConfigSpec.Builder builder, String name, Object defaultValue, Predicate<Object> pred)
	{
		return builder.defineList(name, new LinkedList<>(), (val) -> true);
	}
}
