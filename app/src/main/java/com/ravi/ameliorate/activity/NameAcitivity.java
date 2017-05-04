package com.ravi.ameliorate.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ravi.ameliorate.R;

public class NameAcitivity extends AppCompatActivity {

    EditText enterName;
    ImageView next;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_acitivity);
        final SharedPreferences sp = getSharedPreferences("dictionary",getApplicationContext().MODE_PRIVATE);
        enterName = (EditText) findViewById(R.id.enterName);
        next = (ImageView) findViewById(R.id.next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               name =  enterName.getText().toString();


                if(name.length()==0){

                    Toast.makeText(getApplicationContext(),"Please enter your name",Toast.LENGTH_SHORT).show();

                }else{
                    SharedPreferences.Editor editor = sp
                            .edit();
                    editor.putString("name", name);
                    editor.commit();

                    Intent intent = new Intent(NameAcitivity.this,Dictionary.class);
                    finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }



            }
        });



    }
}
