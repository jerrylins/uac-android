package de.mytfg.uac.wave.stream;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.widget.Toast;

import java.io.IOException;

import de.mytfg.uac.util.AverageTimer;

public class OutputWaveAndroid extends OutputWave {
  private Context context;

  private static final int BUFFER_SIZE = 16;

  //private SourceDataLine line;
  private AudioTrack track;
  private short[] samples;
  int pointer = 0;

  public OutputWaveAndroid(int samplingRate, Context context) {
    this.context = context;

    int bufferSize = Math.max(BUFFER_SIZE,
            AudioTrack.getMinBufferSize(
                    samplingRate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_FLOAT
            )
    );
    Toast.makeText(this.context, "Buffer Size=" + String.valueOf(bufferSize),
            Toast.LENGTH_LONG).show();

    track = new AudioTrack(
            AudioManager.STREAM_MUSIC,
            samplingRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize,
            AudioTrack.MODE_STREAM
    );

    track.setVolume(AudioTrack.getMaxVolume());
    track.play();

    samples = new short[BUFFER_SIZE];
  }

  @Override
  public void writeSample(double sample) throws IOException {
    AverageTimer.getTimer("Android").begin();
    samples[pointer] = (short) Math.round(Short.MAX_VALUE * sample);
    pointer++;
    if(pointer == BUFFER_SIZE) {
      flush();
    }
    AverageTimer.getTimer("Android").end();
  }

  public void flush() {
    AverageTimer.getTimer("Conversion").begin();
    track.write(samples, 0, samples.length);
    AverageTimer.getTimer("Conversion").end();
    pointer = 0;
  }

  public void close() {
  }

}
