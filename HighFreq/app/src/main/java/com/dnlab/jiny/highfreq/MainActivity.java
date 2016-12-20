package com.dnlab.jiny.highfreq;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private final double BIT00 = 18400; // hz
    private final double BIT01 = 18800; //hz
    private final double BIT10 = 19200; // hz
    private final double BIT11 = 19600; //hz
    private final double STARTBIT = 19000; // hz
    private final int gapTime = 100; // ms
    private final int startSignalDuration = 300; // ms
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
                for(int i=1; i < asciiCode.length(); i=i+2) {
                    if (convertAscii.getCode().charAt(i) == '0') {
                        if(convertAscii.getCode().charAt(i+1) == '0'){
                            bitSine.setFrequencyHz(BIT00);
                        }else{
                            bitSine.setFrequencyHz(BIT01);
                        }
                        //bitSine.setFrequencyHz(BIT0);
                    } else if(convertAscii.getCode().charAt(i) == '1') {
                        if(convertAscii.getCode().charAt(i+1) == '0'){
                            bitSine.setFrequencyHz(BIT10);
                        }else{
                            bitSine.setFrequencyHz(BIT11);
                        }
                        //bitSine.setFrequencyHz(BIT1);
                    }
                    bitSine.play();
                    try {
                        Thread.sleep(30);
                    }catch (InterruptedException error){
                        System.out.println(error);
                   }
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
