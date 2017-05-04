package com.ravi.ameliorate.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.ravi.ameliorate.R;
import com.ravi.ameliorate.model.Contact;
import com.ravi.ameliorate.sqlite.DatabaseHandler;


public class Dictionary extends AppCompatActivity implements View.OnClickListener,OnShowcaseEventListener{

    FloatingActionButton add;
    AutoCompleteTextView search;
    TextView link;
    ImageView submit,voice,share;
    String searchString=" ";
    Timer autoSuggestTimer;
    DatabaseHandler db;
    LinearLayout addWords,recent;

    ListView list;

    private int counter = 0;
    ShowcaseView sv;
    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        SharedPreferences sp = getSharedPreferences("dictionary",getApplicationContext().MODE_PRIVATE);
        db = new DatabaseHandler(this);

        search = (AutoCompleteTextView) findViewById(R.id.search);
        submit = (ImageView) findViewById(R.id.submit);
        link = (TextView) findViewById(R.id.link);
        add = (FloatingActionButton) findViewById(R.id.add);
        voice=(ImageView) findViewById(R.id.voice);
        addWords = (LinearLayout) findViewById(R.id.addWords);
        recent = (LinearLayout) findViewById(R.id.recent);
        share =(ImageView) findViewById(R.id.share);
        search.addTextChangedListener(searchWatcher);

        autoSuggestTimer = new Timer();

        String name = sp.getString("name","");
        link.setText("Welcome "+name);

        /*RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
        lps.setMargins(margin, margin, margin, margin);*/

        //Log.d("Insert: ", "Inserting ..");
        //db.addContact2(new Contact(wordString, meaning,meaning2St,pronunCSt,synonymsASt,antonymsASt,makeSentenceSt));

        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts..");
        List<Contact> contacts = db.getAllWord();

        for (Contact cn : contacts) {
            String log = "Id: " + cn.getId() + " ,word: " + cn.getName() + " ,meaning: " + cn.getMeaning()+ " ,meaning2: " + cn.getMeaning2()
                    + " ,Pronun: " + cn.getPronun() + " ,Synonym: " + cn.getSynonym()+ " ,Antonym: " + cn.getAntonym()+ " ,Sentence: " + cn.getSentence();
            // Writing Contacts to log
            Log.d("dictionary : ", log);
        }

        if(isFirstTime()) {

            try {
                sv = new ShowcaseView.Builder(this)
                        .setTarget(new ViewTarget(findViewById(R.id.search)))
                        .setOnClickListener(this)
                        .setContentText("search your added recipe")
                        .setStyle(R.style.CustomShowcaseTheme2)
                        .build();
                sv.setButtonText("Next");
            }catch (Exception e){
                e.printStackTrace();
            }

        }


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchString = search.getText().toString();
                Intent intent = new Intent(Dictionary.this,Webview.class);
                intent.putExtra("search",searchString);
                intent.putExtra("bool","dict");
                finish();
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

            }
        });

        recent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Dictionary.this,RecentActivity.class);
                finish();
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

            }
        });

        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startVoiceRecognitionActivity();

            }
        });

        addWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Dictionary.this,AddWord.class);
                finish();
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Ameliorate - A Custom Dictionary. From now ownwards add your words,situable meaning, make sentence and many more. ";
                String url = "https://play.google.com/store/apps/details?id=com.ravi.ameliorate ";
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody+url);
                //sharingIntent.putExtra(Intent.ACTION_VIEW, url);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));

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

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(Dictionary.this, android.R.layout.simple_dropdown_item_1line, list);
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

                Intent intent = new Intent(Dictionary.this,Description.class);
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

        Intent intent = new Intent(Dictionary.this,Webview.class);
        intent.putExtra("search",searchString);
        intent.putExtra("bool","dict");
        finish();
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

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

                                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(Dictionary.this, android.R.layout.simple_dropdown_item_1line, list);
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

                                            Intent intent = new Intent(Dictionary.this,Description.class);
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

    private boolean isFirstTime() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("RanBefore", false);
        if (!ranBefore) {
            // first time
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RanBefore", true);
            editor.commit();
        }
        return !ranBefore;
    }


    @Override
    public void onShowcaseViewHide(ShowcaseView showcaseView) {

    }

    @Override
    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

    }

    @Override
    public void onShowcaseViewShow(ShowcaseView showcaseView) {

    }

    @Override
    public void onShowcaseViewTouchBlocked(MotionEvent motionEvent) {

    }

    private void setAlpha(float alpha, View... views) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            for (View view : views) {
                view.setAlpha(alpha);
            }
        }
    }

    @Override
    public void onClick(View v) {

        try {
            switch (counter) {


                case 0:
                    sv.setShowcase(new ViewTarget(voice), true);
                    sv.setContentTitle("Voice Search.");
                    break;

                case 1:
                    sv.setShowcase(new ViewTarget(submit), true);
                    sv.setContentTitle("Know your recipe just say OPEN GOOGLE");
                    break;

                case 2:
                    sv.setShowcase(new ViewTarget(addWords), true);
                    sv.setContentTitle("Add some food recipe.");
                    break;

                case 3:
                    sv.setShowcase(new ViewTarget(recent), true);
                    sv.setContentTitle("Know your food history.");
                    break;
                case 4:
                    sv.setTarget(Target.NONE);
                    sv.setContentTitle("Taste it. You love it.");
                    sv.setButtonText("Close");
                    setAlpha(0.4f, search, submit, add);
                    break;

                case 5:
                    sv.hide();
                    setAlpha(1.0f, search, add, submit);
                    break;
            }
            counter++;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
