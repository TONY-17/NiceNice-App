package com.blueconnectionz.nicenice.driver.profile.pages;

public class Transaction {
    private String date;
    private int amount;
    private boolean isAdminTopUp;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isAdminTopUp() {
        return isAdminTopUp;
    }

    public void setAdminTopUp(boolean adminTopUp) {
        isAdminTopUp = adminTopUp;
    }


    public Transaction(String date, int amount, boolean isAdminTopUp) {
        this.date = date;
        this.amount = amount;
        this.isAdminTopUp = isAdminTopUp;
    }
}
