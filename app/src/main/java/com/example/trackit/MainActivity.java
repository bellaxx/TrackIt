package com.example.trackit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Button functionality
        Button btn_recordActivity = findViewById(R.id.btn_recordActivity);
        btn_recordActivity.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                startActivity(new Intent(getApplicationContext(), RecordActivity.class));
            }
        });

        Button btn_seeResults = findViewById(R.id.btn_seeResults);
        btn_seeResults.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                startActivity(new Intent(getApplicationContext(), SeeResults.class));
            }
        });
    }
}
