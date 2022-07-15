package com.underscorekilburn.patchesandbeyond.mixins.quests;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.ftb.mods.ftbquests.FTBQuestsEventHandler;
import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;

@Pseudo
@Mixin(FTBQuestsEventHandler.class)
public class FTBQuestsEventMixin
{
	@Inject(method="playerKill", at=@At("HEAD"), remap=false)
	private void prePlayerKill(LivingEntity entity, DamageSource source, CallbackInfoReturnable<ActionResultType> cir)
	{
		if(source.getEntity() instanceof ServerPlayerEntity)
			ServerQuestFile.INSTANCE.currentPlayer = (ServerPlayerEntity)source.getEntity();
	}
	
	@Inject(method="playerKill", at=@At("RETURN"), remap=false)
	private void postPlayerKill(LivingEntity entity, DamageSource source, CallbackInfoReturnable<ActionResultType> cir)
	{
		ServerQuestFile.INSTANCE.currentPlayer = null;
	}
}
