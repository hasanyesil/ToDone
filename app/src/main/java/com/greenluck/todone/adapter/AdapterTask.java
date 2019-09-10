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
import com.greenluck.todone.interfaces.ItemTouchHelperViewHolder;
import com.greenluck.todone.model.Task;
import com.greenluck.todone.util.TimeUtil;
import com.greenluck.todone.view.fragment.TaskListFragment;

import java.util.List;

public class AdapterTask extends RecyclerView.Adapter<AdapterTask.TaskHolder> {

    private List<Task> mTasks;
    private Context mContext;
    private CheckListener mCheckListener;
    private TaskListFragment.TaskClickListener mTaskClickListener;
    private String mListName;

    public interface CheckListener{
        void onCheck(boolean isChecked);
    }


    public AdapterTask(List<Task> tasks, Context context, CheckListener checkListener, TaskListFragment.TaskClickListener taskClickListener, String listName) {
        mTasks = tasks;
        mContext = context;
        mCheckListener = checkListener;
        mTaskClickListener = taskClickListener;
        mListName = listName;
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
        holder.itemView.setOnClickListener(null);
        holder.mTaskContent.setText(task.getContent());


        // Show task details
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() != R.id.task_check_box){
                    mTaskClickListener.onTaskClick(task,mListName);
                }
            }
        });

        //Show task date
        // Todo: show task's end time
        if (task.getDueTime() != 0){
            holder.mTaskDueTimeTextView.setText(TimeUtil.getReadableTime(task.getDueTime()));
        }else{
            holder.mTaskDueTimeTextView.setText(null);
        }

        // Show task status
        if (task.getStatus() == 1){
            holder.mTaskCheckBox.setChecked(true);
            holder.mTaskContent.setPaintFlags(holder.mTaskCheckBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.mTaskContent.setTextColor(mContext.getResources().getColor(R.color.hintcolor));
        }else{
            holder.mTaskCheckBox.setChecked(false);
            holder.mTaskContent.setPaintFlags(holder.mTaskCheckBox.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            holder.mTaskContent.setTextColor(mContext.getResources().getColor(R.color.black87));
        }

        // Add listener to checkbox.
        holder.mTaskCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                // Change task status and update ui.
                if (b){
                    task.setStatus(1);
                    holder.mTaskContent.setPaintFlags(holder.mTaskCheckBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.mTaskContent.setTextColor(mContext.getResources().getColor(R.color.hintcolor));
                    mCheckListener.onCheck(true);
                }else{
                    task.setStatus(0);
                    holder.mTaskContent.setPaintFlags(holder.mTaskCheckBox.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG) );
                    holder.mTaskContent.setTextColor(mContext.getResources().getColor(R.color.black87));
                    mCheckListener.onCheck(false);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    public class TaskHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

        private CheckBox mTaskCheckBox;
        private TextView mTaskDueTimeTextView;
        private TextView mTaskContent;
        private View mDivider;


        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            mTaskContent = (TextView) itemView.findViewById(R.id.task_content_edt);
            mTaskDueTimeTextView = (TextView) itemView.findViewById(R.id.task_date_textview);
            mTaskCheckBox = (CheckBox) itemView.findViewById(R.id.task_check_box);
            mDivider = (View) itemView.findViewById(R.id.divider);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackground(mContext.getDrawable(R.drawable.viewholder_border));
            itemView.setTranslationZ(7);
            mDivider.setVisibility(View.GONE);
        }

        @Override
        public void onItemClear() {
            mDivider.setVisibility(View.VISIBLE);
            itemView.setBackgroundResource(0);
        }
    }
}
