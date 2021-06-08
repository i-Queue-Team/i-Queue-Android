package com.example.i_queue.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Queue implements Parcelable {

    private int id, position, queue_id, user_id;
    private String name, estimated_time, created_at, updated_at, image;

    public Queue() {
    }

    protected Queue(Parcel in) {
        id = in.readInt();
        position = in.readInt();
        queue_id = in.readInt();
        user_id = in.readInt();
        name = in.readString();
        estimated_time = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
        image = in.readString();
    }

    public static final Creator<Queue> CREATOR = new Creator<Queue>() {
        @Override
        public Queue createFromParcel(Parcel in) {
            return new Queue(in);
        }

        @Override
        public Queue[] newArray(int size) {
            return new Queue[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getQueue_id() {
        return queue_id;
    }

    public void setQueue_id(int queue_id) {
        this.queue_id = queue_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEstimated_time() {
        return estimated_time;
    }

    public void setEstimated_time(String estimated_time) {
        this.estimated_time = estimated_time;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(position);
        dest.writeInt(queue_id);
        dest.writeInt(user_id);
        dest.writeString(name);
        dest.writeString(estimated_time);
        dest.writeString(created_at);
        dest.writeString(updated_at);
        dest.writeString(image);
    }
}
