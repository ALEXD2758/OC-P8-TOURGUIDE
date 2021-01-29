package tourGuide.service;

import tourGuide.exception.UUIDException;
import tourGuide.model.location.Attraction;
import tourGuide.model.location.VisitedLocation;
import org.junit.Test;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.UserModel;
import tourGuide.model.UserRewardModel;
import tourGuide.webclient.GpsUtilWebClient;
import tourGuide.webclient.RewardsWebClient;
import tourGuide.webclient.TripPricerWebClient;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestRewardsService {

	@Test
	public void userGetRewards() {


		InternalTestHelper.setInternalUserNumber(0);
		InternalTestService internalTestService = new InternalTestService();
		GpsUtilWebClient gpsUtilWebClient = new GpsUtilWebClient();
		TripPricerWebClient tripPricerWebClient = new TripPricerWebClient();
		RewardsWebClient rewardsWebClient = new RewardsWebClient();
		RewardsService rewardsService = new RewardsService(gpsUtilWebClient, rewardsWebClient);
		TourGuideService tourGuideService = new TourGuideService(rewardsService, internalTestService,
				gpsUtilWebClient, tripPricerWebClient);
		
		UserModel user = new UserModel(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		Attraction attraction = gpsUtilWebClient.getAllAttractionsWebClient().get(0);
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
		tourGuideService.trackUserLocation(user);
		List<UserRewardModel> userRewards = user.getUserRewards();
		tourGuideService.tracker.stopTracking();
		assertTrue(userRewards.size() == 1);
	}
	
	@Test
	public void isWithinAttractionProximity() {
        GpsUtilWebClient gpsUtilWebClient = new GpsUtilWebClient();
		RewardsWebClient rewardsWebClient = new RewardsWebClient();
		RewardsService rewardsService = new RewardsService(gpsUtilWebClient, rewardsWebClient);
		Attraction attraction = gpsUtilWebClient.getAllAttractionsWebClient().get(0);
		assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
	}

	@Test
	public void nearAttraction() {
		GpsUtilWebClient gpsUtilWebClient = new GpsUtilWebClient();
		RewardsWebClient rewardsWebClient = new RewardsWebClient();
		RewardsService rewardsService = new RewardsService(gpsUtilWebClient, rewardsWebClient);
		InternalTestHelper.setInternalUserNumber(1);

		InternalTestService internalTestService = new InternalTestService();
		TripPricerWebClient tripPricerWebClient = new TripPricerWebClient();
		TourGuideService tourGuideService = new TourGuideService(rewardsService, internalTestService,
				gpsUtilWebClient, tripPricerWebClient);
		rewardsService.setProximityBuffer(Integer.MAX_VALUE);

		tourGuideService.tracker.stopTracking();

		rewardsService.calculateRewards(tourGuideService.getAllUsers().get(0));
		List<UserRewardModel> userRewards = tourGuideService.getUserRewards(tourGuideService.getAllUsers().get(0));

		System.out.println(userRewards);
		assertEquals(gpsUtilWebClient.getAllAttractionsWebClient().size(), userRewards.size());
	}
}