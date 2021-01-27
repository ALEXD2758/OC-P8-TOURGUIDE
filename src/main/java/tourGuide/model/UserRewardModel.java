package tourGuide.model;

import tourGuide.model.location.Attraction;
import tourGuide.model.location.VisitedLocation;

public class UserRewardModel {

	public final VisitedLocation visitedLocation;
	public final Attraction attraction;
	private int rewardPoints;

	public UserRewardModel(VisitedLocation visitedLocation, Attraction attraction, int rewardPoints) {
		this.visitedLocation = visitedLocation;
		this.attraction = attraction;
		this.rewardPoints = rewardPoints;
	}
	
	public UserRewardModel(VisitedLocation visitedLocation, Attraction attraction) {
		this.visitedLocation = visitedLocation;
		this.attraction = attraction;
	}

	public void setRewardPoints(int rewardPoints) {
		this.rewardPoints = rewardPoints;
	}
	
	public int getRewardPoints() {
		return rewardPoints;
	}
}