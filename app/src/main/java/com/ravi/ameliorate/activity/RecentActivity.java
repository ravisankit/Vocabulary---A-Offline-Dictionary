package com.ravi.ameliorate.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.ravi.ameliorate.R;
import com.ravi.ameliorate.model.Contact;
import com.ravi.ameliorate.sqlite.DatabaseHandler;

public class RecentActivity extends AppCompatActivity {

    ListView listView;
    LinearLayout home,addWords;
    ImageView empty;
    TextView showEmpty;
    private int lastPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent);

        final DatabaseHandler db = new DatabaseHandler(this);

        listView = (ListView) findViewById(R.id.list);
        home = (LinearLayout) findViewById(R.id.home);
        addWords = (LinearLayout) findViewById(R.id.addWords);
        empty = (ImageView) findViewById(R.id.empty);
        showEmpty = (TextView) findViewById(R.id.showEmpty);


        Log.d("Reading: ", "Reading all contacts..");
        List<Contact> contacts = db.getTenWords();

        Log.d("Reading: ", "Reading "+contacts.toString());
        final ArrayList<String> list = new ArrayList<String>();
        final ArrayList<String> meaningList = new ArrayList<String>();
        final ArrayList<String> meaningList2 = new ArrayList<String>();
        final ArrayList<String> pronunList = new ArrayList<String>();
        final ArrayList<String> synonymList = new ArrayList<String>();
        final ArrayList<String> antonymList = new ArrayList<String>();
        final ArrayList<String> sentenceList = new ArrayList<String>();

        if(!(contacts.size() == 0)) {
            for (int i = 0; i < contacts.size(); i++) {
                Contact cn = contacts.get(i);
                String name = cn.getName();
                String meaning = cn.getMeaning();
                String meaning2 = cn.getMeaning2();
                String pronun = cn.getPronun();
                String synonym = cn.getSynonym();
                String antonym = cn.getAntonym();
                String sentence = cn.getSentence();

                list.add(name);
                meaningList.add(meaning);
                meaningList2.add(meaning2);
                pronunList.add(pronun);
                synonymList.add(synonym);
                antonymList.add(antonym);
                sentenceList.add(sentence);
                Log.e("ravi", "list " + list.toString());
            }


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(RecentActivity.this, android.R.layout.simple_dropdown_item_1line, list);
            listView.setAdapter(adapter);
        }else{
            empty.setVisibility(View.VISIBLE);
            showEmpty.setVisibility(View.VISIBLE);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.e("ravi ","position "+position );
                String word = list.get(position) ;
                String meaning = meaningList.get(position);
                String meaning2 = meaningList2.get(position);
                String pronun = pronunList.get(position);
                String synonym = synonymList.get(position);
                String antonym = antonymList.get(position);
                String sentence = sentenceList.get(position);
                Log.e("ravi ","word "+word + meaning);

                Intent intent = new Intent(RecentActivity.this,Description.class);
                intent.putExtra("bool","desc");
                intent.putExtra("word",word);
                intent.putExtra("meaning",meaning);
                intent.putExtra("meaning2",meaning2);
                intent.putExtra("pronun",pronun);
                intent.putExtra("synonym",synonym);
                intent.putExtra("antonym",antonym);
                intent.putExtra("sentence",sentence);
                finish();
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        for (Contact cn : contacts) {
            String log = "Id: " + cn.getId() + " ,word: " + cn.getName() + " ,meaning: " + cn.getMeaning()+ " ,meaning2: " + cn.getMeaning2()
                    + " ,Pronun: " + cn.getPronun() + " ,Synonym: " + cn.getSynonym()+ " ,Antonym: " + cn.getAntonym()+ " ,Sentence: " + cn.getSentence();
            // Writing Contacts to log
            Log.d("dictionary : ", log);
        }

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RecentActivity.this,Dictionary.class);
                finish();
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

            }
        });

        addWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RecentActivity.this,AddWord.class);
                finish();
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

            }
        });

        empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RecentActivity.this,AddWord.class);
                finish();
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(RecentActivity.this,Dictionary.class);
        finish();
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

    }
}
