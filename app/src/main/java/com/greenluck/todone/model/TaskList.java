package com.greenluck.todone.model;

import android.os.Parcel;
import android.os.Parcelable;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskList implements Parcelable {

    private String mId;
    private String mName;
    private long mCreationDate;
    private int mColor;
    private int mTaskCount;
    private int mComplatedTaskCount;
    private List<Task> mTasks;


    public TaskList(Parcel in){
        mId = in.readString();
        mName = in.readString();
        mCreationDate = in.readLong();
        mColor = in.readInt();
        mTaskCount = in.readInt();
        mComplatedTaskCount = in.readInt();
        mTasks = in.createTypedArrayList(Task.CREATOR);
    }

    public static final Creator<TaskList> CREATOR = new Creator<TaskList>() {
        @Override
        public TaskList createFromParcel(Parcel in) {
            return new TaskList(in);
        }

        @Override
        public TaskList[] newArray(int size) {
            return new TaskList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mName);
        parcel.writeLong(mCreationDate);
        parcel.writeInt(mColor);
        parcel.writeInt(mTaskCount);
        parcel.writeInt(mComplatedTaskCount);
        parcel.writeTypedList(mTasks);
    }

    // If try to fetch list from database, you have to use this constructer.
    public TaskList(String id, String name, long creationDate, int color, int taskCount, int complatedTaskCount) {
        mId = id;
        mName = name;
        mCreationDate = creationDate;
        mColor = color;
        mTaskCount = taskCount;
        mComplatedTaskCount = complatedTaskCount;
    }

    // When user creates new list
    public TaskList(String name, int color){
        mId = UUID.randomUUID().toString();
        mName = name;
        mCreationDate = System.currentTimeMillis();
        mColor = color;
        mTaskCount = 0;
        mComplatedTaskCount = 0;
    }


    public void addTask(Task task){
        if (mTasks == null)
            mTasks = new ArrayList<>();
        mTasks.add(task);
        mTaskCount = mTasks.size();
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public long getCreationDate() {
        return mCreationDate;
    }

    public void setCreationDate(long creationDate) {
        mCreationDate = creationDate;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public int getTaskCount() {
        return mTaskCount;
    }

    public int getComplatedTaskCount(){
        int count = 0;
        if (mTasks == null)
            return count;

        for (Task task : mTasks){
            if (task.getStatus() == 1){
                count++;
            }
        }
        return count;
    }

    public void setTaskCount(int taskCount) {
        mTaskCount = taskCount;
    }

    public java.util.List<Task> getTasks() {
        return mTasks;
    }

    public void setTasks(java.util.List<Task> tasks) {
        this.mTasks = tasks;
    }

}

