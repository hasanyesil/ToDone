package com.greenluck.todone.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.greenluck.todone.R;
import com.greenluck.todone.model.Task;

public class TaskDetailFragment extends Fragment {

    private EditText mTaskContentEdittext;
    private Toolbar mTaskDetailToolbar;
    private OnDetailFragmentNavigation mNavigationClickListener;
    private OnDeleteTask mOnDeleteTaskListener;

    private Task task;

    public interface OnDetailFragmentNavigation{
        void onDetailNavigationClick();
    }

    public interface OnDeleteTask{
        void deleteTask(Task task);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mNavigationClickListener = (OnDetailFragmentNavigation) context;
            mOnDeleteTaskListener = (OnDeleteTask) context;
        }catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_task_detail,container,false);
        task = getArguments().getParcelable("Task");
        //setHasOptionsMenu(true);
        String listName = getArguments().getString("Listname");
        mTaskContentEdittext = (EditText) v.findViewById(R.id.task_content_edt);
        mTaskDetailToolbar = (Toolbar) v.findViewById(R.id.task_detail_toolbar);
        mTaskDetailToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNavigationClickListener.onDetailNavigationClick();
            }
        });

        mTaskDetailToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.delete_button) {
                    mOnDeleteTaskListener.deleteTask(task);
                    return true;
                }
                return false;
            }
        });

        mTaskDetailToolbar.inflateMenu(R.menu.menu_task_detail);
        mTaskDetailToolbar.setTitle(listName);
        mTaskContentEdittext.setText(task.getContent());
        return v;
    }
}
