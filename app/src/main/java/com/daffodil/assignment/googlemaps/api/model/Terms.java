package com.daffodil.assignment.googlemaps.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Terms implements Parcelable {

    public static final Creator<Terms> CREATOR = new Creator<Terms>() {
        @Override
        public Terms createFromParcel(Parcel in) {
            return new Terms(in);
        }

        @Override
        public Terms[] newArray(int size) {
            return new Terms[size];
        }
    };
    @JsonProperty("value")
    String value;
    @JsonProperty("offset")
    String offset;

    public Terms() {
    }

    public Terms(String value) {
        this.value = value;
    }

    protected Terms(Parcel in) {
        value = in.readString();
        offset = in.readString();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(value);
        dest.writeString(offset);
    }
}
