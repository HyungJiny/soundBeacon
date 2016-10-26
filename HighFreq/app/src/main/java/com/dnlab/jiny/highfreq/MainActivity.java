package com.dnlab.jiny.highfreq;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private final double BIT0 = 18500; // hz
    private final double BIT1 = 19500; // hz
    private final double STARTBIT = 19000; // hz
    private final int gapTime = 110; // ms
    private final int startSignalDuration = 100; // ms
    private String toConvertText;
    private String asciiCode;

    Button textConvertButton;
    Button generateButton;
    TextView asciiCodeView;
    EditText inputText;
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
                playStartSignal();
                for(int i=0; i < asciiCode.length(); i++) {
                    if (convertAscii.getCode().charAt(i) == '0') {
                        bitSine.setFrequencyHz(BIT0);
                    } else {
                        bitSine.setFrequencyHz(BIT1);
                    }
                    bitSine.play();
//                    try {
//                        Thread.sleep(gapTime);
//                    }catch (InterruptedException error){
//                        System.out.println(error);
//                    }
                }
            }
        });
    }

    private void playStartSignal(){
        SineWaveGenerator sineWaveGenerator = new SineWaveGenerator(STARTBIT, startSignalDuration);
        sineWaveGenerator.play();
//        try {
//            Thread.sleep(gapTime);
//        }catch (InterruptedException error){
//            System.out.println(error);
//        }
    }

}
