package tourGuide.model.location;

public class Location {
    public double longitude;
    public double latitude;

    public Location() {
    }

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}