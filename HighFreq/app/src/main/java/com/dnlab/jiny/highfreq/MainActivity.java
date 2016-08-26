package com.dnlab.jiny.highfreq;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private double frequencyOfTune = 440; // hz
    private double BIT0 = 18500; // hz
    private double BIT1 = 19500; // hz
    private int gapTime = 500; // ms
    private String toConvertText;
    private String asciiCode;

    Button wavePlayButton;
    Button waveStopButton;
    Button textConvertButton;
    Button generateButton;

    SeekBar frequencySeekBar;

    TextView asciiCodeView;
    TextView frequencyValue;

    EditText inputText;

    //SineWaveGenerator Test
    SineWaveGenerator sineWaveGenerator = new SineWaveGenerator();
    ConvertAscii convertAscii;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 텍스트를 입력 받아 아스키 코드로 변환하는 코드
        inputText = (EditText) findViewById(R.id.text_input);
        asciiCodeView = (TextView) findViewById(R.id.ascii_textView);
        textConvertButton = (Button) findViewById(R.id.convert_button);
        textConvertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toConvertText = inputText.getText().toString();
                convertAscii = new ConvertAscii(toConvertText);
                asciiCode = convertAscii.getCode();
                asciiCodeView.setText(asciiCode);
            }
        });
        generateButton = (Button) findViewById(R.id.generate_button);
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SineWaveGenerator bitSine = new SineWaveGenerator();
                bitSine.setDurationMs(gapTime);
                for(int i=0; i < asciiCode.length(); i++) {
                    if (convertAscii.getCode().charAt(i) == '0') {
                        bitSine.setFrequencyHz(BIT0);
                    } else {
                        bitSine.setFrequencyHz(BIT1);
                    }
                    bitSine.play();
                    try {
                        Thread.sleep(gapTime);
                    }catch (InterruptedException error){

                    }
                }
            }
        });


        // 원하는 주파수를 조정해서 발생시키는 코드

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
