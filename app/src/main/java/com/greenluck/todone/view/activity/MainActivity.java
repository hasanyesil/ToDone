package com.greenluck.todone.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;

import com.greenluck.todone.R;
import com.greenluck.todone.data.database.DatabaseHelper;
import com.greenluck.todone.model.List;
import com.greenluck.todone.model.Task;
import com.greenluck.todone.view.fragment.AddListDialogFragment;
import com.greenluck.todone.view.fragment.MainFragment;
import com.greenluck.todone.view.fragment.TaskListFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainFragment.OnListClickListener, AddListDialogFragment.OnAddListDialogResultListener, TaskListFragment.OnNavigationButtonClickListener {

    Fragment mMainFragment;
    Fragment mTaskFragment;
    private DatabaseHelper mDatabaseHelper;
    private ArrayList<List> mLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabaseHelper = DatabaseHelper.getInstance(this);
        mLists = mDatabaseHelper.getLists();

        mMainFragment = getSupportFragmentManager().findFragmentByTag("ListFragment");
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
    public void showTasks(List list) {
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
    public void getCreatedList(List list) {
        if (mMainFragment != null){
            mLists.add(list);
            ((MainFragment) mMainFragment).updateAdapter();
        }
    }

    @Override
    protected void onStop() {
        for (List list : mLists){
            mDatabaseHelper.addList(list);
            Log.i("ListFromMainActivity", "onStop: Task count => " + list.getTaskCount());
            for (Task task : list.getTasks()) {
                Log.i("ListFromMainActivity", "onStop: List name => " + list.getName());
                Log.i("ListFromMainActivity", "onStop: Task under list => " + task.getContent() + " status : " + task.getStatus());
            }
        }
        super.onStop();
    }

    @Override
    public void onNavigationPressed() {
        if (mTaskFragment != null){
            getSupportFragmentManager().popBackStack();
        }
    }
}
