package com.greenluck.todone.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.greenluck.todone.util.ListUtil;

import java.util.UUID;

public class Task implements Parcelable {

    private String mId;
    private String mContent;
    private String mParentListId;
    private long mDueTime;
    // status -> 0:uncomplated, 1:complated
    private int mStatus;
    // Tasks order on the view
    private int mTaskOrder;


    public Task(Parcel in){
        mId = in.readString();
        mContent = in.readString();
        mParentListId = in.readString();
        mDueTime = in.readLong();
        mStatus = in.readInt();
        mTaskOrder = in.readInt();
    }


    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mContent);
        parcel.writeString(mParentListId);
        parcel.writeLong(mDueTime);
        parcel.writeInt(mStatus);
        parcel.writeInt(mTaskOrder);
    }


    // For fetching tasks from database.
    public Task(String id, String content, String listId, long dueTime, int status, int taskOrder) {
        mId = id;
        mContent = content;
        mParentListId = listId;
        mDueTime = dueTime;
        mStatus = status;
        mTaskOrder = taskOrder;
    }

    // When user create new task
    public Task(String content, String parentListId, long dueTime, int status, int taskOrder){
        mId = UUID.randomUUID().toString();
        mContent = content;
        mParentListId = parentListId;
        mDueTime = dueTime;
        mStatus = status;
        mTaskOrder = taskOrder;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public String getParentListId() {
        return mParentListId;
    }

    public void setParentListId(String parentListId) {
        mParentListId = parentListId;
    }


    public long getDueTime() {
        return mDueTime;
    }

    public void setDueTime(long dueTime) {
        mDueTime = dueTime;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public int getTaskOrder() {
        return mTaskOrder;
    }

    public void setTaskOrder(int taskOrder) {
        mTaskOrder = taskOrder;
    }

}
