package tourGuide.model;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;

public class UserNearestAttractions {

    private Attraction attraction;
    private Location location;
    private int attractionProximityRangeMiles;
    private int rewardsPoints;

    public UserNearestAttractions(Attraction attraction, Location location, int attractionProximityRangeMiles,
                                  int rewardsPoints) {
        this.attraction = attraction;
        this.location = location;
        this.attractionProximityRangeMiles = attractionProximityRangeMiles;
        this.rewardsPoints = rewardsPoints;
    }

    public Attraction getAttraction() {
        return attraction;
    }

    public void setAttraction(Attraction attraction) {
        this.attraction = attraction;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getAttractionProximityRangeMiles() {
        return attractionProximityRangeMiles;
    }

    public void setAttractionProximityRangeMiles(int attractionProximityRangeMiles) {
        this.attractionProximityRangeMiles = attractionProximityRangeMiles;
    }

    public int getRewardsPoints() {
        return rewardsPoints;
    }

    public void setRewardsPoints(int rewardsPoints) {
        this.rewardsPoints = rewardsPoints;
    }
}
