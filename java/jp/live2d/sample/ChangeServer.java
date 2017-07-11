package jp.live2d.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by PIK-R-5 on 1/26/2017.
 */

public class ChangeServer extends Activity  {
    public ListView listView;
    String srvaddr;
    public static ArrayList<String> ArrayofServer = new ArrayList<String>();
    public static ArrayList<String> ArrayofServerAddress = new ArrayList<String>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        ArrayofServer.clear();
        ArrayofServerAddress.clear();
        ArrayofServer.add(0,"Server 1 (default)");
        ArrayofServer.add(1,"Server 2 ");
        ArrayofServerAddress.add(0,"programoserver01.000webhostapp.com/ProgramO");
        ArrayofServerAddress.add(1,"programoserver02.esy.es/ProgramO");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changeserver);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ChangeServer.this,
                android.R.layout.simple_list_item_1, ArrayofServer);
        listView = (ListView) findViewById(R.id.listview3);
        listView.setAdapter(adapter);
        listView.setVisibility(View.VISIBLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int selectedIndex = (position);
                srvaddr = ArrayofServerAddress.get(selectedIndex);
                SampleApplication s1 = new SampleApplication();
                s1.addrreceiver(srvaddr);
                quitnow();

            }
        });}
public void quitnow(){
    this.finish();
    }


    }


