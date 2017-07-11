/**
 *  You can modify and use this source freely
 *  only for the development of application related Live2D.
 *
 *  (c) Live2D Inc. All rights reserved.
 */

package jp.live2d.sample;

import jp.live2d.utils.android.FileManager;
import jp.live2d.utils.android.SoundManager;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.PatternSyntaxException;


public class SampleApplication extends Activity implements TextToSpeech.OnInitListener
{
	TextToSpeech tts;
	String text="default";
	String encodedText="default";
	String answer="default";
	String addrsrv = "programoserver01.000webhostapp.com/ProgramO";
	Date date = new Date();
	int count = 0;
	int count1 = 0;
	private SpeechRecognizer sr;
	private static final String TAG = "SampleApplication";

	TimePicker picker;
	TimePickerDialog timePickerD;
	Timer t=new Timer();
	String timed="";
	public ListView listView;
	public static ArrayList<String> ArrayofSchedule = new ArrayList<String>();
	public static ArrayList<String> ArrayofSchedule1 = new ArrayList<String>();
	String[] serverlist={"programoserver01.000webhostapp.com/ProgramO","programoserver02.esy.es/ProgramO"};


	final static int req = 1;

	//StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

	private static String protocol = "http://";

	private static String webdir = "/chatbot/conversation_start.php?say=";

	//JSON Node Names
	private static final String CONVO_ID = "convo_id";
	private static final String USERSAY = "usersay";
	private static final String BOTSAY = "botsay";
	private LAppLive2DManager live2DMgr ;
	static private Activity instance;
	private ImageButton buttonoption;
	private final int REQ_CODE_SPEECH_INPUT = 100;
	String mainweather;

	public SampleApplication( )
	{
		instance=this;
		if(LAppDefine.DEBUG_LOG)
		{
			Log.d( "", "==============================================\n" ) ;
			Log.d( "", "   Live2D Sample  \n" ) ;
			Log.d( "", "==============================================\n" ) ;
		}

		SoundManager.init(this);
		live2DMgr = new LAppLive2DManager() ;
	}


