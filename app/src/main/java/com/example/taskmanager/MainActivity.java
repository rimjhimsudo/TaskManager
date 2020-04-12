package com.example.taskmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SearchView;
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

import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskmanager.Adaptors.TaskAdaptors;
import com.example.taskmanager.Database.AppExecutors;
import com.example.taskmanager.Database.TaskDatabase;
import com.example.taskmanager.Model.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private TaskAdaptors taskAdaptors;
    private TaskDatabase taskDatabase;
    boolean canDeletefromDB=true;
    private ArrayList<Task> taskList = new ArrayList<>();
    int postion;
    //everything should be done through 1 list do avoid database calls
    //avod callng again

/*
    public MainActivity() {
        super();
    }

 */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //int position = viewHolder.getAdapterPosition();
        floatingActionButton =findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //startActivity(new Intent(MainActivity.this, AddTask.class));
                //startActivityForResult(intent, 2)
                Intent intent=new Intent(MainActivity.this,AddTask.class);
                startActivityForResult(intent, 2);
            }
        });
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //initialise adapter
        taskAdaptors=new TaskAdaptors(taskList);
        recyclerView.setAdapter(taskAdaptors);
        taskDatabase=TaskDatabase.getInstance(getApplicationContext());

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT| ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                //final List<Task> tasks=taskAdaptors.getTasks();

                final int position = viewHolder.getAdapterPosition();
                final Task  removedItem=taskList.get(position);
                //final int deletedIndex=viewHolder.getAdapterPosition();
                taskAdaptors.swipeItem(position);
                // tTaskList.add(position,task);

                View view=findViewById(R.id.mainRelativeLayout);
                Snackbar snackbar=Snackbar.make(view,"1 task deleted", Snackbar.LENGTH_SHORT);
                // getitemid() in adaptors
                //delayfunction on thread
                //snackbar gets stopped when
                //snackbar.dismiss();//interface
                snackbar.setAction("UNDO", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        taskAdaptors.restoreItem(removedItem,position);
                        //canDeletefromDB=false;
                        Log.d("change made?","listener worked");

                    }
                }).addCallback(new Snackbar.Callback(){
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT || event == Snackbar.Callback.DISMISS_EVENT_CONSECUTIVE || event ==Snackbar.Callback.DISMISS_EVENT_SWIPE){
                            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    taskDatabase.taskDao().deleteTask(removedItem.getId()); //chnge done
                                    //taskDatabase.taskDao().deleteTask(tasks.get(position));
                                    //List<Task> tasks = taskAdaptors.getTasks();
                                    //final Task  removedItem=tasks.get(position);


                                    // only write db chnges in run thread
                                    //notify itemremove noify to adaptor
                                    //new code
                                    Log.d("DELETE","deletion worked");
                                    //retrieveTasks(); //helping n retreving task and showing task on recyclerview again
                                }
                            });
                        }
                    }
                }).show();
                //snackbar.addCallback(new Snackbar.Callback());

                }


        }).attachToRecyclerView(recyclerView);
        retrieveTasks();
        //taskAdaptors.setTasks(taskList);
        //taskAdaptors.updateData(taskList);


}


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent taskid)
    {
        super.onActivityResult(requestCode, resultCode, taskid);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==2)
        {  updateListOnView();
            // int id=taskid.getIntExtra("id",-1);
           // retrieveTasks();
            //taskAdaptors.notifyDataSetChanged();
            //taskAdaptors.updateData(id);
             //cant do this because it is updatng database again
           // taskAdaptors.setTasks(taskList);


//            AppExecutors.getInstance().diskIO().execute(new Runnable() {
//                @Override
//                public void run() {
//                    Task task=taskDatabase.taskDao().loadTaskByID(getTaskId());
//                    AddTask.populateUI(task);
//                }
//            });
            //taskAdaptors.notifyDataSetChanged();

        }

    }
/*
    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
    }

 */
//onresume was fetching list but not a correct way so now fiigure out other functionality to do it
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
        MenuItem menuItem1=menu.findItem(R.id.searchbar);

        SearchView searchView=(SearchView) menuItem1.getActionView();
        searchView.setOnQueryTextListener(this);
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
/*
    @Override
    protected void  onResume(){
        super.onResume();

        retrieveTasks();
    }

 */

    private void retrieveTasks() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                taskList.addAll(taskDatabase.taskDao().loadAllTasks());
                //taskList.add(taskDatabase.taskDao().loadTaskByID());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        taskAdaptors.notifyDataSetChanged();///read about notify all methods
                        // does update ui
                    }
                });


            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        //filter
        String searchinput=newText.toLowerCase();
        ArrayList<Task> newtaskList = new ArrayList<>();
        int position;
        //taskList.get()
        //String title = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.title)).getText().toString();
        for (Task t :taskList){
            if(t.getTitle().toLowerCase().contains(searchinput)){
                newtaskList.add(t);
            }
        }

        taskAdaptors.updateList(newtaskList);
        return true;
    }

    public void updateListOnView(){
        taskList.clear();
        //tTaskList=new ArrayList<>();
        taskList.addAll(taskList);
        taskAdaptors.notifyDataSetChanged();

    }
}