package com.blueconnectionz.nicenice.owner.dashboard;

public class SingleOwnerCar {
    int image;
    String name;
    String info;
    String status;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SingleOwnerCar(int image, String name, String info, String status) {
        this.image = image;
        this.name = name;
        this.info = info;
        this.status = status;
    }
}
