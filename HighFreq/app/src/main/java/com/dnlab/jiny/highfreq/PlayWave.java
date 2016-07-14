package com.dnlab.jiny.highfreq;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

/**
 * Created by jiny on 7/12/16.
 */
public class PlayWave {

    private final int SAMPLE_RATE = 44100;
    private AudioTrack audioTrack;
    private int buffersize = AudioTrack.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
    private int sampleCount;

    public PlayWave(){
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                buffersize, AudioTrack.MODE_STATIC);
    }

    public void setWave(int frequency){
        sampleCount = (int) ((float) SAMPLE_RATE / frequency);
        short samples[] = new short[sampleCount];
        int amplitude = 32767; // max size is 32767 = 2^15 - 1 = Short.MAX_VALUE
        double twopi = 2. * Math.PI;
//      double twopi = 8. * Math.atan(1.);
        double phase = 0.0;

        for(int i = 0; i < sampleCount; i++){
            samples[i] = (short) (amplitude * Math.sin(phase));
            phase += twopi * frequency * i / SAMPLE_RATE;
        }
        audioTrack.write(samples, 0, sampleCount);
    }

    public void start(){
        audioTrack.reloadStaticData();
        audioTrack.setLoopPoints(0, sampleCount, -1);
        audioTrack.setStereoVolume(AudioTrack.getMaxVolume(), AudioTrack.getMaxVolume());
        audioTrack.play();
    }

    public void stop(){
        audioTrack.stop();
    }

    public void sineGenerator(int frequency, int duration){
        // 44100 is the equivalent of 1 second
        // ex) play(1500, 44100) => play a 1500Hz sound for one second

        int playDuration = duration * 44100; // change second to duration
        int bufferSize = AudioTrack.getMinBufferSize(44100,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                bufferSize, AudioTrack.MODE_STREAM);

        // sine wave
        double[] sinSound = new double[44100];
        short[] sinBuffer = new short[playDuration];
        for(int i = 0; i < sinSound.length; i++){
            sinSound[i] = Math.sin((2.0 * Math.PI * i / (44100 / frequency))); // y(t) = A sin(2pi*f*t + p) = A sin(w*t + p)
            sinBuffer[i] = (short) (sinSound[i] * Short.MAX_VALUE);
        }

        audioTrack.setStereoVolume(AudioTrack.getMaxVolume(), AudioTrack.getMaxVolume());
        audioTrack.play();

        audioTrack.write(sinBuffer, 0, sinSound.length);
        audioTrack.stop();
        audioTrack.release();
    }
}
