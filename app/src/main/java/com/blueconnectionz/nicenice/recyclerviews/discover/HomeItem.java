package com.blueconnectionz.nicenice.recyclerviews.discover;

/*
 * This class is used to model data for a single row on the main activity
 */
public class HomeItem {
    private String image;
    private String owner;
    private String car;
    private String location;
    private String weeklyCheckInAmount;
    private boolean requiresDeposit;


    public HomeItem(String image, String owner, String car, String location, String weeklyCheckInAmount, boolean requiresDeposit) {
        this.image = image;
        this.owner = owner;
        this.car = car;
        this.location = location;
        this.weeklyCheckInAmount = weeklyCheckInAmount;
        this.requiresDeposit = requiresDeposit;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWeeklyCheckInAmount() {
        return weeklyCheckInAmount;
    }

    public void setWeeklyCheckInAmount(String weeklyCheckInAmount) {
        this.weeklyCheckInAmount = weeklyCheckInAmount;
    }

    public boolean isRequiresDeposit() {
        return requiresDeposit;
    }

    public void setRequiresDeposit(boolean requiresDeposit) {
        this.requiresDeposit = requiresDeposit;
    }
}
