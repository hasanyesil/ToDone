package com.greenluck.todone.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.greenluck.todone.R;
import com.greenluck.todone.data.database.DatabaseHelper;
import com.greenluck.todone.model.Task;
import com.greenluck.todone.model.TaskList;
import com.greenluck.todone.view.fragment.AddListDialogFragment;
import com.greenluck.todone.view.fragment.DeleteListDialogFragment;
import com.greenluck.todone.view.fragment.MainFragment;
import com.greenluck.todone.view.fragment.TaskDetailFragment;
import com.greenluck.todone.view.fragment.TaskListFragment;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements MainFragment.OnListClickListener, AddListDialogFragment.OnAddListDialogResultListener, TaskListFragment.OnNavigationButtonClickListener, TaskListFragment.TaskClickListener, TaskDetailFragment.OnDetailFragmentNavigation, TaskDetailFragment.OnDeleteTask, DeleteListDialogFragment.OnListDeletedListener {

    Fragment mMainFragment;
    Fragment mTaskFragment;
    Fragment mTaskDetailFragment;
    private DatabaseHelper mDatabaseHelper;
    private ArrayList<TaskList> mLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabaseHelper = DatabaseHelper.getInstance(this);
        mLists = mDatabaseHelper.getLists();

        mMainFragment = getSupportFragmentManager().findFragmentByTag("ListFragment");
        //Control if fragment exist on memory (Rotation)
        if (mMainFragment == null) {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("Lists", mLists);
            mMainFragment = new MainFragment();
            mMainFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, mMainFragment, "ListFragment")
                    .commit();
        }
    }

    @Override
    public void showTasks(TaskList list) {
        mTaskFragment = new TaskListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("list",list);

        mTaskFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,mTaskFragment,"TaskListFragment")
                .addToBackStack(null)
                .commit();

    }

    //Save list to db when list created.
    @Override
    public void getCreatedList(TaskList list) {
        if (mMainFragment != null){
            mLists.add(list);
            ((MainFragment) mMainFragment).updateAdapter();
            mDatabaseHelper.addList(list);
        }
    }

    //When navigation clicked on task list fragment. Return main fragment.
    @Override
    public void onNavigationPressed(TaskList updatedList) {
        if (mTaskFragment != null){
            mDatabaseHelper.updateList(updatedList);
            getSupportFragmentManager().popBackStack();
        }
    }

    //When user pressed task, show task detail fragment.
    @Override
    public void onTaskClick(Task task, String listName) {
        mTaskDetailFragment = getSupportFragmentManager().findFragmentByTag("TaskDetailFragment");
        if (mTaskDetailFragment == null){
            Bundle bundle = new Bundle();
            bundle.putParcelable("Task",task);
            bundle.putString("Listname",listName);
            mTaskDetailFragment = new TaskDetailFragment();
            mTaskDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container,mTaskDetailFragment,"TaskDetailFragment")
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onDetailNavigationClick() {
        if (mTaskDetailFragment != null){
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void deleteTask(Task task) {
        for (TaskList list : mLists){
            if (list.getId().equals(task.getParentListId())){
                list.getTasks().remove(task);
                break;
            }
        }
    }


    @Override
    public void onListDeleted(TaskList list) {
        mDatabaseHelper.deleteList(list);
        mLists.remove(list);

        getSupportFragmentManager().popBackStack();

    }
}
