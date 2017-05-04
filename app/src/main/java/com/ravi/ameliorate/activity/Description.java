package com.ravi.ameliorate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.ravi.ameliorate.R;
import com.ravi.ameliorate.model.Contact;
import com.ravi.ameliorate.sqlite.DatabaseHandler;

public class Description extends AppCompatActivity {

    TextView word,pronun,meanings1,meanings2,
            synonymsM,antonymsM,makeSentence;

    String wordSt,pronunSt,meanings1St,meanings2St,
            synonymsMSt,antonymsMSt,sentenceSt;

    ImageView submit2,voice;

    AutoCompleteTextView search;
    String searchString=" ";
    Timer autoSuggestTimer;
    DatabaseHandler db;

    String bool;

    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        db = new DatabaseHandler(this);
        initialize();

        bool            = getIntent().getStringExtra("bool");
        wordSt          = getIntent().getStringExtra("word");
        meanings1St     = getIntent().getStringExtra("meaning");
        meanings2St     = getIntent().getStringExtra("meaning2");
        pronunSt        = getIntent().getStringExtra("pronun");
        synonymsMSt     = getIntent().getStringExtra("synonym");
        antonymsMSt     = getIntent().getStringExtra("antonym");
        sentenceSt      = getIntent().getStringExtra("sentence");

        getText(wordSt,meanings1St,meanings2St,pronunSt,synonymsMSt,antonymsMSt,sentenceSt);
        search.addTextChangedListener(searchWatcher);
        autoSuggestTimer = new Timer();

        submit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchString = search.getText().toString();

