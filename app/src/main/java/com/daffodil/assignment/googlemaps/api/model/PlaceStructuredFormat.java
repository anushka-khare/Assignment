package com.daffodil.assignment.googlemaps.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PlaceStructuredFormat implements Parcelable {
    public static final Creator<PlaceStructuredFormat> CREATOR = new Creator<PlaceStructuredFormat>() {
        @Override
        public PlaceStructuredFormat createFromParcel(Parcel in) {
            return new PlaceStructuredFormat(in);
        }

        @Override
        public PlaceStructuredFormat[] newArray(int size) {
            return new PlaceStructuredFormat[size];
        }
    };
    @JsonProperty("main_text")
    String main_text;
    @JsonProperty("secondary_text")
    String secondary_text;

    public PlaceStructuredFormat() {
    }

    public PlaceStructuredFormat(String main_text, String secondary_text) {
        this.main_text = main_text;
        this.secondary_text = secondary_text;
    }

    protected PlaceStructuredFormat(Parcel in) {
        main_text = in.readString();
        secondary_text = in.readString();
    }

    public String getMain_text() {
        return main_text;
    }

    public void setMain_text(String main_text) {
        this.main_text = main_text;
    }

    public String getSecondary_text() {
        return secondary_text;
    }

    public void setSecondary_text(String secondary_text) {
        this.secondary_text = secondary_text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(main_text);
        dest.writeString(secondary_text);
    }
}
