package com.underscorekilburn.patchesandbeyond.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.audio.SoundEngine;

@Mixin(SoundEngine.class)
public class SoundEngineFix
{
	@Inject(method="play", at=@At(value="INVOKE", target="Lnet/minecraft/client/audio/Sound;shouldStream()Z", ordinal=0), cancellable=true)
	public void onPlay(ISound isound, CallbackInfo ci)
	{
		SoundEngine self = (SoundEngine)(Object)this;

		Sound sound = isound.getSound();
        float f1 = Math.max(sound.getVolume(), 1.0F) * (float)sound.getAttenuationDistance();
        if(isound.getAttenuation() != ISound.AttenuationType.NONE
        		&& self.listener.getListenerPosition().distanceToSqr(isound.getX(), isound.getY(), isound.getZ()) >= (double)(f1 * f1))
        {
        	ci.cancel();
        }
	}
}
