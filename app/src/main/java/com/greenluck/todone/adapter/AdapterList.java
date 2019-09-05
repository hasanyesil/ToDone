package com.greenluck.todone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.greenluck.todone.R;
import com.greenluck.todone.model.TaskList;
import com.greenluck.todone.view.fragment.MainFragment;

import java.util.List;


public class AdapterList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private MainFragment.OnListClickListener mListClickListener;
    private List<TaskList> mLists;
    public Context mContext;

    public AdapterList(MainFragment.OnListClickListener clickListener, List<TaskList> lists, Context context){
        mListClickListener = clickListener;
        mLists = lists;
        mContext = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);

            return new ListHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ((ListHolder) holder).bindList(mLists.get(position),mContext);

        holder.itemView.setOnClickListener(null);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskList list = mLists.get(position);
                mListClickListener.showTasks(list);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    public class ListHolder extends RecyclerView.ViewHolder{

        public TextView listNameTextView;
        public ImageButton listColorImageButton;

        public ListHolder(@NonNull View itemView) {
            super(itemView);
            listNameTextView = itemView.findViewById(R.id.list_name_textview);
            listColorImageButton = itemView.findViewById(R.id.list_color_imagebutton);
        }

        public void bindList(TaskList list, Context context){
            listNameTextView.setText(list.getName());
            switch (list.getColor()){
                case R.color.grade_gray:
                    listColorImageButton.setBackground(context.getDrawable(R.drawable.gradient_grade_grey));
                    break;
                case R.color.deep_orange:
                    listColorImageButton.setBackground(context.getDrawable(R.drawable.gradient_deep_orange));
                    break;
                case R.color.evening_sunshine:
                    listColorImageButton.setBackground(context.getDrawable(R.drawable.gradient_evening_sunshine));
                    break;
                case R.color.jade:
                    listColorImageButton.setBackground(context.getDrawable(R.drawable.gradient_jade));
                    break;
                case R.color.pinky_pink:
                    listColorImageButton.setBackground(context.getDrawable(R.drawable.gradient_pinky_pink));
                    break;
            }

        }
    }

}
