package com.example.fukuokadota.joyanokane;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity {

    final int limit = 108;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final View kane = findViewById(R.id.kane);
        count = limit;

        final SoundPool sp;
        if (Build.VERSION.SDK_INT > 20) {
            sp = new SoundPool.Builder().setMaxStreams(5).setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).build()).build();
        } else {
            sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }
        final int id = sp.load(this, R.raw.kane, 0);

        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                kane.setClickable(true);
                Toast.makeText(getApplicationContext(), "準備完了！", Toast.LENGTH_SHORT).show();
            }
        });

        kane.setOnClickListener(new View.OnClickListener() {
            long previous;

            @Override
            public void onClick(View v) {
                long now = System.currentTimeMillis();
                if (count > 0 && now > previous + 5000) {
                    sp.play(id, 1.0f, 1.0f, 0, 0, 1.0f);
                    ((TextView) findViewById(R.id.countTextView)).setText(--count + "");
                    previous = now;
                }
            }
        });

        kane.setClickable(false);

        Timer timer = new Timer();
        final Handler h = new Handler();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView) findViewById(R.id.timerTextView)).setText(getRestTimeForNextYear(System.currentTimeMillis()));
                    }
                });
            }

            public String getRestTimeForNextYear(long from){
                final Time to = new Time("Asia/Tokyo");
                to.setToNow();
                to.set(0, 0, 0, 1, 1, to.year + 1);

                return getRestTime(from, to.toMillis(false));
            }

            public String getRestTime(long from, long to) {
                long diff = (to - from) / 1000;
                long sec = diff % 60;
                diff /= 60;
                long min = diff % 60;
                diff /= 60;
                long hour = diff % 24;

                return String.format("%02d:%02d:%02d",hour, min, sec);
            }
        }, 0, 10);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
