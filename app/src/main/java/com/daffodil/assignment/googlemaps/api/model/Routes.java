package com.daffodil.assignment.googlemaps.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Routes {
    @JsonProperty("legs")
    ArrayList<Legs> legsArrayList;
    @JsonProperty("overview_polyline")
    private OverviewPolyline overviewPolyline;

    public OverviewPolyline getOverviewPolyline() {
        return overviewPolyline;
    }

    public void setOverviewPolyline(OverviewPolyline overviewPolyline) {
        this.overviewPolyline = overviewPolyline;
    }

    public ArrayList<Legs> getLegsArrayList() {
        return legsArrayList;
    }

    public void setLegsArrayList(ArrayList<Legs> legsArrayList) {
        this.legsArrayList = legsArrayList;
    }
}
