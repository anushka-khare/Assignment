package com.daffodil.assignment.googlemaps.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GoogleApiResponse {
    @JsonProperty("status")
    String status;

    @JsonProperty("predictions")
    List<PlaceModel> placeModelList;

    @JsonProperty("result")
    PlaceModel placeModel;

    @JsonProperty("routes")
    ArrayList<Routes> routesArrayList;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PlaceModel> getPlaceModelList() {
        return placeModelList;
    }

    public void setPlaceModelList(List<PlaceModel> placeModelList) {
        this.placeModelList = placeModelList;
    }

    public PlaceModel getPlaceModel() {
        return placeModel;
    }

    public void setPlaceModel(PlaceModel placeModel) {
        this.placeModel = placeModel;
    }

    public ArrayList<Routes> getRoutesArrayList() {
        return routesArrayList;
    }

    public void setRoutesArrayList(ArrayList<Routes> routesArrayList) {
        this.routesArrayList = routesArrayList;
    }
}
