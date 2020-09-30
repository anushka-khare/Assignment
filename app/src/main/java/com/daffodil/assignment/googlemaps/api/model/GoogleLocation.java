package com.daffodil.assignment.googlemaps.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GoogleLocation implements Parcelable {
    public static final Creator<GoogleLocation> CREATOR = new Creator<GoogleLocation>() {
        @Override
        public GoogleLocation createFromParcel(Parcel in) {
            return new GoogleLocation(in);
        }

        @Override
        public GoogleLocation[] newArray(int size) {
            return new GoogleLocation[size];
        }
    };
    @JsonProperty("lat")
    double lat;
    @JsonProperty("lng")
    double lng;

    String locationString;

    public GoogleLocation() {
    }

    public GoogleLocation(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    protected GoogleLocation(Parcel in) {
        lat = in.readDouble();
        lng = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(lat);
        dest.writeDouble(lng);
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getLocationString() {
        return lat + "," + lng;
    }

    public void setLocationString(String locationString) {
        this.locationString = locationString;
    }
}
