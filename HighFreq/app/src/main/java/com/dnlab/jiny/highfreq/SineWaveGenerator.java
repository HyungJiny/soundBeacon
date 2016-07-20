package com.dnlab.jiny.highfreq;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class SineWaveGenerator {

    private double frequencyHz;
    private int durationMs;

    private AudioTrack audioTrack;
    private boolean isplay = false;

    public SineWaveGenerator(){
        this.frequencyHz = 440;
        this.durationMs = 2500;
    }

    public SineWaveGenerator(double frequency, int duration){
        this.frequencyHz = frequency;
        this.durationMs = duration;
    }

    private AudioTrack generateTone(){
        int count = (int) (44100.0 * 2.0 * (durationMs / 1000.0)) & ~1;
        short[] samples = new short[count];

        for(int i=0; i < count; i+=2){
            short sample = (short) (Math.sin(2 * Math.PI * i / (44100.0 / frequencyHz)) * 0x7FFF);
            samples[i] = sample;
            samples[i+1] = sample;
        }
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT,
                count * (Short.SIZE / 8), AudioTrack.MODE_STATIC);
        audioTrack.write(samples, 0, count);
        return audioTrack;
    }

    public void play(){
        generateTone();
        audioTrack.play();
        isplay = true;
    }

    public void stop(){
        audioTrack.stop();
        isplay = false;
    }

    public boolean isPlay(){
        return isplay;
    }

    public void setFrequencyHz(double frequency){
        this.frequencyHz = frequency / 2;
    }

    public void setDurationMs(int duration){
        this.durationMs = duration;
    }

}
