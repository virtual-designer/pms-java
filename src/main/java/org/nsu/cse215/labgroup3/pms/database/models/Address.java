package org.nsu.cse215.labgroup3.pms.database.models;

public class Address {
    private User user;
    private String fullAddress;

    public Address() {}

    public Address(User user, String fullAddress) {
        this.user = user;
        this.fullAddress = fullAddress;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    @Override
    public String toString() {
        return "Address(userId=%d) { %s }".formatted(user.getId(), fullAddress);
    }
}
