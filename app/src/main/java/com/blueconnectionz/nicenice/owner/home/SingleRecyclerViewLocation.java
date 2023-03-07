package com.blueconnectionz.nicenice.owner.home;


/**
 * POJO model class for a single location in the recyclerview
 * Displays single driver information
 */
public class SingleRecyclerViewLocation {
    Long id;
    String image;
    String name;
    String location;
    String joinedDate;
    int numReferences;
    int views;
    boolean available;

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(String joinedDate) {
        this.joinedDate = joinedDate;
    }

    public int getNumReferences() {
        return numReferences;
    }

    public void setNumReferences(int numReferences) {
        this.numReferences = numReferences;
    }
}
