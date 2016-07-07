package com.dnlab.jiny.highfreq;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private final int duration = 10; // second
    private final int sampleRate = 8000;
    private final int numberOfSamples = duration * sampleRate;
    private final double sample[] = new double[numberOfSamples];
    private final double frequencyOfTune = 180000; // hz

    private final byte generatedSound[] = new byte[2*numberOfSamples];

    Handler handler = new Handler();

    private boolean isplay = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume(){
        super.onResume();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                genTone();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        playSound();
                    }
                });
            }
        });

        thread.start();
    }

    public boolean playSound(){
        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT, numberOfSamples,
                AudioTrack.MODE_STATIC);
        audioTrack.write(generatedSound, 0, numberOfSamples);
        audioTrack.play();
        return isplay = true;
    }

    public void genTone(){
        for(int i=0; i<numberOfSamples; i++){
            sample[i] = Math.sin(2*Math.PI * i / (sampleRate/frequencyOfTune));
        }

        int index = 0;
        for(double dVal: sample){
            short value = (short) (dVal*32767); // 32767=0x7fff
            generatedSound[index++] = (byte) (value & 0x00ff);
            generatedSound[index++] = (byte) ((value & 0xff00)>>8);
        }
    }
}
