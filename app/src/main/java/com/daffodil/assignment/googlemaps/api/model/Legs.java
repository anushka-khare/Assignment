package com.daffodil.assignment.googlemaps.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Legs {

    @JsonProperty("duration")
    private PlaceDurations duration;

    @JsonProperty("distance")
    private PlaceDurations distance;

    @JsonProperty("duration_in_traffic")
    private PlaceDurations durationInTraffic;

    @JsonProperty("end_address")
    private String endAddress;

    @JsonProperty("end_location")
    private GoogleLocation endLocationLatLng;

    @JsonProperty("start_address")
    private String startAddress;

    @JsonProperty("start_location")
    private GoogleLocation startLocationLatLng;

    @JsonProperty("steps")
    private List<Steps> stepsList;

    public PlaceDurations getDuration() {
        return duration;
    }

    public void setDuration(PlaceDurations duration) {
        this.duration = duration;
    }

    public PlaceDurations getDistance() {
        return distance;
    }

    public void setDistance(PlaceDurations distance) {
        this.distance = distance;
    }

    public PlaceDurations getDurationInTraffic() {
        return durationInTraffic;
    }

    public void setDurationInTraffic(PlaceDurations durationInTraffic) {
        this.durationInTraffic = durationInTraffic;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public GoogleLocation getEndLocationLatLng() {
        return endLocationLatLng;
    }

    public void setEndLocationLatLng(GoogleLocation endLocationLatLng) {
        this.endLocationLatLng = endLocationLatLng;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public GoogleLocation getStartLocationLatLng() {
        return startLocationLatLng;
    }

    public void setStartLocationLatLng(GoogleLocation startLocationLatLng) {
        this.startLocationLatLng = startLocationLatLng;
    }

    public List<Steps> getStepsList() {
        return stepsList;
    }

    public void setStepsList(List<Steps> stepsList) {
        this.stepsList = stepsList;
    }
}
