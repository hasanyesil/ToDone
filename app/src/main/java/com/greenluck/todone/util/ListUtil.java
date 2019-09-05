package com.greenluck.todone.util;

import android.content.ContentValues;
import android.database.Cursor;

import com.greenluck.todone.data.database.DatabaseHelper;
import com.greenluck.todone.model.TaskList;

import java.util.ArrayList;
import java.util.UUID;

public class ListUtil {

    public static long generateUniqueId(){
        return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    }

    public static ContentValues toContentValues(TaskList list){
        final ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_ID_1, list.getId());
        cv.put(DatabaseHelper.COLUMN_NAME_1,list.getName());
        cv.put(DatabaseHelper.COLUMN_CREATION_DATE_1,list.getCreationDate());
        cv.put(DatabaseHelper.COLUMN_COLOR_1,list.getColor());
        cv.put(DatabaseHelper.COLUMN_TASK_COUNT_1,list.getTaskCount());
        cv.put(DatabaseHelper.COLUMN_COMPLATED_TASK_COUNT_1,list.getComplatedTaskCount());
        return cv;
    }

    public static ArrayList<TaskList> buildLists(Cursor listCursor){

        if (listCursor == null) return new ArrayList<>();

        final ArrayList<TaskList> taskLists = new ArrayList<>();

        if (listCursor.moveToFirst()){
            do {
                final String listId = listCursor.getString(listCursor.getColumnIndex(DatabaseHelper.COLUMN_ID_1));
                final String listName = listCursor.getString(listCursor.getColumnIndex(DatabaseHelper.COLUMN_NAME_1));
                final long listCreationDate = listCursor.getLong(listCursor.getColumnIndex(DatabaseHelper.COLUMN_CREATION_DATE_1));
                final int listColor = listCursor.getInt(listCursor.getColumnIndex(DatabaseHelper.COLUMN_COLOR_1));
                final int listTaskCount = listCursor.getInt(listCursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_COUNT_1));
                final int listComplatedTaskCount = listCursor.getInt(listCursor.getColumnIndex(DatabaseHelper.COLUMN_COMPLATED_TASK_COUNT_1));
                taskLists.add(new TaskList(listId,listName,listCreationDate,listColor,listTaskCount,listComplatedTaskCount));
            }while (listCursor.moveToNext());
        }

        return taskLists;
        /* // If database has no list return empty array
        if (listCursor == null) return new ArrayList<>();

        final ArrayList<com.greenluck.todone.model.List> lists = new ArrayList<>();

        if (listCursor.moveToFirst()){
            do {
                final String mListId = listCursor.getString(listCursor.getColumnIndex(DatabaseHelper.COLUMN_ID_1));
                final String mListName = listCursor.getString(listCursor.getColumnIndex(DatabaseHelper.COLUMN_NAME_1));
                final long mListCreationDate = listCursor.getLong(listCursor.getColumnIndex(DatabaseHelper.COLUMN_CREATION_DATE_1));
                final int mListColor = listCursor.getInt(listCursor.getColumnIndex(DatabaseHelper.COLUMN_COLOR_1));
                final int mListTaskCount = listCursor.getInt(listCursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_COUNT_1));

                final ArrayList<Task> tasks = helper.getTasks(mListId);
                com.greenluck.todone.model.List list = new com.greenluck.todone.model.List(mListId, mListName, mListCreationDate, mListColor, mListTaskCount,tasks);
                lists.add(list);
            } while (listCursor.moveToNext());
        }
        return lists;*/
    }

}
