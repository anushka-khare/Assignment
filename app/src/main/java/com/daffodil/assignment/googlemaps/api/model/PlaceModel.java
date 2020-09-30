package com.daffodil.assignment.googlemaps.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PlaceModel implements Parcelable {
    public static final Creator<PlaceModel> CREATOR = new Creator<PlaceModel>() {
        @Override
        public PlaceModel createFromParcel(Parcel in) {
            return new PlaceModel(in);
        }

        @Override
        public PlaceModel[] newArray(int size) {
            return new PlaceModel[size];
        }
    };
    @JsonProperty("description")
    private String description;
    @JsonProperty("place_id")
    private String place_id;
    @JsonProperty("structured_formatting")
    private PlaceStructuredFormat placeStructuredFormat;
    @JsonProperty("terms")
    private ArrayList<Terms> terms;
    private String state;
    //@JsonInclude(JsonInclude.Include.NON_EMPTY)
    // private ContactInfoModel contactInfoModel;
    @JsonProperty("geometry")
    private Geometry geometry;
    private int drop_id;
    @JsonProperty("favorite")
    private boolean isFavorite;
    private boolean isRecent;
    private String country;

    public PlaceModel() {
    }

    protected PlaceModel(Parcel in) {
        place_id = in.readString();
        description = in.readString();
        placeStructuredFormat = in.readParcelable(PlaceStructuredFormat.class.getClassLoader());
        terms = in.createTypedArrayList(Terms.CREATOR);
        state = in.readString();
        geometry = in.readParcelable(Geometry.class.getClassLoader());
        drop_id = in.readInt();
        isFavorite = in.readByte() != 0;
        isRecent = in.readByte() != 0;
        country = in.readString();
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public PlaceStructuredFormat getPlaceStructuredFormat() {
        return placeStructuredFormat;
    }

    public void setPlaceStructuredFormat(PlaceStructuredFormat placeStructuredFormat) {
        this.placeStructuredFormat = placeStructuredFormat;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

   /* public ContactInfoModel getContactInfoModel() {
        return contactInfoModel;
    }

    public void setContactInfoModel(ContactInfoModel contactInfoModel) {
        this.contactInfoModel = contactInfoModel;
    }*/

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public ArrayList<Terms> getTerms() {
        return terms;
    }

    public void setTerms(ArrayList<Terms> terms) {
        this.terms = terms;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getDrop_id() {
        return drop_id;
    }

    public void setDrop_id(int drop_id) {
        this.drop_id = drop_id;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isRecent() {
        return isRecent;
    }

    public void setRecent(boolean recent) {
        isRecent = recent;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(place_id);
        dest.writeString(description);
        dest.writeParcelable(placeStructuredFormat, flags);
        dest.writeTypedList(terms);
        dest.writeString(state);
        dest.writeParcelable(geometry, flags);
        dest.writeInt(drop_id);
        dest.writeByte((byte) (isFavorite ? 1 : 0));
        dest.writeByte((byte) (isRecent ? 1 : 0));
        dest.writeString(country);
    }
}
