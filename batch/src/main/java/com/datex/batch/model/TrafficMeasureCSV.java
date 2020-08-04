package com.datex.batch.model;

public class TrafficMeasureCSV {
    private String id;
    private String measurementTime;
    private String latitude;
    private String longitude;
    private String direction;
    private String road;
    private String status;
    private String averageVehicleSpeed;
    private String vehicleFlowRate;
    private String trafficConcentration;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMeasurementTime() {
        return measurementTime;
    }

    public void setMeasurementTime(String measurementTime) {
        this.measurementTime = measurementTime;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAverageVehicleSpeed() {
        return averageVehicleSpeed;
    }

    public void setAverageVehicleSpeed(String averageVehicleSpeed) {
        this.averageVehicleSpeed = averageVehicleSpeed;
    }

    public String getVehicleFlowRate() {
        return vehicleFlowRate;
    }

    public void setVehicleFlowRate(String vehicleFlowRate) {
        this.vehicleFlowRate = vehicleFlowRate;
    }

    public String getTrafficConcentration() {
        return trafficConcentration;
    }

    public void setTrafficConcentration(String trafficConcentration) {
        this.trafficConcentration = trafficConcentration;
    }

    @Override
    public String toString() {
        return "TrafficMeasureCSV{" +
                "id='" + id + '\'' +
                ", measurementTime='" + measurementTime + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", direction='" + direction + '\'' +
                ", road='" + road + '\'' +
                ", status='" + status + '\'' +
                ", averageVehicleSpeed='" + averageVehicleSpeed + '\'' +
                ", vehicleFlowRate='" + vehicleFlowRate + '\'' +
                ", trafficConcentration='" + trafficConcentration + '\'' +
                '}';
    }
}