package com.greenluck.todone.view.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.greenluck.todone.R;
import com.greenluck.todone.adapter.AdapterTask;
import com.greenluck.todone.data.database.DatabaseHelper;
import com.greenluck.todone.interfaces.ItemTouchHelperViewHolder;
import com.greenluck.todone.model.Task;
import com.greenluck.todone.model.TaskList;
import com.greenluck.todone.util.TimeUtil;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class TaskListFragment extends Fragment {

    private TextView mTaskCountTextview;
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

    private TaskList mList;
    private DatabaseHelper mDatabaseHelper;

    private OnNavigationButtonClickListener mOnNavigationButtonClickListener;
    private TaskClickListener mTaskClickListener;


    public interface OnNavigationButtonClickListener{
        void onNavigationPressed(TaskList updatedList);
    }

    public interface TaskClickListener{
        void onTaskClick(Task task, String listName);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnNavigationButtonClickListener = (OnNavigationButtonClickListener) context;
            mTaskClickListener = (TaskClickListener) context;
        }catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_task_list,container,false);

        mDatabaseHelper = DatabaseHelper.getInstance(getContext());

        mList = getArguments().getParcelable("list");

        mList.setTasks(mDatabaseHelper.getTasks(mList.getId()));


        mTaskProgressBar = (ProgressBar) v.findViewById(R.id.tasklist_task_progressbar);
        mAddTaskCardview = (CardView) v.findViewById(R.id.add_task_cardview);
        mNewTaskLinearLayout = (LinearLayout) v.findViewById(R.id.new_task_linearlayout);
        mTaskToolbar = (Toolbar) v.findViewById(R.id.task_list_toolbar);
        mTaskNameEdittext = (EditText) v.findViewById(R.id.task_name_edittext);
        mAddTaskButton = (ImageButton) v.findViewById(R.id.add_task_image_button);
        mSetReminderButton = (Button) v.findViewById(R.id.set_reminder_button);
        mTaskCountTextview = (TextView) v.findViewById(R.id.task_count_textview);
        mTaskRecyclerView = v.findViewById(R.id.task_list_recyclerview);

        mTaskToolbar.inflateMenu(R.menu.menu_task_list);
        mTaskToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.delete_list_item){
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("list",mList);
                    DeleteListDialogFragment fragment = new DeleteListDialogFragment();
                    fragment.setArguments(bundle);
                    fragment.show(getActivity().getSupportFragmentManager(),"delete_list");
                }else if(item.getItemId() == R.id.color_change_item){

                }else if (item.getItemId() == R.id.rename_list_item){

                }
                return false;
            }
        });
        mTaskToolbar.setNavigationIcon(R.drawable.back_icon);
        mTaskToolbar.setTitle(mList.getName());
        mTaskToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mOnNavigationButtonClickListener.onNavigationPressed(mList);
            }
        });


        //Set progressbar and task count tv
        mTaskCountTextview.setText(getString(R.string.task_count,mList.getComplatedTaskCount(),mList.getTaskCount()));
        mTaskProgressBar.setMax(mList.getTaskCount());
        mTaskProgressBar.post(new Runnable() {
            @Override
            public void run() {
                mTaskProgressBar.setProgress(mList.getComplatedTaskCount());
            }
        });

        Log.i("PROGRESS", "onCreateView: max => " + mTaskProgressBar.getMax() + " progress => " + mTaskProgressBar.getProgress());

        //Todo : Write easy to read code again.
        //Show list info (Color,Task count,Complated tasks)
        switch (mList.getColor()){
            case R.color.grade_gray:
                mTaskProgressBar.getProgressDrawable().setColorFilter(getContext().getResources().getColor(R.color.grade_gray), PorterDuff.Mode.SRC_IN);
                mTaskToolbar.setBackgroundColor(getResources().getColor(R.color.grade_gray));
                break;
            case R.color.deep_orange:
                mTaskProgressBar.getProgressDrawable().setColorFilter(getContext().getResources().getColor(R.color.deep_orange), PorterDuff.Mode.SRC_IN);
                mTaskToolbar.setBackgroundColor(getResources().getColor(R.color.deep_orange));
                break;
            case R.color.evening_sunshine:
                mTaskProgressBar.getProgressDrawable().setColorFilter(getContext().getResources().getColor(R.color.evening_sunshine), PorterDuff.Mode.SRC_IN);
                mTaskToolbar.setBackgroundColor(getResources().getColor(R.color.evening_sunshine));
                break;
            case R.color.jade:
                mTaskProgressBar.getProgressDrawable().setColorFilter(getContext().getResources().getColor(R.color.jade), PorterDuff.Mode.SRC_IN);
                mTaskToolbar.setBackgroundColor(getResources().getColor(R.color.jade));
                break;
            case R.color.pinky_pink:
                mTaskProgressBar.getProgressDrawable().setColorFilter(getContext().getResources().getColor(R.color.pinky_pink), PorterDuff.Mode.SRC_IN);
                mTaskToolbar.setBackgroundColor(getResources().getColor(R.color.pinky_pink));
                break;
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
                    mList.setComplatedTaskCount(mList.getComplatedTaskCount() + 1);
                    mTaskCountTextview.setText(getString(R.string.task_count,mList.getComplatedTaskCount(),mList.getTaskCount()));
                    mTaskProgressBar.setProgress(mList.getComplatedTaskCount());
                }else{
                    mList.setComplatedTaskCount(mList.getComplatedTaskCount() - 1);
                    mTaskCountTextview.setText(getString(R.string.task_count,mList.getComplatedTaskCount(),mList.getTaskCount()));
                    mTaskProgressBar.setProgress(mList.getComplatedTaskCount());
                }
            }
        },mTaskClickListener,mList.getName());


        //RecyclerView adapter and divider.
        mTaskRecyclerView.setAdapter(mTaskAdapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mTaskRecyclerView.setLayoutManager(linearLayoutManager);
        //RecyclerView drag and drop.
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                final int position_dragged = viewHolder.getAdapterPosition();
                final int position_target = target.getAdapterPosition();
                int firstPos = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                int offsetTop = 0;

                View firstView = linearLayoutManager.findViewByPosition(firstPos);
                offsetTop = firstView.getTop();


                Collections.swap(mList.getTasks(),position_dragged,position_target);
                mTaskAdapter.notifyItemMoved(position_dragged,position_target);

                linearLayoutManager.scrollToPositionWithOffset(firstPos, offsetTop);


                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }

            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                if (actionState == ItemTouchHelper.DOWN || actionState == ItemTouchHelper.UP){
                    if (viewHolder instanceof ItemTouchHelperViewHolder){
                        ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
                        itemViewHolder.onItemSelected();
                    }
                }
                super.onSelectedChanged(viewHolder, actionState);
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                if (viewHolder instanceof ItemTouchHelperViewHolder){
                    ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
                    itemViewHolder.onItemClear();
                }
                super.clearView(recyclerView, viewHolder);
            }
        });

        itemTouchHelper.attachToRecyclerView(mTaskRecyclerView);

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
        mList.setTaskCount(mList.getTaskCount() + 1);

        mTaskAdapter.notifyItemInserted(mList.getTaskCount() - 1);
        mTaskRecyclerView.scrollToPosition(mList.getTaskCount() - 1);

        mTaskNameEdittext.setText("");
        mSetReminderButton.setText("Due Date");
        settedTime = 0;

        mTaskCountTextview.setText(getString(R.string.task_count,mList.getComplatedTaskCount(),mList.getTaskCount()));
        mTaskProgressBar.setMax(mList.getTaskCount());
    }


    @Override
    public void onStop() {
        List<Task> tasks = mList.getTasks();

        for (Task task : tasks){
            task.setTaskOrder(tasks.indexOf(task));
        }

        mDatabaseHelper.updateList(mList);
        mDatabaseHelper.updateTask(tasks);
        super.onStop();
    }
}
