package com.example.taskmanager.Adaptors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.example.taskmanager.MainActivity;
import com.example.taskmanager.Model.Task;
import com.example.taskmanager.R;

import java.util.ArrayList;
import java.util.List;

public class TaskAdaptors extends  RecyclerView.Adapter<TaskAdaptors.MyViewHolder>{
    Context mContext;
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
        myViewholder.category.setText(tTaskList.get(position).getCategory());
        Log.d("bind","workkiing");
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
  // public void  updatelistonspinner()
   //updtaelistcategorywise(list){
   // this.ttasklist=list
   // notifydatasetchnged();
   //
   // }
    TaskAdaptors(Context context) {
        mContext = context;
    }



   /* public  void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("MyAdapter", "onActivityResult");
    }*/



    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title, description, category;
        ImageView editImage;

        TaskDatabase tDb;


        MyViewHolder(@NonNull final View itemView){
            super(itemView);
            //tDb=TaskDatabase.getInstance(context);
            title=itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            category = itemView.findViewById(R.id.category);
            //TaskAdaptors  a = TaskAdaptors.this;
            //M  yViewHolder b=this;
            editImage = itemView.findViewById(R.id.edit_Image);
            editImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //View.OnClickListener a=  this;
                    int elementId=tTaskList.get(getAdapterPosition()).getId();
                    /*Intent i=new Intent(v.getContext(), AddTask.class);
                    i.putExtra(Constants.UPDATE_Task_Id, elementId);
                    //v.getContext().st(i, 2);
                    //v.getContext().startActivity(i);*/
                    Intent i=new Intent(v.getContext(), AddTask.class);
                    i.putExtra(Constants.UPDATE_Task_Id, elementId);
                    //Intent intent=new Intent(v.getContext(), AddTask.class);
                    //v.getContext().startActivity(i);
                    ( (Activity)v.getContext()).startActivityForResult(i,2);

                }
            });

        }
    }

}