                if(bool.equals("dict")){
                    Intent intent = new Intent(Description.this,Webview.class);
                    intent.putExtra("search",searchString);
                    intent.putExtra("bool","dictW");
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }else{
                    Intent intent = new Intent(Description.this,Webview.class);
                    intent.putExtra("search",searchString);
                    intent.putExtra("bool","rect");
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }


            }
        });

        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startVoiceRecognitionActivity();

            }
        });

    }

    public void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Speak Loud! Dude");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    public void informationMenu(String match) {

        Log.e("ravi ","matchess"+match);

        List<Contact> contacts = db.getAllWords(match);

        final ArrayList<String> list = new ArrayList<String>();
        final ArrayList<String> meaningList = new ArrayList<String>();
        final ArrayList<String> meaningList2 = new ArrayList<String>();
        final ArrayList<String> pronunList = new ArrayList<String>();
        final ArrayList<String> synonymList = new ArrayList<String>();
        final ArrayList<String> antonymList = new ArrayList<String>();
        final ArrayList<String> sentenceList = new ArrayList<String>();

        for (int i=0;i<contacts.size();i++) {
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

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(Description.this, android.R.layout.simple_dropdown_item_1line, list);
        adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        search.setAdapter(adapter1);
        search.showDropDown();

        search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String word = list.get(position) ;
                String meaning = meaningList.get(position);
                String meaning2 = meaningList2.get(position);
                String pronun = pronunList.get(position);
                String synonym = synonymList.get(position);
                String antonym = antonymList.get(position);
                String sentence = sentenceList.get(position);
                Log.e("ravi ","word "+word + meaning);

                Intent intent = new Intent(Description.this,Description.class);
                intent.putExtra("bool","dict");
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

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            // Fill the list view with the strings the recognizer thought it
            // could have heard
            final ArrayList matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            //list.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, matches));
            // matches is the result of voice input. It is a list of what the
            // user possibly said.
            // Using an if statement for the keyword you want to use allows the
            // use of any activity if keywords match
            // it is possible to set up multiple keywords to use the same
            // activity so more than one word will allow the user
            // to use the activity (makes it so the user doesn't have to
            // memorize words from a list)
            // to use an activity from the voice input information simply use
            // the following format;
            // if (matches.contains("keyword here") { startActivity(new
            // Intent("name.of.manifest.ACTIVITY")

            Log.e("ravi","match"+matches.toString());
            if(matches.contains("open Google")){

                /*String [] newSearch = matches.toString().split(",");
                String newString = newSearch[0].replace("[open Google","");
                Log.e("ravi","match2 "+newString);*/
                googleOpen();

            }else {
                String split[] = matches.toString().split(",");
                String match = split[0].replace("[", "");
                informationMenu(match);
            }



        }
    }

    public void googleOpen(){

        if(bool.equals("dict")){
            Intent intent = new Intent(Description.this,Webview.class);
            intent.putExtra("search",searchString);
            intent.putExtra("bool","dictW");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }else{
            Intent intent = new Intent(Description.this,Webview.class);
            intent.putExtra("search",searchString);
            intent.putExtra("bool","rect");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }

    }

    public TextWatcher searchWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, final int start, int before, int count) {


            //db.open();
            final CharSequence s1=s;
            //final Cursor cursor = db.fetchAllContacts(s1.toString());
            if (search.getText().toString().trim().length() > 1 && (before < 2)) {

                Log.e("ravi", "time 2=" + System.currentTimeMillis());
                autoSuggestTimer.cancel();
                Log.e("ravi", "time 3=" + System.currentTimeMillis());
                autoSuggestTimer.purge();
                Log.e("ravi", "time 4=" + System.currentTimeMillis());
                autoSuggestTimer = new Timer();
                autoSuggestTimer.schedule(new TimerTask() {
                    public void run() {

                        runOnUiThread(new Runnable() {
                            public void run() {


                                if (!searchString.contentEquals(search.getText().toString())) {

                                    List<Contact> contacts = db.getAllWords(s1.toString());
                                    final ArrayList<String> list = new ArrayList<String>();
                                    final ArrayList<String> meaningList = new ArrayList<String>();
                                    final ArrayList<String> meaningList2 = new ArrayList<String>();
                                    final ArrayList<String> pronunList = new ArrayList<String>();
                                    final ArrayList<String> synonymList = new ArrayList<String>();
                                    final ArrayList<String> antonymList = new ArrayList<String>();
                                    final ArrayList<String> sentenceList = new ArrayList<String>();

                                    for (int i=0;i<contacts.size();i++) {
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

                                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(Description.this, android.R.layout.simple_dropdown_item_1line, list);
                                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    search.setAdapter(adapter1);
                                    search.showDropDown();


                                    search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

                                            Intent intent = new Intent(Description.this,Description.class);
                                            intent.putExtra("bool","dict");
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

                                }

                            }
                        });
                    }
                }, 200);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    @Override
    public void onBackPressed() {
        super.onBackPressed();


        if(bool.equals("rect")){
            Intent intent = new Intent(Description.this,RecentActivity.class);
            finish();
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }else if(bool.equals("desc")){
            Intent intent = new Intent(Description.this,RecentActivity.class);
            finish();
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }else{
            Intent intent = new Intent(Description.this,Dictionary.class);
            finish();
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        }

    }

    public void initialize(){
        voice=(ImageView) findViewById(R.id.voice);
        word = (TextView) findViewById(R.id.word);
        pronun = (TextView) findViewById(R.id.pronun);
        meanings1 = (TextView) findViewById(R.id.meanings1);
        meanings2 = (TextView) findViewById(R.id.meanings2);
        synonymsM = (TextView) findViewById(R.id.synonymsM);
        antonymsM = (TextView) findViewById(R.id.antonymsM);
        makeSentence = (TextView) findViewById(R.id.urSent);
        submit2 = (ImageView) findViewById(R.id.submit2);
        search = (AutoCompleteTextView) findViewById(R.id.search2);

    }

    public void getText(String wordSt,String meanings1St,String meanings2St,
                        String pronunSt, String synonymsMSt, String antonymsMSt,
                        String sentenceSt){

        word.setText(wordSt);
        pronun.setText("| "+pronunSt+" |");
        meanings1.setText("1. " + meanings1St);
        meanings2.setText("2. " + meanings2St);
        synonymsM.setText(synonymsMSt);
        antonymsM.setText(antonymsMSt);
        makeSentence.setText(sentenceSt);
    }

    public void clearField(){
       ;
        //pronun.setText(wordSt);
        /*meanings2.setText(wordSt);
        synonyms.setText(wordSt);
        synonymsM.setText(wordSt);
        antonyms.setText(wordSt);
        antonymsM.setText(wordSt);*/
    }
}
