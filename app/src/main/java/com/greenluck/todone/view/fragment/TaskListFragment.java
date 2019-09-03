package com.greenluck.todone.view.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.greenluck.todone.R;
import com.greenluck.todone.adapter.AdapterTask;
import com.greenluck.todone.model.List;
import com.greenluck.todone.model.Task;
import com.greenluck.todone.util.ListUtil;
import com.greenluck.todone.util.TimeUtil;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.Calendar;

public class TaskListFragment extends Fragment {

    private LinearLayout mListInfoLayout;
    private ImageButton mListColorImageButton;
    private TextView mListNameTextView;
    private TextView mTaskCountTextView;
    private ProgressBar mTaskProgressBar;
    private RecyclerView mTaskRecyclerView;
    private Toolbar mTaskToolbar;
    private AdapterTask mTaskAdapter;
    private LinearLayout mNewTaskLinearLayout;
    private CardView mAddTaskCardview;
    private EditText mTaskNameEdittext;
    private ImageButton mAddTaskButton;
    private Button mSetReminderButton;
    private long settedTime;
    private List mList;

    private OnNavigationButtonClickListener mOnNavigationButtonClickListener;


    public interface OnNavigationButtonClickListener{
        void onNavigationPressed();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnNavigationButtonClickListener = (OnNavigationButtonClickListener) context;
        }catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_task_list,container,false);

        //Get clicked list.
        //Todo : Only mTasks passed by referance when list calling from bundle object. Have to send new list to main activity then save to database.
        mList = getArguments().getParcelable("list");

        //Todo: Remove debug logs
        java.util.List<Task> mTasks = mList.getTasks();
        for (Task task : mTasks){
            Log.i("LIST_FROM_TASKLIST", "Content = " + task.getContent() + "Status = " + String.valueOf(task.getStatus()));
        }

        mListInfoLayout = (LinearLayout) v.findViewById(R.id.list_info_linear_layout);
        mTaskProgressBar = (ProgressBar) v.findViewById(R.id.tasklist_task_progressbar);
        mListColorImageButton = (ImageButton) v.findViewById(R.id.tasklist_color_image_button);
        mListNameTextView = (TextView) v.findViewById(R.id.tasklist_list_name);
        mAddTaskCardview = (CardView) v.findViewById(R.id.add_task_cardview);
        mNewTaskLinearLayout = (LinearLayout) v.findViewById(R.id.new_task_linearlayout);
        mTaskToolbar = (Toolbar) v.findViewById(R.id.task_list_toolbar);
        mTaskCountTextView = (TextView) v.findViewById(R.id.tasklist_task_count_textview);
        mTaskNameEdittext = (EditText) v.findViewById(R.id.task_name_edittext);
        mAddTaskButton = (ImageButton) v.findViewById(R.id.add_task_image_button);
        mSetReminderButton = (Button) v.findViewById(R.id.set_reminder_button);

        //Set navigation button
        mTaskToolbar.setNavigationIcon(R.drawable.back_icon);
        mTaskToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mOnNavigationButtonClickListener.onNavigationPressed();
            }
        });

        //Set progressbar.
        mTaskProgressBar.setMax(mList.getTaskCount());
        mTaskProgressBar.setProgress(mList.getComplatedTaskCount());

        //Show list info (Color,Task count,Complated tasks)
        switch (mList.getColor()){
            case R.color.grade_gray:
                mTaskProgressBar.getProgressDrawable().setColorFilter(getContext().getResources().getColor(R.color.grade_gray), PorterDuff.Mode.SRC_IN);
                mListColorImageButton.setBackground(getContext().getDrawable(R.drawable.gradient_grade_grey));
                break;
            case R.color.deep_orange:
                mTaskProgressBar.getProgressDrawable().setColorFilter(getContext().getResources().getColor(R.color.deep_orange), PorterDuff.Mode.SRC_IN);
                mListColorImageButton.setBackground(getContext().getDrawable(R.drawable.gradient_deep_orange));
                break;
            case R.color.evening_sunshine:
                mTaskProgressBar.getProgressDrawable().setColorFilter(getContext().getResources().getColor(R.color.evening_sunshine), PorterDuff.Mode.SRC_IN);
                mListColorImageButton.setBackground(getContext().getDrawable(R.drawable.gradient_evening_sunshine));
                break;
            case R.color.jade:
                mTaskProgressBar.getProgressDrawable().setColorFilter(getContext().getResources().getColor(R.color.jade), PorterDuff.Mode.SRC_IN);
                mListColorImageButton.setBackground(getContext().getDrawable(R.drawable.gradient_jade));
                break;
            case R.color.pinky_pink:
                mTaskProgressBar.getProgressDrawable().setColorFilter(getContext().getResources().getColor(R.color.pinky_pink), PorterDuff.Mode.SRC_IN);
                mListColorImageButton.setBackground(getContext().getDrawable(R.drawable.gradient_pinky_pink));
                break;
        }
        mListNameTextView.setText(mList.getName());
        if (mList.getTaskCount() <= 1){            String taskCount = getString(R.string.task_count_equal_smalllerthan1,mList.getTaskCount());
            mTaskCountTextView.setText(taskCount);
        }else {
            String taskCount = getString(R.string.task_count_biggerthan1,mList.getTaskCount());
            mTaskCountTextView.setText(taskCount);
        }

        //Add new task when user pressed enter on keyboard.
        mTaskNameEdittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_NEXT || i == EditorInfo.IME_ACTION_DONE && !mTaskNameEdittext.getText().equals("")){
                    Task task = new Task(mTaskNameEdittext.getText().toString(),mList.getId(),settedTime,0,mList.getTaskCount());
                    addNewTask(task);
                    return true;
                }
                return false;
            }
        });

        //Add new task when user click add task button.
        mAddTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mTaskNameEdittext.getText().toString().equals("")) {
                    Task task = new Task(mTaskNameEdittext.getText().toString(), mList.getId(), settedTime, 0, mList.getTaskCount());
                    addNewTask(task);
                }
            }
        });

        // Show date picker
        mSetReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long currentTime = System.currentTimeMillis();
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(i,i1,i2);
                        settedTime = calendar.getTimeInMillis();
                        mSetReminderButton.setText(TimeUtil.getReadableTime(settedTime));
                    }
                }, TimeUtil.getYear(currentTime),TimeUtil.getMonth(currentTime),TimeUtil.getDay(currentTime));

                datePickerDialog.show();
            }
        });

        // When user pressed bottom linear layout
        mNewTaskLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNewTaskLinearLayout.setVisibility(View.GONE);
                mListInfoLayout.setVisibility(View.GONE);
                mAddTaskCardview.setVisibility(View.VISIBLE);
                showSoftKeyboard(mTaskNameEdittext);
            }
        });




        //Keyboard listener
        KeyboardVisibilityEvent.setEventListener(
                getActivity(),
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if (!isOpen){
                            mAddTaskCardview.setVisibility(View.GONE);
                            mNewTaskLinearLayout.setVisibility(View.VISIBLE);
                            mListInfoLayout.setVisibility(View.VISIBLE);
                            settedTime = 0;
                        }else{
                            mSetReminderButton.setText("Reminder");
                            mTaskNameEdittext.setText("");
                        }
                    }
                });


        //Set recyclerview and adapter.
        mTaskAdapter = new AdapterTask(mList.getTasks(), getContext(), new AdapterTask.CheckListener() {
            @Override
            public void onCheck(boolean isChecked) {
                if (isChecked){
                    mTaskProgressBar.setProgress(mTaskProgressBar.getProgress() + 1);
                }else{
                    mTaskProgressBar.setProgress(mTaskProgressBar.getProgress() - 1);
                }
            }
        });
        mTaskRecyclerView = v.findViewById(R.id.task_list_recyclerview);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mTaskRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getContext().getDrawable(R.drawable.divider));
        mTaskRecyclerView.addItemDecoration(dividerItemDecoration);
        mTaskRecyclerView.setAdapter(mTaskAdapter);
        mTaskRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));

        return v;
    }

    //Show keyboard
    private void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);

        }
    }

    private void addNewTask(Task task){
        mList.addTask(task);
        mTaskAdapter.notifyItemInserted(mList.getTaskCount() - 1);
        mTaskRecyclerView.scrollToPosition(mList.getTaskCount() - 1);
        if (mList.getTaskCount() <= 1){
            mTaskCountTextView.setText(getString(R.string.task_count_equal_smalllerthan1,mList.getTaskCount()));
        }else {
            mTaskCountTextView.setText(getString(R.string.task_count_biggerthan1,mList.getTaskCount()));
        }
        mTaskNameEdittext.setText("");
        mSetReminderButton.setText("Reminder");
        settedTime = 0;
        //Update progressbar
        mTaskProgressBar.setMax(mList.getTaskCount());
    }


}
