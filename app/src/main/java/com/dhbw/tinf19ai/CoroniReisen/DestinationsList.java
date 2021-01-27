package com.dhbw.tinf19ai.CoroniReisen;

/**
 * This class creates the lists of countries for the different buttons.
 * It displays the selected category with a suitable text and the list of selectable countries.
 */

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.osmdroid.util.GeoPoint;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DestinationsList extends ListActivity {

    //set lists
    static final String[] sonne = new String[]{"Spanien", "Italien", "Madagaskar", "Australien", "Türkei", "Malta", "Ägypten", "Brasilien", "Mexiko"};
    static final String[] berge = new String[]{"Schweiz", "USA", "Deutschland", "Australien", "Vereinigtes Königreich", "Nordmazedonien", "Italien", "Griechenland", "Belgien", "Slowakei"};
    static final String[] stadt = new String[]{"Spanien", "Polen", "Nordmazedonien", "Indien", "Türkei", "Japan", "China", "Deutschland", "Russische Föderation", "USA"};
    static final String[] natur = new String[]{"Marokko", "Kanada", "Neuseeland", "Japan", "Iran", "Deutschland", "Österreich", "Thailand", "Norwegen", "Irland"};
    public String eingabe_marker_btn;
    public TextView title, text;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.destinations_list);

        //TextViews
        title = (TextView) findViewById(R.id.tx_title);
        text = (TextView) findViewById(R.id.tx_introduction_categories);

        //button from MapFragment
        String btn = MapFragment.btn;

        final ArrayAdapter adapter;

        //set title and text
        switch (btn) {
            case "sonne":
                adapter = new ArrayAdapter<String>(this, R.layout.destinations_listview, sonne);
                title.setText("Sonne");
                text.setText("Hier sehen sie warme und sonnige Länder, perfekt für einen entspannten Strandurlaub!");
                break;
            case "berge":
                adapter = new ArrayAdapter<String>(this, R.layout.destinations_listview, berge);
                title.setText("Berge");
                text.setText("Hier finden sie Länder in denen man viele Berge hat, perfekt für Kletterausflüge und Wandertouren!");
                break;
            case "stadt":
                adapter = new ArrayAdapter<String>(this, R.layout.destinations_listview, stadt);
                title.setText("Stadt");
                text.setText("Hier finden sie Länder mit interessanten Städten. Welche Stadt werden sie als nächstes erkunden?");
                break;
            case "natur":
                adapter = new ArrayAdapter<String>(this, R.layout.destinations_listview, natur);
                title.setText("Natur");
                text.setText("Hier finden sie Länder mit viel Natur, perfekt zum entspannen und genießen von außergewöhnlicher Vegitation!");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + btn);
        }

        //set ListView
        final ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(adapter);

        //redirection to information of the selected country from the list
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //String selectedFromList =(listView.getItemAtPosition(i).toString());
                final String country = (String) adapterView.getItemAtPosition(i);
                Intent newActivity1 = new Intent(DestinationsList.this, CountryDetails.class);
                newActivity1.putExtra("country", country);
                startActivity(newActivity1);
            }
        });
    }
}
