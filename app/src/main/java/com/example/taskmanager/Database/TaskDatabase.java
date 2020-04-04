package com.example.taskmanager.Database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.taskmanager.Model.Task;

@Database(entities={Task.class},version=1, exportSchema = false)
public abstract class TaskDatabase extends RoomDatabase {
    private  static final  String LOG_TAG=TaskDatabase.class.getSimpleName();
    private  static final String DATABASE_NAME="tasklist";
    private  static final Object LOCK= new Object();
    private  static  TaskDatabase taskinstance;
    public  static  TaskDatabase getInstance(Context context){
        if(taskinstance==null){
            synchronized (LOCK){
                Log.d(LOG_TAG, "creating databse");
                taskinstance= Room.databaseBuilder(context.getApplicationContext(),
                TaskDatabase.class,TaskDatabase.DATABASE_NAME).build();
            }
        }
        Log.d(LOG_TAG, "starting with database");
        return taskinstance;
    }
    public abstract TaskDao taskDao();
}
