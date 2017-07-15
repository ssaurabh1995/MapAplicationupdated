package com.example.saurabh.mapaplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private EditText place;
    private Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i=getIntent();
        place= (EditText) findViewById(R.id.edit);
        add= (Button) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gp=place.getText().toString();
                Intent intent=new Intent(MainActivity.this,MapsActivity.class);
                intent.putExtra("gp",gp);
                startActivity(intent);
            }
        });


    }
}
