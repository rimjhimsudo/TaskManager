package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

public class AddTask extends AppCompatActivity {
    EditText title, task, date, time;
    Button savetask;
    DatePickerDialog datePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        title=findViewById(R.id.edit_title);
        task=findViewById(R.id.edit_task);
        date=findViewById(R.id.edit_date);
        time=findViewById(R.id.edit_time);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar=Calendar.getInstance();
                int day=calendar.get(Calendar.DAY_OF_MONTH);
                int month=calendar.get(Calendar.MONTH);
                int year=calendar.get(Calendar.YEAR);
                //date picker dialog
                datePickerDialog=new DatePickerDialog(AddTask.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date.setText(dayOfMonth+"/"+(month +1) +"/"+year);}
                },year,month,day);
                datePickerDialog.show();
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog=new TimePickerDialog(AddTask.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
                        time.setText(hourOfDay + ":" + minutes);
                    }
                    },0,0,false);
                timePickerDialog.show();
            }
        });
    }
}
