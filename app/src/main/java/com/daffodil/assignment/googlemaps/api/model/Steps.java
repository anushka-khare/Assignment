package com.daffodil.assignment.googlemaps.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Steps {

    @JsonProperty("polyline")
    private OverviewPolyline mPolyline;

    @JsonProperty("travel_mode")
    private String mTravelMode;

    public OverviewPolyline getmPolyline() {
        return mPolyline;
    }

    public void setmPolyline(OverviewPolyline mPolyline) {
        this.mPolyline = mPolyline;
    }

    public String getmTravelMode() {
        return mTravelMode;
    }

    public void setmTravelMode(String mTravelMode) {
        this.mTravelMode = mTravelMode;
    }
}
