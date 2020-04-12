package com.example.taskmanager.Adaptors;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.AddTask;
import com.example.taskmanager.Constants.Constants;
import com.example.taskmanager.Database.TaskDatabase;
import com.example.taskmanager.Model.Task;
import com.example.taskmanager.R;

import java.util.ArrayList;
import java.util.List;

public class TaskAdaptors extends  RecyclerView.Adapter<TaskAdaptors.MyViewHolder>{

    private ArrayList<Task> tTaskList;
    public TaskAdaptors(ArrayList<Task> list){
        this.tTaskList=list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.task_item,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdaptors.MyViewHolder myViewholder, int position) {
        myViewholder.title.setText(tTaskList.get(position).getTitle());
        myViewholder.description.setText(tTaskList.get(position).getDescription());
    }

    @Override
    public long getItemId(int position) {
       //return super.getItemId(position);

        return Long.valueOf(tTaskList.get(position).getId());
        //explcit to long from id
    }



    @Override
    public int getItemCount() {
        if(tTaskList==null){
            return 0;
        }
        return tTaskList.size();
    }
    public  void swipeItem(int position){
        tTaskList.remove(position);
        notifyItemRemoved(position);

    }
    public  void restoreItem(Task task, int position){
        tTaskList.add(position,task);
        notifyItemInserted(position);
    }

   /* public  void setTasks(List<Task> taskList){
        tTaskList=taskList;
        notifyDataSetChanged();
    }*/
   /* public void updateData(int id) {
        //tTaskList=taskList;
        //tTaskList.clear();
        Task task=new Task(id);
        //tTaskList.addAll(taskList); //this s adapter method so addall add n arraylst of maiiiiinactiiiviity
        notifyDataSetChanged();
        tTaskList.add(task);
    }

    */
   public void updateList(ArrayList<Task> list){
       tTaskList=new ArrayList<>();
       tTaskList.addAll(list);
       notifyDataSetChanged();

   }




    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title, description;
        ImageView editImage;
        TaskDatabase tDb;


        MyViewHolder(@NonNull final View itemView){
            super(itemView);
            //tDb=TaskDatabase.getInstance(context);
            title=itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);

            editImage = itemView.findViewById(R.id.edit_Image);
            editImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int elementId=tTaskList.get(getAdapterPosition()).getId();
                    Intent i=new Intent(v.getContext(), AddTask.class);
                    i.putExtra(Constants.UPDATE_Task_Id, elementId);
                    v.getContext().startActivity(i);

                }
            });

        }
    }
}
