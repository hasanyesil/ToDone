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
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        holder.bindView(mTasks.get(position),mContext);
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
            mTaskCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b){
                        mTaskCheckBox.setTextColor(mContext.getResources().getColor(R.color.hintcolor));
                        mTaskCheckBox.setPaintFlags(mTaskCheckBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        mTasks.get(getAdapterPosition()).setStatus(1);
                        mCheckListener.onCheck(true);
                    }else {
                        mTaskCheckBox.setTextColor(mContext.getResources().getColor(R.color.black87));
                        mTaskCheckBox.setPaintFlags(mTaskCheckBox.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
                        mTasks.get(getAdapterPosition()).setStatus(0);
                        mCheckListener.onCheck(false);
                    }
                }
            });
        }

        public void bindView(Task task,Context context){
            mTaskCheckBox.setText(task.getContent());
            if (task.getDueTime() == 0){
                mTaskDueTimeTextView.setVisibility(View.GONE);
            }else{
                mTaskDueTimeTextView.setText(TimeUtil.getReadableTime(task.getDueTime()));
            }
            if (task.getStatus() == 1){
                mTaskCheckBox.setChecked(true);
                mTaskCheckBox.setTextColor(context.getResources().getColor(R.color.hintcolor));
                mTaskCheckBox.setPaintFlags(mTaskCheckBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }else{
                mTaskCheckBox.setChecked(false);
                mTaskCheckBox.setTextColor(context.getResources().getColor(R.color.black87));
            }
        }
    }
}
