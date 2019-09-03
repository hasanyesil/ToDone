package com.greenluck.todone.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.greenluck.todone.R;
import com.greenluck.todone.model.Task;
import com.greenluck.todone.util.ListUtil;
import com.greenluck.todone.util.TaskUtil;
import com.greenluck.todone.util.TimeUtil;


import java.util.List;

public class AdapterTask extends RecyclerView.Adapter<AdapterTask.TaskHolder> {

    private List<Task> mTasks;
    private Context mContext;
    private CheckListener mCheckListener;

    public interface CheckListener{
        void onCheck(boolean isChecked);
    }

    public AdapterTask(List<Task> tasks, Context context, CheckListener checkListener) {
        mTasks = tasks;
        mContext = context;
        mCheckListener = checkListener;
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_row,null);
        return new TaskHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final TaskHolder holder, final int position) {

        final Task task = mTasks.get(position);

        holder.mTaskCheckBox.setOnCheckedChangeListener(null);
        holder.mTaskCheckBox.setText(task.getContent());

        if (task.getDueTime() != 0){
            holder.mTaskDueTimeTextView.setText(TimeUtil.getReadableTime(task.getDueTime()));
        }

        if (task.getStatus() == 1){
            holder.mTaskCheckBox.setChecked(true);
            holder.mTaskCheckBox.setPaintFlags(holder.mTaskCheckBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            holder.mTaskCheckBox.setChecked(false);
            holder.mTaskCheckBox.setPaintFlags(holder.mTaskCheckBox.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        }

        holder.mTaskCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    task.setStatus(1);
                    holder.mTaskCheckBox.setPaintFlags(holder.mTaskCheckBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    mCheckListener.onCheck(true);
                }else{
                    task.setStatus(0);
                    holder.mTaskCheckBox.setPaintFlags(holder.mTaskCheckBox.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG) );
                    mCheckListener.onCheck(false);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    public class TaskHolder extends RecyclerView.ViewHolder{

        private CheckBox mTaskCheckBox;
        private TextView mTaskDueTimeTextView;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            mTaskDueTimeTextView = (TextView) itemView.findViewById(R.id.task_time_textview);
            mTaskCheckBox = (CheckBox) itemView.findViewById(R.id.task_check_box);
        }

    }
}
