package com.dhbw.tinf19ai.CoroniReisen;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.osmdroid.util.GeoPoint;

public class DestinationsList extends ListActivity {

    static final String[] sonne = new String[]{"Spanien","Italien","Madagaskar","Australien","Türkei","Malta","Ägypten","Brasilien","Mexiko"};
    static final String[] berge = new String[]{"Schweiz","USA","Deutschland","Australien","Vereinigtes Königreich von Großbritannien und Nordirland","Nordmazedonien","Italien","Griechenland","Belgien","Slowakei"};
    static final String[] stadt = new String[]{"Spanien","Polen","Nordmazedonien","Indien","Türkei","Japan","China","Deutschland","Russische Föderation","USA"};
    static final String[] natur = new String[]{"Marokko","Kanada","Neuseeland","Japan","Iran","Deutschland","Österreich","Thailand","Norwegen","Irland"};
    public String eingabe_marker_btn;
    public static String eingabe;
    public TextView title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.destinations_list);
        title = (TextView) findViewById(R.id.tx_title);
        final ArrayAdapter adapter;
        String btn = MapFragment.btn;


        switch(btn){
            case "sonne": adapter = new ArrayAdapter<String>(this,R.layout.destinations_listview,sonne);
                title.setText("Hier sehen sie warme und sonnige Länder:");
            break;
            case "berge": adapter = new ArrayAdapter<String>(this,R.layout.destinations_listview,berge);
                title.setText("Hier finden sie Länder in denen man viele Berge hat:");
                break;
            case "stadt": adapter = new ArrayAdapter<String>(this,R.layout.destinations_listview,stadt);
                title.setText("Hier finden sie Länder mit interessanten Städten:");
                break;
            case "natur": adapter = new ArrayAdapter<String>(this,R.layout.destinations_listview,natur);
                title.setText("Hier finden sie Länder mit viel Natur:");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + btn);
        }

        final ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               String eingabe= (String) adapterView.getItemAtPosition(i);
               // MapFragment mapFragment = new MapFragment();
                //mapFragment.searchAndCenterAddress(selectedFromList);
                //GeoPoint geoPoint = mapFragment.geoPoint;
                Intent newActivity1 = new Intent(DestinationsList.this, CountryDetails.class);
                startActivity(newActivity1);
            }
        });
    }
}
