package com.underscorekilburn.patchesandbeyond.mixins.quests;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.underscorekilburn.patchesandbeyond.PatchesAndBeyond;

import org.spongepowered.asm.mixin.injection.At;

import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.reward.Reward;
import net.minecraft.entity.player.ServerPlayerEntity;

@Pseudo
@Mixin(TeamData.class)
public class TeamDataMixin
{
	private static boolean isCheckingAutoCompletion = false;
	
	@Inject(method="checkAutoCompletion", at=@At("HEAD"), remap=false)
	public void preCheckAutoCompletion(Quest quest, CallbackInfo ci)
	{
		isCheckingAutoCompletion = true;
	}

	@Inject(method="checkAutoCompletion", at=@At("RETURN"), remap=false)
	public void postCheckAutoCompletion(Quest quest, CallbackInfo ci)
	{
		isCheckingAutoCompletion = false;
	}
	
	@Inject(method="claimReward(Lnet/minecraft/entity/player/ServerPlayerEntity;Ldev/ftb/mods/ftbquests/quest/reward/Reward;Z)V", at=@At("HEAD"), cancellable=true, remap=false)
	public void claimReward(ServerPlayerEntity player, Reward reward, boolean notify, CallbackInfo ci)
	{
		if(isCheckingAutoCompletion && ServerQuestFile.INSTANCE.currentPlayer != null)
			PatchesAndBeyond.LOGGER.info("auto-claiming reward {} for player {}", reward.getPath(), ServerQuestFile.INSTANCE.currentPlayer.getScoreboardName());
		
		if(isCheckingAutoCompletion && reward.isTeamReward() && ServerQuestFile.INSTANCE.currentPlayer != null && ServerQuestFile.INSTANCE.currentPlayer != player)
			ci.cancel();
	}
}