	 static public void exit()
    {
		SoundManager.release();
    	instance.finish();
    }


	
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);

		tts = new TextToSpeech(this, this);
      	setupGUI();
		DatabaseHandler db = new DatabaseHandler(this);

		Log.d("Reading: ", "Reading all list..");
		List<Schedule> schedule = db.getAllSchedule();

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, ArrayofSchedule);
		int listSize = ArrayofSchedule.size();
		listView = (ListView) findViewById(R.id.listview2);

		listView.setAdapter(null);


		for (int i = 0; i<listSize; i++){
			Log.d("Member name: ", ArrayofSchedule.get(i));
		}
		for (Schedule sch : schedule) {
			String log = "||Id|| "+sch.getID()+" ||Date|| " + sch.getDate() + " ||Time|| " + sch.getTime() + " ||Todo|| " + sch.getTodo() + " ||";
			// Writing Contacts to log
			Log.d("Name: ", log);
		}
      	FileManager.init(this.getApplicationContext());
		ArrayofSchedule.clear();
		listView.setVisibility(View.INVISIBLE);


		listView.setOnTouchListener(new OnSwipeTouchListener(this) {
			public void onSwipeRight() {
				swiperight();
			}

		});
		ArrayofSchedule1=ArrayofSchedule;
		sr = SpeechRecognizer.createSpeechRecognizer(this);        // 初始化识别工具，得到句柄
		sr.setRecognitionListener(new listener());



	}

	class listener implements RecognitionListener            // 回调类的实现
	{
		public void onReadyForSpeech(Bundle params)
		{
			Log.d(TAG, "onReadyForSpeech");
			//Toast.makeText(SampleApplication.this,"ready for your question",Toast.LENGTH_SHORT).show();
			ImageView image1 = (ImageView) findViewById(R.id.speakicon);
			image1.setVisibility(View.VISIBLE);

		}
		public void onBeginningOfSpeech()
		{
			Log.d(TAG, "onBeginningOfSpeech");
		}
		public void onRmsChanged(float rmsdB)
		{
			Log.d(TAG, "onRmsChanged");
		}
		public void onBufferReceived(byte[] buffer)
		{
			Log.d(TAG, "onBufferReceived");
		}
		public void onEndOfSpeech()
		{
			Log.d(TAG, "onEndofSpeech");
			count1=0;
			ImageView image1 = (ImageView) findViewById(R.id.speakicon);
			image1.setVisibility(View.INVISIBLE);
		}
		public void onError(int error)
		{
			Log.d(TAG,  "error " +  error);
			count1=0;
			ImageView image1 = (ImageView) findViewById(R.id.speakicon);
			image1.setVisibility(View.INVISIBLE);

		}
		public void onResults(Bundle results)
		{
			String str = new String();
			Log.d(TAG, "onResults " + results);
			ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

				Log.d(TAG, "result1 " + data.get(0));
				str += data.get(0);

			text=str;
			count1=0;
			ImageView image1 = (ImageView) findViewById(R.id.speakicon);
			image1.setVisibility(View.INVISIBLE);
			speakOut();


			// 显示被识别的数据
		}
		public void onPartialResults(Bundle partialResults)
		{
			Log.d(TAG, "onPartialResults");
		}
		public void onEvent(int eventType, Bundle params)
		{
			Log.d(TAG, "onEvent " + eventType);
		}
	}


	
	void setupGUI()
	{
    	setContentView(R.layout.activity_main);

        
        LAppView view = live2DMgr.createView(this) ;

        
        FrameLayout layout=(FrameLayout) findViewById(R.id.live2DLayout);
		layout.addView(view, 0, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		ImageView image1 = (ImageView) findViewById(R.id.speakicon);
		image1.setVisibility(View.INVISIBLE);


		ImageButton aBtn = (ImageButton)findViewById(R.id.talkbutton);
		ClickListener1 listener1 = new ClickListener1();
		aBtn.setOnClickListener(listener1);



		ImageButton bBtn = (ImageButton)findViewById(R.id.serverbutton);
		ClickListener2 listener2= new ClickListener2();
		bBtn.setOnClickListener(listener2);
		bBtn.setVisibility(View.INVISIBLE);

		ImageButton cBtn = (ImageButton)findViewById(R.id.managebutton);
		ClickListener3 listener3= new ClickListener3();
		cBtn.setOnClickListener(listener3);
		cBtn.setVisibility(View.INVISIBLE);

		ImageButton dBtn = (ImageButton)findViewById(R.id.alarmbutton);
		ClickListener4 listener4= new ClickListener4();
		dBtn.setOnClickListener(listener4);
		dBtn.setVisibility(View.INVISIBLE);

		ImageButton eBtn = (ImageButton)findViewById(R.id.charabutton);
		ClickListener5 listener5= new ClickListener5();
		eBtn.setOnClickListener(listener5);
		eBtn.setVisibility(View.INVISIBLE);

		ImageButton fBtn = (ImageButton)findViewById(R.id.helpbutton);
		ClickListener6 listener6= new ClickListener6();
		fBtn.setOnClickListener(listener6);


		ImageButton gBtn = (ImageButton)findViewById(R.id.maxbutton);
		ClickListener7 listener7= new ClickListener7();
		gBtn.setOnClickListener(listener7);
		gBtn.setOnTouchListener(new OnSwipeTouchListener(this) {
			public void onSwipeLeft() {
				swipeleft();
			}

		});

		ImageButton hBtn = (ImageButton)findViewById(R.id.minbutton);
		ClickListener8 listener8= new ClickListener8();
		hBtn.setOnClickListener(listener8);
		hBtn.setVisibility(View.INVISIBLE);










	}

	public void onInit(int status) {

		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.US);

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			} else {

				speakOut();
			}

		} else {
			Log.e("TTS", "Initilization Failed!");
		}

	}

	class ClickListener3 implements OnClickListener{
		public void onClick(View v) {
			Intent intent = new Intent(SampleApplication.this, ScheduleAdd.class);
			startActivity(intent);

		}
	}
	class ClickListener4 implements OnClickListener{
		public void onClick(View v) {
			openTimePicker(false);
		}
	}

	private void openTimePicker(boolean is24jam)
	{
		Calendar kalender=Calendar.getInstance();
		timePickerD = new TimePickerDialog(
				SampleApplication.this,
				timeSetListener,
				kalender.get(Calendar.HOUR_OF_DAY),
				kalender.get(Calendar.MINUTE),
				true);

		timePickerD.setTitle("Set the Alarm");
		timePickerD.setButton(TimePickerDialog.BUTTON_POSITIVE,"Ok",timePickerD);
		timePickerD.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (which == DialogInterface.BUTTON_NEGATIVE) {
							Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
							PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), req, intent, 0);
							AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
							alarmManager.cancel(pendingIntent);
							tts.speak("alarm canceled", TextToSpeech.QUEUE_FLUSH, null);
							live2DMgr.exprs1();
							Toast.makeText(SampleApplication.this, "alarm canceled", Toast.LENGTH_LONG).show();
						}
					}
				});
		timePickerD.show();
	}

	TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener()
	{

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

			Calendar calNow = Calendar.getInstance();
			Calendar calSet = (Calendar) calNow.clone();

			calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
			calSet.set(Calendar.MINUTE, minute);
			calSet.set(Calendar.SECOND, 0);
			calSet.set(Calendar.MILLISECOND, 0);

			if(calSet.compareTo(calNow) <= 0){

				calSet.add(Calendar.DATE, 1);
			}


			setAlarm(calSet);

		}};

	private void setAlarm(Calendar targetCal){

		final Calendar targetCal1 = targetCal;



				Intent intentAlarm = new Intent(SampleApplication.this, AlarmReceiver.class);


				AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

				alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal1.getTimeInMillis(), PendingIntent.getBroadcast(SampleApplication.this, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));

		tts.speak("alarm set", TextToSpeech.QUEUE_FLUSH, null);
		Toast.makeText(this, "Alarm set on " + targetCal1.getTime(), Toast.LENGTH_LONG).show();
		live2DMgr.exprs1();


	}
	class ClickListener5 implements OnClickListener{

		@Override
		public void onClick(View v) {
			Toast.makeText(getApplicationContext(), "change model", Toast.LENGTH_SHORT).show();
			live2DMgr.changeModel();//Live2D Event
			if(count==0){
				count++;
				ImageButton aBtn = (ImageButton)findViewById(R.id.talkbutton);
				aBtn.setClickable(false);
				aBtn.setEnabled(false);
				Toast.makeText(SampleApplication.this,"this model has not supported yet",Toast.LENGTH_SHORT).show();

			} else {
				count--;
				ImageButton aBtn = (ImageButton)findViewById(R.id.talkbutton);
				aBtn.setClickable(true);
				aBtn.setEnabled(true);
			}
		}
	}

	class ClickListener6 implements OnClickListener{

		@Override
		public void onClick(View v) {
			Uri uri = Uri.parse("https://drive.google.com/open?id=0BzBUTXiv04vxR3AwdlJHYUFGSWs"); // missing 'http://' will cause crashed
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		}
	}

	public void addrreceiver(String addrc){
		String newaddr = addrc;
		addrsrv=newaddr;
		//Toast.makeText(SampleApplication.this,"connection succesfully changed to "+addrc,Toast.LENGTH_SHORT).show();

	}

	public void swipeleft(){

		DatabaseHandler db = new DatabaseHandler(SampleApplication.this);
		List<Schedule> schedule = db.getAllSchedule();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(SampleApplication.this,
				android.R.layout.simple_list_item_1, ArrayofSchedule);
		listView = (ListView) findViewById(R.id.listview2);
		listView.setAdapter(adapter);
		listView.setVisibility(View.VISIBLE);


		listView.setVisibility(View.VISIBLE);
		ImageButton aBtn = (ImageButton)findViewById(R.id.talkbutton);
		ClickListener1 listener1 = new ClickListener1();
		aBtn.setOnClickListener(listener1);
		aBtn.setVisibility(View.VISIBLE);
		listView.getBackground().clearColorFilter();

		ImageButton bBtn = (ImageButton)findViewById(R.id.serverbutton);
		ClickListener2 listener2= new ClickListener2();
		bBtn.setOnClickListener(listener2);
		bBtn.setVisibility(View.VISIBLE);

		ImageButton cBtn = (ImageButton)findViewById(R.id.managebutton);
		ClickListener3 listener3= new ClickListener3();
		cBtn.setOnClickListener(listener3);
		cBtn.setVisibility(View.VISIBLE);

		ImageButton dBtn = (ImageButton)findViewById(R.id.alarmbutton);
		ClickListener4 listener4= new ClickListener4();
		dBtn.setOnClickListener(listener4);
		dBtn.setVisibility(View.VISIBLE);

		ImageButton eBtn = (ImageButton)findViewById(R.id.charabutton);
		ClickListener5 listener5= new ClickListener5();
		eBtn.setOnClickListener(listener5);
		eBtn.setVisibility(View.VISIBLE);

		ImageButton fBtn = (ImageButton)findViewById(R.id.helpbutton);
		ClickListener6 listener6= new ClickListener6();
		fBtn.setOnClickListener(listener6);


		ImageButton gBtn = (ImageButton)findViewById(R.id.maxbutton);
		ClickListener7 listener7= new ClickListener7();
		gBtn.setOnClickListener(listener7);
		gBtn.setVisibility(View.INVISIBLE);

		ImageButton hBtn = (ImageButton)findViewById(R.id.minbutton);
		ClickListener8 listener8= new ClickListener8();
		hBtn.setOnClickListener(listener8);
		hBtn.setVisibility(View.VISIBLE);
	}

	public void swiperight(){
		ArrayofSchedule.clear();
		listView.setAdapter(null);
		ImageButton aBtn = (ImageButton)findViewById(R.id.talkbutton);
		ClickListener1 listener1 = new ClickListener1();
		aBtn.setOnClickListener(listener1);
		aBtn.setVisibility(View.VISIBLE);


		ImageButton bBtn = (ImageButton)findViewById(R.id.serverbutton);
		ClickListener2 listener2= new ClickListener2();
		bBtn.setOnClickListener(listener2);
		bBtn.setVisibility(View.INVISIBLE);

		ImageButton cBtn = (ImageButton)findViewById(R.id.managebutton);
		ClickListener3 listener3= new ClickListener3();
		cBtn.setOnClickListener(listener3);
		cBtn.setVisibility(View.INVISIBLE);

		ImageButton dBtn = (ImageButton)findViewById(R.id.alarmbutton);
		ClickListener4 listener4= new ClickListener4();
		dBtn.setOnClickListener(listener4);
		dBtn.setVisibility(View.INVISIBLE);

		ImageButton eBtn = (ImageButton)findViewById(R.id.charabutton);
		ClickListener5 listener5= new ClickListener5();
		eBtn.setOnClickListener(listener5);
		eBtn.setVisibility(View.INVISIBLE);

		ImageButton fBtn = (ImageButton)findViewById(R.id.helpbutton);
		ClickListener6 listener6= new ClickListener6();
		fBtn.setOnClickListener(listener6);


		ImageButton gBtn = (ImageButton)findViewById(R.id.maxbutton);
		ClickListener7 listener7= new ClickListener7();
		gBtn.setOnClickListener(listener7);
		gBtn.setVisibility(View.VISIBLE);

		ImageButton hBtn = (ImageButton)findViewById(R.id.minbutton);
		ClickListener8 listener8= new ClickListener8();
		hBtn.setOnClickListener(listener8);
		hBtn.setVisibility(View.INVISIBLE);


		listView.setVisibility(View.INVISIBLE);

	}
	class ClickListener7 implements OnClickListener{

		@Override
		public void onClick(View v) {
			swipeleft();

		}
	}

	class ClickListener8 implements OnClickListener {

		@Override
		public void onClick(View v) {

			swiperight();



		}
	}

	class ClickListener2 implements OnClickListener{
		@Override
		public void onClick(View v) {
			AlertDialog.Builder builderSingle = new AlertDialog.Builder(SampleApplication.this);

			builderSingle.setTitle("Select A Server");

			final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SampleApplication.this, android.R.layout.select_dialog_singlechoice);
			arrayAdapter.add("Server 01 @ webhost");
			arrayAdapter.add("Server 02 @ idhostinger");

			builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});

			builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String strName = arrayAdapter.getItem(which);

					AlertDialog.Builder builderInner = new AlertDialog.Builder(SampleApplication.this);
					builderInner.setMessage(strName);
					builderInner.setTitle("Your Selected Server is");
					builderInner.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,int which) {
							if(which==0){
								addrsrv=serverlist[0];
							} else {
								addrsrv=serverlist[1];
							}
							Toast.makeText(SampleApplication.this,"connection succesfully changed to "+addrsrv,Toast.LENGTH_LONG).show();

							dialog.dismiss();

						}
					});
					builderInner.show();
				}
			});
			builderSingle.show();
		}
	}

		class ClickListener1 implements OnClickListener{

		@Override
		public void onClick(View v) {
			if (count1==0) {
				Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
						RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US.toString());
				sr.startListening(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH));
				count1++;
				ImageView image1 = (ImageView) findViewById(R.id.speakicon);
				image1.setVisibility(View.VISIBLE);
			} else {
				sr.cancel();
				count1--;
				ImageView image1 = (ImageView) findViewById(R.id.speakicon);
				image1.setVisibility(View.INVISIBLE);
			}
		}
	}
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case REQ_CODE_SPEECH_INPUT: {
				if (resultCode == RESULT_OK && null != data) {

					ArrayList<String> result = data
							.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
					text=(result.get(0));

					speakOut();


				}
				break;
			}

		}


	}

	public void getO(){




		try{
			encodedText = URLEncoder.encode(text, "UTF-8");
			Log.i("encoded", text);
		}catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		AsyncTask.execute(new Runnable() {
			@Override
			public void run() {
				JSONParser jParser = new JSONParser();
		JSONObject json = jParser.getJSONFromUrl(protocol+addrsrv+webdir+encodedText);

		try{
			answer = json.getString(BOTSAY);

			Log.i("answer",answer);
			answerspeak();


		}catch (JSONException e) {
			e.printStackTrace();
		} catch (NullPointerException n){
			n.printStackTrace();
		}
			}
		});


	}

	public void alarmvoice(){
		String[] splitArray = new String[10];

		try {
			 splitArray = text.split("\\W+");
		} catch (PatternSyntaxException ex) {
			//
		}

		Calendar calNow = Calendar.getInstance();
		Calendar calSet = (Calendar) calNow.clone();

		try {
			calSet.set(Calendar.HOUR_OF_DAY, Integer.parseInt(splitArray[2]));
			calSet.set(Calendar.MINUTE, Integer.parseInt(splitArray[3]));
			calSet.set(Calendar.SECOND, 0);
			calSet.set(Calendar.MILLISECOND, 0);
		}catch (RuntimeException e){
			e.printStackTrace();
		}

		if(calSet.compareTo(calNow) <= 0){
			//jika ternyata waktu lewat maka alarm akan di atur untuk besok
			calSet.add(Calendar.DATE, 1);
		}
		if(text.contains("p.m")){
			calSet.add(Calendar.HOUR_OF_DAY, 12);
		}
		Log.i("hour",splitArray[2]);
		Log.i("minute",splitArray[3]);

		setAlarm(calSet);

	}

