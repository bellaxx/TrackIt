package com.example.trackit;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;

public class RecordActivity extends AppCompatActivity {
    String filename = "trackItData";
    int year;
    int month;
    int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_activity);

        // save current date
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);

        // date button
        Button btn_selectDate = findViewById(R.id.btn_selectDate);
        btn_selectDate.setText(day + "." + month + "." + year);
        btn_selectDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showDialog(999);
            }
        });


        // show values for the seekbars
        SeekBar seekbarHours = findViewById(R.id.seekbarHours);
        seekbarHours.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView hoursProgress = findViewById(R.id.hoursProgress);
                hoursProgress.setText(progress + "h");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        SeekBar seekbarMinutes = findViewById(R.id.seekbarMinutes);
        seekbarMinutes.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // set stepsize
                progress = ((int)Math.round(progress/10))*10;
                seekBar.setProgress(progress);
                TextView minProgress = findViewById(R.id.minutesProgress);
                minProgress.setText(progress + " min");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        SeekBar seekbarKm = findViewById(R.id.seekbarKm);
        seekbarKm.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // set stepsize
                TextView kmProgress = findViewById(R.id.kmProgress);
                kmProgress.setText(progress + " km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        SeekBar seekbarMeters = findViewById(R.id.seekbarMeters);
        seekbarMeters.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // set stepsize
                progress = ((int)Math.round(progress/100))*100;
                seekBar.setProgress(progress);
                TextView metersProgress = findViewById(R.id.metersProgress);
                metersProgress.setText(progress + " m");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // save button
        Button btn_save = findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // get data that was set by user

                // Time
                SeekBar seekbarHours = findViewById(R.id.seekbarHours);
                int hours = seekbarHours.getProgress();
                SeekBar seekbarMinutes = findViewById(R.id.seekbarMinutes);
                int minutes = seekbarMinutes.getProgress();

                // distance
                SeekBar seekbarKm = findViewById(R.id.seekbarKm);
                int km = seekbarKm.getProgress();
                SeekBar seekbarMeters = findViewById(R.id.seekbarMeters);
                int m = seekbarMeters.getProgress();


                // save data to the file
                String todaysEntry = "";
                File file = new File(getApplicationContext().getFilesDir(), filename);
                if (!file.exists()) {
                    // write into a new file
                    try (FileOutputStream fos = getApplicationContext().openFileOutput(filename, Context.MODE_PRIVATE)) {
                        todaysEntry = year + "-" + month + "-" + day + ", " + hours + ", " + minutes + ", " + km + ", " + m;
                        fos.write(todaysEntry.getBytes());
                    } catch (Exception e) {
                        System.out.println("Error when writing into the file");
                    }
                } else {
                    // append text to an existing file
                    try (FileOutputStream fos = getApplicationContext().openFileOutput(filename, Context.MODE_APPEND)) {
                        // file structure: date, recordedTime, recordedDistance
                        // Bsp: 2019-12-18, 2, 15, 15, 0
                        todaysEntry = "\n" + year + "-" + month + "-" + day + ", " + hours + ", " + minutes + ", " + km + ", " + m;
                        fos.write(todaysEntry.getBytes());
                    } catch (Exception e) {
                        System.out.println("Error when writing into the file");
                    }
                }
                System.out.println("Saved to file: " + todaysEntry);
                Toast.makeText(getApplicationContext(), "Successfully saved data", Toast.LENGTH_LONG).show();
                printFileContentOnConsole();
            }
        });


    }

    // shows a dialog to select a date
    @Override
    protected Dialog onCreateDialog(int id){
        if(id == 999){
            return new DatePickerDialog(this, myDateListener, year, month-1, day);
        }
        return null;
    }

    // when a date is selected, write it on the date button and save the date
    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int y, int m, int d) {
            year = y;
            month = m+1;
            day = d;
            Button btn_selectDate = findViewById(R.id.btn_selectDate);
            btn_selectDate.setText(day + "." + month + "." + year);
        }
    };

    public void printFileContentOnConsole(){
        // Read from file
        FileInputStream fis = null;
        try {
            fis = getApplicationContext().openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(isr);
                String line = reader.readLine();
                while(line != null){
                    System.out.println(line);
                    // move on to next entry
                    line = reader.readLine();
                }
        } catch (FileNotFoundException e) {
            System.out.println("File does not exist.");
        } catch(IOException e){
            System.out.println("Error occured when reading file");
        }
    }

}
