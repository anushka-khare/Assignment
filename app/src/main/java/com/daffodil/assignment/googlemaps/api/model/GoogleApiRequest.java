package com.daffodil.assignment.googlemaps.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GoogleApiRequest {
    @JsonProperty("input")
    String input;
    @JsonProperty("radius")
    String radius;
    @JsonProperty("location")
    String location;
    @JsonProperty("key")
    String key;
    @JsonProperty("sessiontoken")
    String sessiontoken;
    @JsonProperty("placeid")
    String placeId;
    @JsonProperty("fields")
    String fields;
    @JsonProperty("origin")
    String origin;
    @JsonProperty("destination")
    String destination;
    @JsonProperty("departure_time")
    String departure_time;
    @JsonProperty("waypoints")
    String waypoints;
    @JsonProperty("mode")
    String mode;

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSessiontoken() {
        return sessiontoken;
    }

    public void setSessiontoken(String sessiontoken) {
        this.sessiontoken = sessiontoken;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(String departure_time) {
        this.departure_time = departure_time;
    }

    public String getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(String waypoints) {
        this.waypoints = waypoints;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}


