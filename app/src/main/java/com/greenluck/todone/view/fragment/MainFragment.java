package com.greenluck.todone.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.greenluck.todone.R;
import com.greenluck.todone.adapter.AdapterList;
import com.greenluck.todone.data.database.DatabaseHelper;
import com.greenluck.todone.model.TaskList;

import java.util.List;

public class MainFragment extends Fragment {

    private RecyclerView mListRecyclerView;
    private AdapterList mListAdapter;
    private ImageButton mAddListButton;
    private OnListClickListener mListClickListener;
    private List<TaskList> mLists;
    private DatabaseHelper mDatabaseHelper;

    public interface OnListClickListener{
        void showTasks(TaskList list);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mListClickListener = (OnListClickListener) context;
        }catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mLists = getArguments().getParcelableArrayList("Lists");

        View v = inflater.inflate(R.layout.fragment_main,container,false);
        mListRecyclerView = v.findViewById(R.id.list_recycler_view);
        mListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mListAdapter = new AdapterList(mListClickListener,mLists,getContext());
        mListRecyclerView.setAdapter(mListAdapter);

        mAddListButton = (ImageButton) v.findViewById(R.id.add_list_image_button);
        mAddListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddListDialogFragment fragment = new AddListDialogFragment();
                fragment.show(getActivity().getSupportFragmentManager(),"Add List");
            }
        });

        return v;
    }


    public void updateAdapter(){
        mListAdapter.notifyItemInserted(mLists.size());
    }

    public List<TaskList> getLists(){
        return mLists;
    }
}
