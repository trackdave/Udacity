package com.dcasillasappdev.currencyexchangerates.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Currency implements Parcelable {

    private double rate;
    private String date;
    private String base;
    private String target;
    private long id;

    public Currency() {
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.rate);
        dest.writeString(this.date);
        dest.writeString(this.target);
        dest.writeString(this.base);
        dest.writeLong(this.id);
    }

    protected Currency(Parcel in) {
        this.rate = in.readDouble();
        this.date = in.readString();
        this.target = in.readString();
        this.base = in.readString();
        this.id = in.readLong();
    }

    public static final Creator<Currency> CREATOR = new Creator<Currency>() {
        @Override
        public Currency createFromParcel(Parcel source) {
            return new Currency(source);
        }

        @Override
        public Currency[] newArray(int size) {
            return new Currency[size];
        }
    };
}