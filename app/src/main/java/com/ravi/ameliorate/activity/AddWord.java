package com.ravi.ameliorate.activity;

import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.ravi.ameliorate.R;
import com.ravi.ameliorate.model.Contact;
import com.ravi.ameliorate.sqlite.DatabaseHandler;

import static android.R.attr.type;


public class AddWord extends AppCompatActivity {

    EditText wordEdit,meaningEdit,pronunC,meaning2,
            synonymsA,antonymsA,makeSentence;
    Button addWord;
    Context context;
    String wordString,meaning,pronunCSt,meaning2St,
            synonymsASt,antonymsASt,makeSentenceSt;

    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);

        context = this;

        initialize();

        addWord = (Button) findViewById(R.id.addWord);
        back = (ImageView) findViewById(R.id.back);

        final DatabaseHandler db = new DatabaseHandler(this);

        addWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getText();
                if(wordString.length()==0 ) {
                    Toast.makeText(context, "Please enter word.", Toast.LENGTH_SHORT).show();
                } else if(meaning.length()==0){
                    Toast.makeText(context, "Please enter atleast one meaning.", Toast.LENGTH_SHORT).show();
                } else{

                    Log.d("Insert: ", "Inserting ..");
                    db.addContact2(new Contact(wordString, meaning,meaning2St,pronunCSt,synonymsASt,antonymsASt,makeSentenceSt));

                    // Reading all contacts
                    Log.d("Reading: ", "Reading all contacts..");
                    List<Contact> contacts = db.getAllWord();

                    for (Contact cn : contacts) {
                        String log = "Id: " + cn.getId() + " ,word: " + cn.getName() + " ,meaning: " + cn.getMeaning()+ " ,meaning2: " + cn.getMeaning2()
                                + " ,Pronun: " + cn.getPronun() + " ,Synonym: " + cn.getSynonym()+ " ,Antonym: " + cn.getAntonym()+ " ,Sentence: " + cn.getSentence();
                        // Writing Contacts to log
                        Log.d("dictionary : ", log);
                    }

                    Intent intent = new Intent(AddWord.this,RecentActivity.class);
                    finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

                    Toast.makeText(context, " Word added successfully. ", Toast.LENGTH_SHORT).show();


                    /*final NiftyDialogBuilder dialog = NiftyDialogBuilder.getInstance(context);

                    Effectstype effect;

                    effect = Effectstype.Fall;

                    dialog.isCancelableOnTouchOutside(true)
                            .withTitle(null)
                            .withMessage(null)
                            .withEffect(effect)
                            .setCustomView(R.layout.dialog, context)
                            .show();

                                        *//*final Dialog dialog = new Dialog(mContext);
                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        dialog.setContentView(R.layout.custom_alert);*//*
                    //dialog.setTitle(" Picture ");

                    TextView text = (TextView) dialog.findViewById(R.id.text);
                    text.setText(" Do you want to continue? ");

                    TextView invoicetext = (TextView) dialog.findViewById(R.id.invoicetext);
                    invoicetext.setText(" Custom Dict! ");

                    TextView dialogButton = (TextView) dialog
                            .findViewById(R.id.dialogButtonOK);
                    TextView dialogButtoncancel = (TextView) dialog
                            .findViewById(R.id.dialogButtoncancel);

                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog.dismiss();

                            //ClearText();

                        }
                    });

                    dialogButtoncancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog.dismiss();

                        }
                    });

                    dialog.show();*/
                    /*Intent intent = new Intent(AddWord.this,RecentActivity.class);
                    finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);*/



                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddWord.this,Dictionary.class);
                finish();
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AddWord.this,Dictionary.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

    }





    public void getText(){

        wordString = wordEdit.getText().toString();
        meaning = meaningEdit.getText().toString();
        meaning2St = meaning2.getText().toString();
        pronunCSt = pronunC.getText().toString();
        synonymsASt = synonymsA.getText().toString();
        antonymsASt = antonymsA.getText().toString();
        makeSentenceSt = makeSentence.getText().toString();

    }

    public void initialize(){

        wordEdit = (EditText) findViewById(R.id.word);
        meaningEdit = (EditText) findViewById(R.id.meaning);
        pronunC = (EditText) findViewById(R.id.pronunC);
        meaning2 = (EditText) findViewById(R.id.meaning2);
        synonymsA = (EditText) findViewById(R.id.synonymsA);
        antonymsA = (EditText) findViewById(R.id.antonymsA);
        makeSentence = (EditText) findViewById(R.id.makeSentence);


    }

    public void ClearText(){

        wordEdit.getText().clear();
        meaningEdit.getText().clear();
        pronunC.getText().clear();
        meaning2.getText().clear();
        synonymsA.getText().clear();
        antonymsA.getText().clear();
        makeSentence.getText().clear();

    }
}
