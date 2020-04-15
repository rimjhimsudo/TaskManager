package com.example.taskmanager.Model;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.taskmanager.Utils.DateConverter;
import com.example.taskmanager.Utils.TimestampConverter;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
//article followed : ?


/*SQLite does not have a storage class set aside for storing dates and/or times.
 Instead, the built-in Date And Time Functions of SQLite are capable of storing
 dates and times as TEXT, REAL, or INTEGER values*/
@Entity(tableName = "Task")
public class Task implements Serializable
{
    @PrimaryKey(autoGenerate = true)
    int id;

    public Task(String title, String description, String category) {
        this.title = title;
        this.description = description;
        this.category=category;
    }

    String title;
    String description;
    public  String category;
    //String priority;
   // @ColumnInfo(name = "creationDate")
   // @TypeConverters({DateConverter.class})
    //Date creationDate;
    /*
    @ColumnInfo(name = "time")
    @TypeConverters({DateConverter.class})
    private Time time;
    */

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        category = category;
    }
    /**
    public void setPriority(String priority) {
        this.priority = priority;
    }


    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
     */

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }
/*
    public String getPriority() {
        return priority;
    }



    public Date getCreationDate() {
        return creationDate;
    }

    */


    @Ignore
    public Task(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        //this.priority = priority;
       // this.creationDate = creationDate;
    }









}
