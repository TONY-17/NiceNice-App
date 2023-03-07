package com.blueconnectionz.nicenice.owner.dashboard;

public class SingleOwnerCar {
    String image;
    String name;
    String info;
    String status;
    int connections;

    public int getConnections() {
        return connections;
    }

    public void setConnections(int connections) {
        this.connections = connections;
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

    public SingleOwnerCar(String image, String name, String info, String status, int connections) {
        this.image = image;
        this.name = name;
        this.info = info;
        this.status = status;
        this.connections = connections;
    }
}
