package com.applicationlist.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vikrant Chauhan on 10/7/2016.
 */

public class Contact implements Parcelable{

    public Contact(String name, int number,String note, int selected) {
        this.name = name;
        this.number = number;
        this.note = note;
        this.isSelected = selected==1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    private String name;
    private int number;

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public static Creator getCREATOR() {
        return CREATOR;
    }

    private Boolean isSelected;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    private String note;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this.name);
        dest.writeInt(this.number);
        dest.writeString(this.note);
    }

    public Contact(Parcel in){
        this.name = in.readString();
        this.number = in.readInt();
        this.note = in.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){

        @Override
        public Object createFromParcel(Parcel source) {
            return new Contact(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new Contact[size];
        }
    };
}
