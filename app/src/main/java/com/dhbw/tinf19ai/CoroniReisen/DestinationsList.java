package com.dhbw.tinf19ai.CoroniReisen;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DestinationsList extends ListActivity {

    static final String[] sonne = new String[]{"Spanien","Italien","Madagaskar","Australien","Türkei","Malta","Ägypten","Brasilien","Mexiko"};
    static final String[] berge = new String[]{"Schweiz","USA","Deutschland","Australien","Vereinigtes Königreich von Großbritannien und Nordirland","Nordmazedonien","Italien","Griechenland","Belgien","Slowakei"};
    static final String[] stadt = new String[]{"Spanien","Polen","Nordmazedonien","Indien","Türkei","Japan","China","Deutschland","Russische Föderation","USA"};
    static final String[] natur = new String[]{"Marokko","Kanada","Neuseeland","Japan","Iran","Deutschland","Österreich","Thailand","Norwegen","Irland"};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.destinations_list);
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.destinations_listview,sonne);
        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(adapter);
/*        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });*/
    }
}
