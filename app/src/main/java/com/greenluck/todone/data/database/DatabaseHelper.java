package com.greenluck.todone.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.greenluck.todone.model.Task;
import com.greenluck.todone.model.TaskList;
import com.greenluck.todone.util.ListUtil;
import com.greenluck.todone.util.TaskUtil;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TODO";
    private static final int DATABASE_VERSION = 1;

    //List table
    private static final String TABLE_NAME_1 ="lists";

    public static final String COLUMN_ID_1 = "_id";
    public static final String COLUMN_NAME_1 = "name";
    public static final String COLUMN_CREATION_DATE_1 = "date";
    public static final String COLUMN_COLOR_1 = "color";
    public static final String COLUMN_TASK_COUNT_1 = "count";
    public static final String COLUMN_COMPLATED_TASK_COUNT_1 = "complated_task_count";

    //Task table
    private static final String TABLE_NAME_2 = "tasks";

    public static final String COLUMN_ID_2 = "_id";
    public static final String COLUMN_CONTENT_2 = "content";
    public static final String COLUMN_PARENT_LIST_ID_2 = "list_id";
    public static final String COLUMN_DUE_DATE_2 = "due_date";
    public static final String COLUMN_STATUS_2 = "status";
    public static final String COLUMN_TASK_ORDER_2 = "task_order";

    private static DatabaseHelper sInstance = null;

    public DatabaseHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String CREATE_LISTS_TABLE = "CREATE TABLE " + TABLE_NAME_1 + " (" +
                COLUMN_ID_1 + " TEXT UNIQUE, " +
                COLUMN_NAME_1 + " TEXT, " +
                COLUMN_CREATION_DATE_1 + " TEXT, " +
                COLUMN_COLOR_1 + " TEXT, " +
                COLUMN_TASK_COUNT_1 + " INTEGER, " +
                COLUMN_COMPLATED_TASK_COUNT_1 + " INTEGER " + ");";

        sqLiteDatabase.execSQL(CREATE_LISTS_TABLE);

        final String CREATE_TASK_TABLE = "CREATE TABLE " + TABLE_NAME_2 + " (" +
                COLUMN_ID_2 + " TEXT UNIQUE, " +
                COLUMN_PARENT_LIST_ID_2 + " TEXT, " +
                COLUMN_CONTENT_2 + " TEXT, " +
                COLUMN_DUE_DATE_2 + " TEXT, " +
                COLUMN_STATUS_2 + " INTEGER, " +
                COLUMN_TASK_ORDER_2 + " INTEGER " + ");";

        sqLiteDatabase.execSQL(CREATE_TASK_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public long addList(TaskList list){
        ContentValues cv = ListUtil.toContentValues(list);
        Log.i("DATABASE", "addList: Added " + cv.getAsString(COLUMN_ID_1) + " " + cv.getAsString(COLUMN_NAME_1)+ " " + cv.getAsInteger(COLUMN_TASK_COUNT_1) + " " + cv.getAsInteger(COLUMN_COMPLATED_TASK_COUNT_1));

        return getWritableDatabase().insert(TABLE_NAME_1,null,cv);
    }

    public long deleteList(TaskList list){
        deleteTask(list.getId());
        String where = COLUMN_ID_1 + "=?";
        String [] whereArgs = new String[]{list.getId()};
        return getWritableDatabase().delete(TABLE_NAME_1,where,whereArgs);
    }

    public ArrayList<TaskList> getLists(){
        Cursor listCursor = null;
        String[] columns  = new String[]{COLUMN_ID_1,COLUMN_NAME_1,COLUMN_CREATION_DATE_1,COLUMN_COLOR_1,COLUMN_TASK_COUNT_1,COLUMN_COMPLATED_TASK_COUNT_1};
        try{
            listCursor = getReadableDatabase().query(TABLE_NAME_1,columns,null,null,null,null,null);
            return ListUtil.buildLists(listCursor);
        }finally {
            if (listCursor!=null && !listCursor.isClosed()) listCursor.close();
        }
    }

    public int updateList(TaskList list){
        final String where = COLUMN_ID_1 + "=?";
        final String[] whereArgs = new String[]{list.getId()};
        ContentValues cv = ListUtil.toContentValues(list);
        updateTask(list.getTasks());
        return getWritableDatabase().update(TABLE_NAME_1,cv,where,whereArgs);
    }

    private long addTask(Task task){
        Log.i("DATABASE", "addTask: database e eklenecek task " + task.getContent() + " parent list id => " + task.getParentListId() );
        ContentValues cv = TaskUtil.toContentValues(task);
        return getWritableDatabase().insert(TABLE_NAME_2,null,cv);
    }

    public long deleteTask(Task task){
        final String where = COLUMN_ID_2 + "=?";
        final String [] whereArgs = new String[]{task.getParentListId()};
        return getWritableDatabase().delete(TABLE_NAME_2,where,whereArgs);
    }

    public long deleteTask(String parentId){
        final String where = COLUMN_PARENT_LIST_ID_2 + "=?";
        final String[] whereArgs = new String[]{parentId};
        return getWritableDatabase().delete(TABLE_NAME_2,where,whereArgs);
    }

    // Todo : Find efficient way to update tasks.
    public void updateTask(List<Task> tasks){
        for (Task task : tasks){
            final String where = COLUMN_ID_2 + "=?";
            final String[] whereArgs = new String[]{task.getId()};
            ContentValues cv = TaskUtil.toContentValues(task);
            if (getWritableDatabase().update(TABLE_NAME_2,cv,where,whereArgs) == 0){
                addTask(task);
            }
        }
    }


    public ArrayList<Task> getTasks(String listId){
        Cursor c = null;
        ArrayList<Task> tasks = new ArrayList<>();
        final String where = COLUMN_PARENT_LIST_ID_2 + "=?";
        final String [] whereArgs = new String[]{listId};
        Log.i("DATABASE", "getTasks: ");
        try {
            c = getReadableDatabase().query(TABLE_NAME_2, null, where, whereArgs, null, null, null, null);
            Log.i("DATABASE", "getTasks: All task which list id = listId => " + c.getCount());
            return TaskUtil.buildTasks(c);
        }finally {
            if (c != null && !c.isClosed()) c.close();
        }
    }
}