public void alarmalert(){

	tts.speak("hey, wake up", TextToSpeech.QUEUE_FLUSH, null);
	live2DMgr.exprs1();
}

	private void speakOut() {
		Log.i("answerspeak",text);


		if(text.contains("default")) {
			Log.e("TTS", "Insert Command");
		}else if(text.contains("help")) {
			Uri uri = Uri.parse("https://drive.google.com/open?id=0BzBUTXiv04vxR3AwdlJHYUFGSWs"); // missing 'http://' will cause crashed
			tts.speak("i'll open the help page for you", TextToSpeech.QUEUE_FLUSH, null);
			live2DMgr.exprs1();
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
			Toast.makeText(this, "i'll open the help page for you", Toast.LENGTH_LONG).show();
		} else if (text.contains("time")){
			String formattedDate = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
			tts.speak(formattedDate,TextToSpeech.QUEUE_FLUSH,null);
			live2DMgr.exprs1();
			//alarmManager.cancel(pendingIntent);
			Toast.makeText(this, formattedDate, Toast.LENGTH_LONG).show();
		} else if (text.contains("date")){
			String formattedDate = new SimpleDateFormat("EEEE, dd/MMMM/yyyy").format(Calendar.getInstance().getTime());
			tts.speak(formattedDate,TextToSpeech.QUEUE_FLUSH,null);
			live2DMgr.exprs1();
			Toast.makeText(this, formattedDate, Toast.LENGTH_LONG).show();
		} else if (text.contains("alarm setting")){
			openTimePicker(false);
			tts.speak("you can start setting your alarm now", TextToSpeech.QUEUE_FLUSH, null);
			Toast.makeText(this, "you can start setting your alarm now", Toast.LENGTH_LONG).show();
			live2DMgr.exprs1();
		} else if (text.contains("server")){
			final EditText txtUrl = new EditText(this);
			txtUrl.setHint(addrsrv);
			new AlertDialog.Builder(this)
					.setTitle("Server Address")
					.setMessage("Insert Server Address Here")
					.setView(txtUrl)
					.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							String url = txtUrl.getText().toString();
							addrsrv=url.toString();
							dialog.dismiss();


						}
					})
					.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							dialog.cancel();
						}
					})
					.show();
			tts.speak("be careful when changing the address", TextToSpeech.QUEUE_FLUSH, null);
			Toast.makeText(this, "be careful, wrong setting, the app may crash", Toast.LENGTH_LONG).show();
			live2DMgr.exprs1();
		} else if (text.contains("change character")){
			Toast.makeText(getApplicationContext(), "change model", Toast.LENGTH_SHORT).show();
			live2DMgr.changeModel();//Live2D Event
			if(count==0){
				count++;
				ImageButton aBtn = (ImageButton)findViewById(R.id.talkbutton);
				aBtn.setClickable(false);
				aBtn.setEnabled(false);
				Toast.makeText(SampleApplication.this,"this model has not supported yet",Toast.LENGTH_SHORT).show();

			} else {
				count--;
				ImageButton aBtn = (ImageButton)findViewById(R.id.talkbutton);
				aBtn.setClickable(true);
				aBtn.setEnabled(true);
			}
		} else if (text.contains("set schedule")){
			Intent intent = new Intent(SampleApplication.this, ScheduleAdd.class);
			startActivity(intent);
			tts.speak("you can add your schedule here", TextToSpeech.QUEUE_FLUSH, null);
			Toast.makeText(this, "you can add your schedule here", Toast.LENGTH_LONG).show();

		} else if (text.contains("alarm cancel")) {
			Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), req, intent, 0);
			AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
			alarmManager.cancel(pendingIntent);
			tts.speak("alarm canceled", TextToSpeech.QUEUE_FLUSH, null);
			live2DMgr.exprs1();
			Toast.makeText(this, "alarm canceled", Toast.LENGTH_LONG).show();
		}else if(text.contains("set alarm")) {
			if(text.contains("o'clock")){
				text=text.replaceAll("o'clock","00");
				Log.d("text",text);
			}
			alarmvoice();
		} else if (text.contains("weather")){
			AsyncTask.execute(new Runnable() {
				@Override
				public void run() {
			//StrictMode.setThreadPolicy(policy);
			JSONParser jParser = new JSONParser();
			LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
			Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if (location!=null) {
				double longitude = location.getLongitude();
				double latitude = location.getLatitude();
				String reqweather = "http://api.openweathermap.org/data/2.5/weather?lat=" + Double.toString(latitude) + "&lon=" + Double.toString(longitude) + "&APPID=5af48df10577cfc9451cee42584982aa";
				Log.i("reqweather", reqweather);
				JSONObject json = jParser.getJSONFromUrl(reqweather);

				try {
					JSONArray weather = json.getJSONArray("weather");
					JSONObject d = weather.getJSONObject(0);
					mainweather = d.getString("main");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				tts.speak("the weather today is " + mainweather, TextToSpeech.QUEUE_FLUSH, null);
				live2DMgr.exprs1();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
				Toast.makeText(SampleApplication.this, "the weather today is " + mainweather, Toast.LENGTH_LONG).show();
					}
				});

			} else{
				Log.i("location problem","null");
			}
				}
			});




		} else if (text.matches(".*[a-zA-Z].*")) {
			getO();

		} else {
			Log.i("text",text);
		}

		//tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	}

public void answerspeak(){
	if (answer.matches(".*[a-zA-Z].*")) {
		Log.i("answer", answer);
		tts.speak(answer, TextToSpeech.QUEUE_FLUSH, null);
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(SampleApplication.this, answer, Toast.LENGTH_LONG).show();
			}
		});
		live2DMgr.exprs1();
	}else {
		Log.i("answer",answer);
	}
}




	public void onDestroy() {
		// Don't forget to shutdown tts!
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		super.onDestroy();
	}

	@Override
	protected void onResume()
	{
		//live2DMgr.onResume() ;
		super.onResume();
		Log.d("resume","resume");

	}



	@Override
	protected void onPause()
	{
		live2DMgr.onPause() ;
    	super.onPause();
		this.recreate();
	}
}
