package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.taskmanager.Constants.Constants;
import com.example.taskmanager.Database.AppExecutors;
import com.example.taskmanager.Database.TaskDatabase;
import com.example.taskmanager.Model.Task;

import java.util.Calendar;

public class AddTask extends AppCompatActivity {
    EditText title, description, date, time;
    Spinner spinnerCategory, spinnerPriority;
    Button savetask;
    DatePickerDialog datePickerDialog;
    Intent intent;
    int tTaskId;
    private TaskDatabase tDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        date=findViewById(R.id.edit_date);
        time=findViewById(R.id.edit_time);
        //doubt
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //find view by id
        initViews();
        //Database work
        tDb=TaskDatabase.getInstance(getApplicationContext());
        intent=getIntent(); //shows that it will return something
        if(intent!=null && intent.hasExtra(Constants.UPDATE_Task_Id)){
            savetask.setText("update");
            tTaskId=intent.getIntExtra(Constants.UPDATE_Task_Id,-1);
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    Task task=tDb.taskDao().loadTaskByID(tTaskId);
                    populateUI(task);
                }
            });
        }


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

    private void populateUI(Task task) {
        if (task==null){
            return;
        }
        title.setText(task.getTitle());
        description.setText(task.getDescription());

    }

    private void initViews() {
        title=findViewById(R.id.edit_title);
        description=findViewById(R.id.edit_task);
        //date=findViewById(R.id.edit_date);
        //time=findViewById(R.id.edit_time);
        savetask=findViewById(R.id.button);
        //spinnerCategory=findViewById(R.id.spinner1);
        //spinnerPriority=findViewById(R.id.spinner2);
        savetask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //use defined method
                onSavebuttonclicked();
            }
        });
    }

    private void onSavebuttonclicked() {
        final Task tasksave=new Task(title.getText().toString(),
                description.getText().toString());
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if(!intent.hasExtra(Constants.UPDATE_Task_Id)){
                    tDb.taskDao().insertTask(tasksave);
                }
                else{
                    tasksave.setId(tTaskId);
                    tDb.taskDao().updateTask(tasksave);
                }
                finish();
            }
        });

    }
}
