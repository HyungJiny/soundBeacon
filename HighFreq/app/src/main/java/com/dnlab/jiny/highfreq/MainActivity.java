package com.dnlab.jiny.highfreq;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private double frequencyOfTune = 440; // hz
    private double BIT0 = 18500;
    private double BIT1 = 19500;

    Button wavePlayButton;
    Button waveStopButton;
    SeekBar frequencySeekBar;
    TextView frequencyValue;

    //SineWaveGenerator Test
    SineWaveGenerator sineWaveGenerator = new SineWaveGenerator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frequencySeekBar = (SeekBar) findViewById(R.id.frequency_seekBar);
        frequencyValue = (TextView) findViewById(R.id.frequency_value_textView);
        frequencySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                if(progress < 20){
                    progress = 20;
                    seekBar.setProgress(progress);
                }
                frequencyValue.setText("현재 주파수 : " + progress + "Hz");
                frequencyOfTune = (double) progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        wavePlayButton = (Button) findViewById(R.id.play_button);
        wavePlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sineWaveGenerator.setFrequencyHz((double) frequencyOfTune);
                sineWaveGenerator.play();
            }
        });

        waveStopButton = (Button) findViewById(R.id.stop_button);
        waveStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sineWaveGenerator.stop();
            }
        });

    }

}
