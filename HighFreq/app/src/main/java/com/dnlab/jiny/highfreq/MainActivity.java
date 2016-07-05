package com.dnlab.jiny.highfreq;

import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    SoundPool highFrequenceSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        highFrequenceSound = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        int sound167khz = highFrequenceSound.load(this, R.raw.167khz, 1);
        
    }


}
