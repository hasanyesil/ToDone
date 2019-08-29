package com.greenluck.todone.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.greenluck.todone.data.database.DatabaseHelper;
import com.greenluck.todone.model.Task;

import java.util.ArrayList;

public class TaskUtil {

    public static ArrayList<Task> buildTasks(Cursor taskCursor){
        Log.i("TASK UTIL", "buildTasks: Cursor = null");
        if(taskCursor == null) return new ArrayList<>();
        Log.i("TASK UTIL", "buildTasks: Cursor != null");

        ArrayList<Task> tasks = new ArrayList<>();
        if (taskCursor.moveToFirst()){
            do{
                final long mTaskId = taskCursor.getLong(taskCursor.getColumnIndex(DatabaseHelper.COLUMN_ID_2));
                final String mContent = taskCursor.getString(taskCursor.getColumnIndex(DatabaseHelper.COLUMN_CONTENT_2));
                final long mParentListId = taskCursor.getLong(taskCursor.getColumnIndex(DatabaseHelper.COLUMN_PARENT_LIST_ID_2));
                final long mDueTime = taskCursor.getLong(taskCursor.getColumnIndex(DatabaseHelper.COLUMN_DUE_DATE_2));
                final int mStatus = taskCursor.getInt(taskCursor.getColumnIndex(DatabaseHelper.COLUMN_STATUS_2));
                final int mTaskOrder = taskCursor.getInt(taskCursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_ORDER_2));

                Task task = new Task(mTaskId,mContent,mParentListId,mDueTime,mStatus,mTaskOrder);
                tasks.add(task);
            }while(taskCursor.moveToNext());
        }
        return tasks;
    }

    public static ContentValues toContentValues(Task task){
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_ID_2,task.getId());
        cv.put(DatabaseHelper.COLUMN_CONTENT_2,task.getContent());
        cv.put(DatabaseHelper.COLUMN_PARENT_LIST_ID_2,task.getParentListId());
        cv.put(DatabaseHelper.COLUMN_DUE_DATE_2,task.getDueTime());
        cv.put(DatabaseHelper.COLUMN_STATUS_2,task.getStatus());
        cv.put(DatabaseHelper.COLUMN_TASK_ORDER_2,task.getTaskOrder());
        return cv;
    }
}
