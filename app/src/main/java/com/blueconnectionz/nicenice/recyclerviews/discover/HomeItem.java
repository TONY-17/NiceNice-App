package com.blueconnectionz.nicenice.recyclerviews.discover;

/*
 * This class is used to model data for a single row on the main activity
 */
public class HomeItem {
    private Long id;
    private String image;
    private String owner;
    private String car;
    private String location;
    private String weeklyCheckInAmount;
    private boolean requiresDeposit;
    private String description;


    private int views;
    private int connections;
    private int age;
    private boolean onPlatform;


    private boolean hasInsurance;
    private boolean hasTracker;
    private boolean activeOnHailingPlatforms;
    private boolean available;

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getConnections() {
        return connections;
    }

    public void setConnections(int connections) {
        this.connections = connections;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isOnPlatform() {
        return onPlatform;
    }

    public void setOnPlatform(boolean onPlatform) {
        this.onPlatform = onPlatform;
    }

    public Long getId() {
        return id;
    }

    public boolean isHasInsurance() {
        return hasInsurance;
    }

    public void setHasInsurance(boolean hasInsurance) {
        this.hasInsurance = hasInsurance;
    }

    public boolean isHasTracker() {
        return hasTracker;
    }

    public void setHasTracker(boolean hasTracker) {
        this.hasTracker = hasTracker;
    }

    public boolean isActiveOnHailingPlatforms() {
        return activeOnHailingPlatforms;
    }

    public void setActiveOnHailingPlatforms(boolean activeOnHailingPlatforms) {
        this.activeOnHailingPlatforms = activeOnHailingPlatforms;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HomeItem(Long id, String image, String owner, String car, String location, String weeklyCheckInAmount,
                    boolean requiresDeposit,
                    String description,
                    int views,
                    int connections,
                    int age,
                    boolean onPlatform,
                    boolean hasInsurance,
                            boolean hasTracker,
                            boolean activeOnHailingPlatforms,
                            boolean available) {
        this.id = id;
        this.image = image;
        this.owner = owner;
        this.car = car;
        this.location = location;
        this.weeklyCheckInAmount = weeklyCheckInAmount;
        this.requiresDeposit = requiresDeposit;
        this.description = description;

        this.views = views;
        this.connections = connections;
        this.age = age;
        this.onPlatform = onPlatform;

        this.hasTracker = hasTracker;
        this.activeOnHailingPlatforms = activeOnHailingPlatforms;
        this.available = available;
        this.hasInsurance = hasInsurance;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRequiresDeposit(boolean requiresDeposit) {
        this.requiresDeposit = requiresDeposit;
    }
}
