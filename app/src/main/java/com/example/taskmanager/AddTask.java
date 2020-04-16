package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.taskmanager.Constants.Constants;
import com.example.taskmanager.Database.AppExecutors;
import com.example.taskmanager.Database.TaskDatabase;
import com.example.taskmanager.Model.Task;

import java.util.Calendar;

public class AddTask extends AppCompatActivity {
    EditText title, description, date, time;

    Spinner spinnerCategory, spinnerPriority;
    String[] categories;
    String item;
    Button cancelalarm , savetask ,addalarm;
    DatePickerDialog datePickerDialog;
    Intent intent;
    int tTaskId;
    private TaskDatabase tDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
         date=findViewById(R.id.edit_date);
        time = findViewById(R.id.edit_time);
        cancelalarm=findViewById(R.id.cancel_button);
        addalarm=findViewById(R.id.addalarm_button);
        //doubt

        /*getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);*/ //ctrl shft + backslash
        //find view by id
        initViews();
        //spinner functionaility
        categories = getResources().getStringArray(R.array.category_arrays);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                item = categories[position];
                 Log.d("TAGCATEGORY","value of item"+item);
                 }
                /*AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        Task task = tDb.taskDao().loadTaskByCategory(item);
                    }
                });*/

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Database work
        tDb = TaskDatabase.getInstance(getApplicationContext());
        intent = getIntent(); //shows that it will return something
        if (intent != null && intent.hasExtra(Constants.UPDATE_Task_Id)) {
            savetask.setText("update");
            tTaskId = intent.getIntExtra(Constants.UPDATE_Task_Id, -1);
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    Task task = tDb.taskDao().loadTaskByID(tTaskId);
                    populateUI(task);
                }
            });
        }

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                //date picker dialog
                datePickerDialog = new DatePickerDialog(AddTask.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        addalarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(AddTask.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
                       // time.setText(hourOfDay + ":" + minutes);
                        Calendar calendar=Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minutes);
                        calendar.set(Calendar.SECOND,0);
                        time.setText(hourOfDay + ":" + minutes);
                        startAlarm(calendar); //alarm method
                    }
                }, 0, 0, true);
                timePickerDialog.show();
            }
        });

        cancelalarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm(); //define method
            }
        });
    }
    public  void  cancelAlarm(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.cancel(pendingIntent);
        Toast.makeText(getApplicationContext(),"Alarm canceled",Toast.LENGTH_SHORT).show();
    }
    public void startAlarm(Calendar c)
    {
        AlarmManager alarmManager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }

        assert alarmManager != null;
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    private void populateUI(Task task) {
        if (task == null) {
            return;
        }
        title.setText(task.getTitle());
        description.setText(task.getDescription());
        //spinnerCategory.set

    }

    private void initViews() {
        title = findViewById(R.id.edit_title);
        description = findViewById(R.id.edit_task);
        //date=findViewById(R.id.edit_date);
        //time=findViewById(R.id.edit_time);
        savetask = findViewById(R.id.button);
        spinnerCategory = (Spinner) findViewById(R.id.spinner1);

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
        final Task tasksave = new Task(title.getText().toString(),
                description.getText().toString(),item);

        final  Intent resultintent = new Intent();
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final  int val;
                if (!intent.hasExtra(Constants.UPDATE_Task_Id)) {
                    val = tDb.taskDao().insertTask(tasksave).intValue();
                    resultintent.putExtra("result", val);
                    Log.d("tag", "write functions" + val);
                    setResult(RESULT_OK, resultintent);
                } else {
                    tasksave.setId(tTaskId);
                    tDb.taskDao().updateTask(tasksave);
                    val = tTaskId;
                    resultintent.putExtra("result", val);
                    Log.d("tag", "update function" + val);
                    setResult(2, resultintent);
                }


                finish();//finishing activity

            }
        });

    }
}
