package tourGuide.model;

import tourGuide.model.location.Location;

public class UserLocationModel {

    private Location location;
    private String userId;


    public UserLocationModel(Location location, String userId) {
        this.userId = userId;
        this.location = location;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}