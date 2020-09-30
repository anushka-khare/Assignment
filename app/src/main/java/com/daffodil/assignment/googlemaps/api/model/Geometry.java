package com.daffodil.assignment.googlemaps.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Geometry implements Parcelable {
    public static final Creator<Geometry> CREATOR = new Creator<Geometry>() {
        @Override
        public Geometry createFromParcel(Parcel in) {
            return new Geometry(in);
        }

        @Override
        public Geometry[] newArray(int size) {
            return new Geometry[size];
        }
    };
    @JsonProperty("location")
    private GoogleLocation googleLocation;

    public Geometry() {
    }

    public Geometry(GoogleLocation googleLocation) {
        this.googleLocation = googleLocation;
    }

    protected Geometry(Parcel in) {
        googleLocation = in.readParcelable(GoogleLocation.class.getClassLoader());
    }

    public GoogleLocation getGoogleLocation() {
        return googleLocation;
    }

    public void setGoogleLocation(GoogleLocation googleLocation) {
        this.googleLocation = googleLocation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(googleLocation, flags);
    }
}
