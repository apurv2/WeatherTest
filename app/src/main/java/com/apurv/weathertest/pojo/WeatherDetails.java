package com.apurv.weathertest.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by akamalapuri on 7/5/2016.
 * POJO object to store Weather Details
 */
public class WeatherDetails implements Parcelable{

    String day;
    String high;
    String low;
    String condition;
    int maxWind;
    int averageWind;
    int averageHumidity;
    String imageUrl;

    public WeatherDetails(String day, String high, String low, String condition, int maxWind, int averageWind, int averageHumidity, String imageUrl) {
        this.day = day;
        this.high = high;
        this.low = low;
        this.condition = condition;
        this.maxWind = maxWind;
        this.averageWind = averageWind;
        this.averageHumidity = averageHumidity;
        this.imageUrl = imageUrl;
    }

    protected WeatherDetails(Parcel in) {
        day = in.readString();
        high = in.readString();
        low = in.readString();
        condition = in.readString();
        maxWind = in.readInt();
        averageWind = in.readInt();
        averageHumidity = in.readInt();
        imageUrl = in.readString();
    }

    public static final Creator<WeatherDetails> CREATOR = new Creator<WeatherDetails>() {
        @Override
        public WeatherDetails createFromParcel(Parcel in) {
            return new WeatherDetails(in);
        }

        @Override
        public WeatherDetails[] newArray(int size) {
            return new WeatherDetails[size];
        }
    };

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WeatherDetails that = (WeatherDetails) o;


        return getDay().equals(that.getDay());

    }

    @Override
    public int hashCode() {
        return getDay().hashCode();
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public int getMaxWind() {
        return maxWind;
    }

    public void setMaxWind(int maxWind) {
        this.maxWind = maxWind;
    }

    public int getAverageWind() {
        return averageWind;
    }

    public void setAverageWind(int averageWind) {
        this.averageWind = averageWind;
    }

    public int getAverageHumidity() {
        return averageHumidity;
    }

    public void setAverageHumidity(int averageHumidity) {
        this.averageHumidity = averageHumidity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(day);
        dest.writeString(high);
        dest.writeString(low);
        dest.writeString(condition);
        dest.writeInt(maxWind);
        dest.writeInt(averageWind);
        dest.writeInt(averageHumidity);
        dest.writeString(imageUrl);
    }
}
