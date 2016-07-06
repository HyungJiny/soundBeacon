package com.dnlab.jiny.highfreq;

import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    SoundPool highFrequenceSound;
    Button playButton;
    Button stopButton;
    private boolean isplay = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        highFrequenceSound = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        final int sound167khz = highFrequenceSound.load(this, R.raw.freq167sound, 1);

        playButton = (Button) findViewById(R.id.play_button);
        stopButton = (Button) findViewById(R.id.stop_button);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                highFrequenceSound.play(sound167khz, 1f, 1f, 0, 0 , 1f);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                highFrequenceSound.pause(sound167khz);
            }
        });

        
    }

    private void playSound(){
        //soundpool.play(soundload, leftVolume, rightVolume, priority, loop, frequency);
    }

    private void stopSound(){

    }


}
