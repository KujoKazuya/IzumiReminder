package jp.live2d.sample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * Created by PIK-R-5 on 10/4/2016.
 */

public class ScheduleAdd extends Activity{
    TimePicker tpr;
    DatePicker dpr;
    EditText etx;
    Button okbutton;
    Button clearall;
    ImageButton exitbutton1;
    DatabaseHandler db = new DatabaseHandler(this);
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.schedule);
       tpr = (TimePicker) findViewById(R.id.timePicker3);
       dpr= (DatePicker)findViewById(R.id.datePicker3);
        etx = (EditText) findViewById(R.id.editText3);
        ClickListenerS3 listenerS3 = new ClickListenerS3();
        etx.setOnClickListener(listenerS3);
        exitbutton1 = (ImageButton) findViewById(R.id.exitbutton);
        ClickListenerS4 listenerS4 = new ClickListenerS4();
        exitbutton1.setOnClickListener(listenerS4);

        okbutton = (Button) findViewById(R.id.buttonset);
        ClickListenerS1 listenerS1 = new ClickListenerS1();
        okbutton.setOnClickListener(listenerS1);
        clearall = (Button) findViewById(R.id.buttonclear);
        ClickListenerS2 listenerS2 = new ClickListenerS2();
        clearall.setOnClickListener(listenerS2);

    }

    class ClickListenerS4 implements View.OnClickListener {
        public void onClick(View v) {
        ScheduleAdd.this.finish();
        }
    }
    class ClickListenerS3 implements View.OnClickListener {
        public void onClick(View v) {
            AlertDialog.Builder alert = new AlertDialog.Builder(ScheduleAdd.this);
            final EditText edittext = new EditText(ScheduleAdd.this);
            alert.setMessage("Type your schedule here");
            alert.setTitle("Schedule");

            alert.setView(edittext);

            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //What ever you want to do with the value

                    //OR
                    String YouEditTextValue = edittext.getText().toString();
                    etx.setText(YouEditTextValue);
                }
            });

            alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // what ever you want to do with No option.
                }
            });

            alert.show();
        }
    }

    class ClickListenerS1 implements View.OnClickListener {
        public void onClick(View v) {
            Integer dobYear = dpr.getYear();
            Integer dobMonth = dpr.getMonth()+1;
            Integer dobDate = dpr.getDayOfMonth();
            StringBuilder sb=new StringBuilder();
            sb.append(dobDate.toString()).append("-").append(dobMonth.toString()).append("-").append(dobYear.toString());
            String datestr=sb.toString();
            Integer dobHour = tpr.getCurrentHour();
            Integer dobMinute = tpr.getCurrentMinute();
            StringBuilder sb1=new StringBuilder();
            sb1.append(dobHour.toString()).append(":").append(dobMinute.toString());
            String timestr=sb1.toString();
            String todostr=etx.getText().toString();
            db.addSchedule(new Schedule(datestr,timestr,todostr));
            etx.setText("");
            Toast.makeText(getApplicationContext(), "Your schedule has been set!", Toast.LENGTH_SHORT).show();

        }
    }
    class ClickListenerS2 implements View.OnClickListener {
        public void onClick(View v) {
            db.deleteAll();
            Toast.makeText(getApplicationContext(), "Your schedule has been cleared!", Toast.LENGTH_SHORT).show();

        }
    }
}
