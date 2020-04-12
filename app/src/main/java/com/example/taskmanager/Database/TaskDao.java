package com.example.taskmanager.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.taskmanager.Model.Task;

import java.util.List;
@Dao
public interface TaskDao {
    @Query("Select * from Task ORDER BY ID")
    List<Task> loadAllTasks();
    @Insert
    Long insertTask(Task task);
    //gve id if there iis taskkk
    @Update
    void updateTask(Task task);
    //change to int

    @Query("delete from Task where id=:id")
    void deleteTask(int id);
    //@Delete
    //void  deleteTask(Task task);
    @Query("Select * from TASK WHERE id=:id")
    Task loadTaskByID(int id);

}






