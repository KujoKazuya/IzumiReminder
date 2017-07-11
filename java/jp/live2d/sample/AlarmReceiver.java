package jp.live2d.sample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by PIK-R-5 on 11/12/2016.
 */

public class AlarmReceiver extends BroadcastReceiver {
    MediaPlayer mp;
    //SampleApplication sApp;
    public void onReceive(Context c, Intent arg1) {
        Log.d("alarm","alarm set");
        mp = MediaPlayer.create(c, R.raw.alarmtone);

        mp.start();


       //sApp.alarmalert();


    }

}
