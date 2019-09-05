package com.greenluck.todone.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;

import com.greenluck.todone.R;
import com.greenluck.todone.data.database.DatabaseHelper;
import com.greenluck.todone.model.Task;
import com.greenluck.todone.model.TaskList;
import com.greenluck.todone.view.fragment.AddListDialogFragment;
import com.greenluck.todone.view.fragment.MainFragment;
import com.greenluck.todone.view.fragment.TaskDetailFragment;
import com.greenluck.todone.view.fragment.TaskListFragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements MainFragment.OnListClickListener, AddListDialogFragment.OnAddListDialogResultListener, TaskListFragment.OnNavigationButtonClickListener, TaskListFragment.TaskClickListener, TaskDetailFragment.OnDetailFragmentNavigation, TaskDetailFragment.OnDeleteTask{

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
                .replace(R.id.fragment_container,mTaskFragment)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void getCreatedList(TaskList list) {
        if (mMainFragment != null){
            mLists.add(list);
            ((MainFragment) mMainFragment).updateAdapter();
            mDatabaseHelper.addList(list);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        /*boolean isUpdated = false;

        //Compare old list and new list.
        for (TaskList list : mLists){
            for (TaskList oldList : mOldLists){

                //If new list did not created, only updated.
                if (list.getId().equals(oldList.getId())) {
                    mDatabaseHelper.updateList(list);
                    isUpdated = true;
                    break;
                }
            }

            // If new list created and need to add database.
            if (!isUpdated){
                mDatabaseHelper.addList(list);
                Log.i("ListFromMainActivity", "onStop: Task count => " + list.getTaskCount());
                java.util.List<Task> tasks = list.getTasks();
                for (Task task : tasks) {
                    Log.i("LIST_FROM_STOP", "onStop: Task under list => " + task.getContent() + " status : " + task.getStatus());
                }
            }

            isUpdated = false;
        }
        super.onStop();*/
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
    public void onBackPressed() {
        if (mTaskFragment.isVisible()){

        }
        super.onBackPressed();
    }
}
