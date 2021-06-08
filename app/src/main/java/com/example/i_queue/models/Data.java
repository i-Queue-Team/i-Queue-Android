package com.example.i_queue.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Data implements Parcelable {

    private int id, user_id;
    private String name, created_at, updated_at, info, image;
    private Double latitude, longitude;

    public Data(int id, int user_id, String name, String created_at, String updated_at, String info, String image, Double latitude, Double longitude) {
        this.id = id;
        this.user_id = user_id;
        this.name = name;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.info = info;
        this.image = image;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected Data(Parcel in) {
        id = in.readInt();
        user_id = in.readInt();
        name = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
        info = in.readString();
        image = in.readString();
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(user_id);
        dest.writeString(name);
        dest.writeString(created_at);
        dest.writeString(updated_at);
        dest.writeString(info);
        dest.writeString(image);
        if (latitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(latitude);
        }
        if (longitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(longitude);
        }
    }
}
