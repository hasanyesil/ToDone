package com.greenluck.todone.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.greenluck.todone.util.ListUtil;

public class Task implements Parcelable {

    private long mId;
    private String mContent;
    private long mParentListId;
    private long mDueTime;
    // status -> 0:uncomplated, 1:complated
    private int mStatus;
    // Tasks order on the view
    private int mTaskOrder;


    public Task(Parcel in){
        mId = in.readLong();
        mContent = in.readString();
        mParentListId = in.readLong();
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
        parcel.writeLong(mId);
        parcel.writeString(mContent);
        parcel.writeLong(mParentListId);
        parcel.writeLong(mDueTime);
        parcel.writeInt(mStatus);
        parcel.writeInt(mTaskOrder);
    }


    // For fetching tasks from database.
    public Task(long id, String content, long listId, long dueTime, int status, int taskOrder) {
        mId = id;
        mContent = content;
        mParentListId = listId;
        mDueTime = dueTime;
        mStatus = status;
        mTaskOrder = taskOrder;
    }

    // When user create new task
    public Task(String content, long parentListId, long dueTime, int status, int taskOrder){
        mId = ListUtil.generateUniqueId();
        mContent = content;
        mParentListId = parentListId;
        mDueTime = dueTime;
        mStatus = status;
        mTaskOrder = taskOrder;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public long getParentListId() {
        return mParentListId;
    }

    public void setParentListId(int parentListId) {
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
