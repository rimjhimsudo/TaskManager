package com.example.taskmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.ArrayAdapter;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Spinner;
import android.widget.Toast;

import com.example.taskmanager.Adaptors.TaskAdaptors;
import com.example.taskmanager.Database.AppExecutors;
import com.example.taskmanager.Database.TaskDatabase;
import com.example.taskmanager.Model.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private TaskAdaptors taskAdaptors;
    private TaskDatabase taskDatabase;
    private int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //int position = viewHolder.getAdapterPosition();
        floatingActionButton =findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddTask.class));
            }
        });
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //initialise adapter
        taskAdaptors=new TaskAdaptors(this);
        recyclerView.setAdapter(taskAdaptors);
        taskDatabase=TaskDatabase.getInstance(getApplicationContext());

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT| ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                         position = viewHolder.getAdapterPosition();
                        final List<Task> tasks = taskAdaptors.getTasks();

                        taskDatabase.taskDao().deleteTask(tasks.get(position));
                        //notify itemremove noify to adaptor
                        //new code

                        View view=findViewById(R.id.mainRelativeLayout);
                        Snackbar snackbar=Snackbar.make(view,"1 task deleted", Snackbar.LENGTH_LONG);
                        //delayfunction on thread
                        //snackbar gets stopped when
                        snackbar.setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                tasks.add(position, tasks.get(position));
                                taskAdaptors.notifyItemInserted(position);

                                Log.d("change made?","listener worked");

                            }
                        });
                        snackbar.show();
                        retrieveTasks();
                    }
                });
            }
        }).attachToRecyclerView(recyclerView);
}

   /* private void showUndoSnackbar() {

    }

    private void undoDelete() {
        taskAdaptors.getTasks()
    }

    */

    @Override
    public  boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        MenuItem menuItem=menu.findItem(R.id.spinner);

        //Spinner spinner=findViewById(R.id.spinner);
        Spinner spinner= (Spinner) MenuItemCompat.getActionView(menuItem);//deprecated

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_sort_arrays, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Toast.makeText(this, "Item 1 selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item2:
                Toast.makeText(this, "Item 2 selected", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void  onResume(){
        super.onResume();
        retrieveTasks();
    }

    private void retrieveTasks() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<Task> tasks=taskDatabase.taskDao().loadAllTasks();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        taskAdaptors.setTasks(tasks);
                    }
                });
            }
        });
    }
}
