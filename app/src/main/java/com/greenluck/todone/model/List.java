package com.greenluck.todone.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.greenluck.todone.util.ListUtil;

import java.util.ArrayList;

public class List implements Parcelable {

    private long mId;
    private String mName;
    private long mCreationDate;
    private int mColor;
    private int mTaskCount;
    private java.util.List<Task> mTasks;


    public List(Parcel in){
        mId = in.readLong();
        mName = in.readString();
        mCreationDate = in.readLong();
        mColor = in.readInt();
        mTaskCount = in.readInt();
        mTasks = in.createTypedArrayList(Task.CREATOR);
    }

    public static final Creator<List> CREATOR = new Creator<List>() {
        @Override
        public List createFromParcel(Parcel in) {
            return new List(in);
        }

        @Override
        public List[] newArray(int size) {
            return new List[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(mId);
        parcel.writeString(mName);
        parcel.writeLong(mCreationDate);
        parcel.writeInt(mColor);
        parcel.writeInt(mTaskCount);
        parcel.writeTypedList(mTasks);
    }

    // If try to fetch list from database, you have to use this constructer.
    public List(long id, String name, long creationDate, int color, int taskCount, java.util.List<Task> tasks) {
        mId = id;
        mName = name;
        mCreationDate = creationDate;
        mColor = color;
        mTaskCount = taskCount;
        mTasks = tasks;
    }

    // When user creates new list without
    public List(String name, int color){
        mId = ListUtil.generateUniqueId();
        mName = name;
        mCreationDate = System.currentTimeMillis();
        mColor = color;
        mTaskCount = 0;
        mTasks = new ArrayList<>();
    }

    public void addTask(Task task){
        mTasks.add(task);
        mTaskCount++;
    }

    public long getId() {
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
        return mTasks.size();
    }

    public int getComplatedTaskCount(){
        int count = 0;
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

