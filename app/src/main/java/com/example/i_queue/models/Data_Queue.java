package com.example.i_queue.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Data_Queue implements Parcelable {

    private int fixed_capacity, current_capacity, average_time, id;
    private String password_verification, updated_at, created_at, image, name;

    public Data_Queue(int fixed_capacity, int current_capacity, int average_time, int id, String password_verification, String updated_at, String created_at, String image, String name) {
        this.fixed_capacity = fixed_capacity;
        this.current_capacity = current_capacity;
        this.average_time = average_time;
        this.id = id;
        this.password_verification = password_verification;
        this.updated_at = updated_at;
        this.created_at = created_at;
        this.image = image;
        this.name = name;
    }

    public Data_Queue() {
    }

    protected Data_Queue(Parcel in) {
        fixed_capacity = in.readInt();
        current_capacity = in.readInt();
        average_time = in.readInt();
        id = in.readInt();
        password_verification = in.readString();
        updated_at = in.readString();
        created_at = in.readString();
        image = in.readString();
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(fixed_capacity);
        dest.writeInt(current_capacity);
        dest.writeInt(average_time);
        dest.writeInt(id);
        dest.writeString(password_verification);
        dest.writeString(updated_at);
        dest.writeString(created_at);
        dest.writeString(image);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Data_Queue> CREATOR = new Creator<Data_Queue>() {
        @Override
        public Data_Queue createFromParcel(Parcel in) {
            return new Data_Queue(in);
        }

        @Override
        public Data_Queue[] newArray(int size) {
            return new Data_Queue[size];
        }
    };

    public int getFixed_capacity() {
        return fixed_capacity;
    }

    public void setFixed_capacity(int fixed_capacity) {
        this.fixed_capacity = fixed_capacity;
    }

    public int getCurrent_capacity() {
        return current_capacity;
    }

    public void setCurrent_capacity(int current_capacity) {
        this.current_capacity = current_capacity;
    }

    public int getAverage_time() {
        return average_time;
    }

    public void setAverage_time(int average_time) {
        this.average_time = average_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword_verification() {
        return password_verification;
    }

    public void setPassword_verification(String password_verification) {
        this.password_verification = password_verification;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
