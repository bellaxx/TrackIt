package com.example.trackit;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;

public class SeeResults extends AppCompatActivity {
    String filename = "trackItData";
    int year;
    int month;
    int day;
    String date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_results);

        // save current date
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
        date = year + "-" + month + "-" + day;

        // Button to select the date
        Button btn_selectDate = findViewById(R.id.btn_selectDate);
        btn_selectDate.setText(day + "." + month + "." + year);
        btn_selectDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showDialog(999);
            }
        });

        // display the data for today
        showData();
    }

    // displays the data for the selected date
    public void showData(){
        // reset text views that show data
        TextView tv_time = findViewById(R.id.tv_time);
        tv_time.setText("0 h");
        TextView tv_distance = findViewById(R.id.tv_distance);
        tv_distance.setText("0 km");

        // Read from file
        FileInputStream fis = null;
        try {
            fis = getApplicationContext().openFileInput(filename);

            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            try(BufferedReader reader = new BufferedReader(isr)){
                String line = reader.readLine();
                while(line != null){

                    // if this line holds the data for the selected day
                    if(line.contains(date)) {

                        // divide line at every comma
                        String[] values = line.split(",");
                        // 0: date, 1: hours, 2: minutes, 3: km, 4: m
                        tv_time.setText(values[1] + ":" + values[2] + " h");
                        tv_distance.setText(values[3] + "." + values[4] + " km");
                    }

                    // move on to next entry
                    line = reader.readLine();
                }
            } catch(IOException e){
                System.out.println("Error occured when reading file");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error opening file or file does not exist");
        }

    }

    // open a dialog to select a date
    @Override
    protected Dialog onCreateDialog(int id){
        if(id == 999){
            return new DatePickerDialog(this, myDateListener, year, month-1, day);
        }
        return null;
    }

    // if a date was selected
    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int y, int m, int d) {
            // save it
            year = y;
            month = m+1;
            day = d;
            date = year + "-" + month + "-" + day;

            // write it on the button
            Button btn_selectDate = findViewById(R.id.btn_selectDate);
            btn_selectDate.setText(day + "." + month + "." + year);

            // display the corresponding data
            showData();
        }
    };
}
