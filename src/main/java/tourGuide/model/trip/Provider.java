package tourGuide.model.trip;

import java.util.UUID;

public class Provider {
    public String name;
    public double price;
    public UUID tripId;

    public Provider() {
    }

    public Provider(UUID tripId, String name, double price) {
        this.name = name;
        this.tripId = tripId;
        this.price = price;
    }
}