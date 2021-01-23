package tourGuide.model;

import tourGuide.model.location.Location;

public class UserNearestAttractionsModel {

    private String attractionName;
    private double attractionLongitude;
    private double attractionLatitude;
    private Location location;
    private double attractionProximityRangeMiles;
    private int rewardsPoints;

    public UserNearestAttractionsModel(String attractionName, double attractionLongitude, double attractionLatitude,
                                       Location location, double attractionProximityRangeMiles, int rewardsPoints) {
        this.attractionName = attractionName;
        this.attractionLongitude = attractionLongitude;
        this.attractionLatitude = attractionLatitude;
        this.location = location;
        this.attractionProximityRangeMiles = attractionProximityRangeMiles;
        this.rewardsPoints = rewardsPoints;
    }

    public String getAttractionName() {
        return attractionName;
    }

    public void setAttractionName(String attractionName) {
        this.attractionName = attractionName;
    }

    public double getAttractionLongitude() {
        return attractionLongitude;
    }

    public void setAttractionLongitude(double attractionLongitude) {
        this.attractionLongitude = attractionLongitude;
    }

    public double getAttractionLatitude() {
        return attractionLatitude;
    }

    public void setAttractionLatitude(double attractionLatitude) {
        this.attractionLatitude = attractionLatitude;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public double getAttractionProximityRangeMiles() {
        return attractionProximityRangeMiles;
    }

    public void setAttractionProximityRangeMiles(double attractionProximityRangeMiles) {
        this.attractionProximityRangeMiles = attractionProximityRangeMiles;
    }

    public int getRewardsPoints() {
        return rewardsPoints;
    }

    public void setRewardsPoints(int rewardsPoints) {
        this.rewardsPoints = rewardsPoints;
    }
}