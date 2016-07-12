package com.dnlab.jiny.highfreq;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private final int duration = 20; // second
    private final int sampleRate = 8000;
    //private final int numberOfSamples = duration * sampleRate;
    //private final double sample[] = new double[numberOfSamples];
    private double frequencyOfTune = 440; // hz

    //private final byte generatedSound[] = new byte[2*numberOfSamples];
    Handler handler = new Handler();

    //private boolean isplay = false;

    Button soundPlayButton;
    SeekBar frequencySeekBar;
    TextView frequencyValue;

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

        soundPlayButton = (Button) findViewById(R.id.play_button);
        soundPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //genTone();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //playSound();
                                sineGenerator((int) frequencyOfTune, duration);
                            }
                        });
                    }
                });
                thread.start();
            }
        });

    }

//    public boolean playSound(){
//        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
//                sampleRate, AudioFormat.CHANNEL_CONFIGURATION_MONO,
//                AudioFormat.ENCODING_PCM_16BIT, numberOfSamples,
//                AudioTrack.MODE_STATIC);
//        audioTrack.write(generatedSound, 0, numberOfSamples);
//        audioTrack.play();
//        return isplay = true;
//    }
//
//    public void genTone(){
//        for(int i=0; i<numberOfSamples; i++){
//            sample[i] = Math.sin((2*Math.PI - 0.001)*i / (sampleRate/frequencyOfTune));
//        }
//
//        int index = 0;
//        int ramp = numberOfSamples / 20;
//
//        for(int i = 0; i< ramp; i++){
//            final short val = (short) ((sample[i] * 32767) * i / ramp);
//            generatedSound[index++] = (byte) (val & 0x00ff);
//            generatedSound[index++] = (byte) ((val & 0xff00)>>>8);
//        }
//
//        for(int i = ramp; i< numberOfSamples - ramp; i++){
//            final short val = (short) ((sample[i] * 32767));
//            generatedSound[index++] = (byte) (val & 0x00ff);
//            generatedSound[index++] = (byte) ((val & 0xff00)>>>8);
//        }
//
//        for(int i = numberOfSamples - ramp; i< numberOfSamples; i++){
//            final short val = (short) ((sample[i] * 32767) * (numberOfSamples - i) / ramp);
//            generatedSound[index++] = (byte) (val & 0x00ff);
//            generatedSound[index++] = (byte) ((val & 0xff00)>>>8);
//        }
//    }

    private void sineGenerator(int frequency, int duration){
        // 44100 is the equivalent of 1 second
        // ex) play(1500, 44100) => play a 1500Hz sound for one second

        int playDuration = duration * 44100; // change second to duration
        int bufferSize = AudioTrack.getMinBufferSize(44100,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_8BIT);

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
