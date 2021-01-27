package tourGuide;

import tourGuide.exception.UUIDException;
import tourGuide.model.UserRewardModel;
import tourGuide.model.location.Attraction;
import tourGuide.model.location.VisitedLocation;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.UserModel;
import tourGuide.service.InternalTestService;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.webclient.GpsUtilWebClient;
import tourGuide.webclient.RewardsWebClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class TestPerformance {
	
	/*
	 * A note on performance improvements:
	 *     
	 *     The number of users generated for the high volume tests can be easily adjusted via this method:
	 *     
	 *     		InternalTestHelper.setInternalUserNumber(100000);
	 *     
	 *     
	 *     These tests can be modified to suit new solutions, just as long as the performance metrics
	 *     at the end of the tests remains consistent. 
	 * 
	 *     These are performance metrics that we are trying to hit:
	 *     
	 *     highVolumeTrackLocation: 100,000 users within 15 minutes:
	 *     		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
     *
     *     highVolumeGetRewards: 100,000 users within 20 minutes:
	 *          assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	 */
	

	@Test
	public void highVolumeTrackLocation() throws UUIDException {
		RewardsService rewardsService = new RewardsService();
		InternalTestService internalTestService = new InternalTestService();
		GpsUtilWebClient gpsUtilWebClient = new GpsUtilWebClient();
		InternalTestHelper internalTestHelper = new InternalTestHelper();
		// Users should be incremented up to 100,000, and test finishes within 15 minutes
		internalTestHelper.setInternalUserNumber(100);
		TourGuideService tourGuideService = new TourGuideService(rewardsService, internalTestService);

		tourGuideService.tracker.stopTracking();

		//Create a list of UserModel containing all users
		List<UserModel> allUsers = new ArrayList<>();
		allUsers = tourGuideService.getAllUsers();

		//Create a stopWatch and start it
	    StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		//Create an executor service with a thread pool of certain amount of threads
		try {
		ExecutorService executorService = Executors.newFixedThreadPool(100);

		//Execute the code as per in the method "trackListUserLocations" in TourGuideService
		//but without the calculation of rewards
		for (UserModel user: allUsers) {
			Runnable runnable = () -> {
				VisitedLocation visitedLocation = gpsUtilWebClient.getUserLocationWebClient(user.getUserId());
				user.addToVisitedLocations(visitedLocation);
			};
			executorService.execute(runnable);
		}
			executorService.shutdown();
			executorService.awaitTermination(15, TimeUnit.MINUTES);
		}
		catch (InterruptedException interruptedException) {
		}
		stopWatch.stop();

		//Asserting part that the time is as performant as wanted
		System.out.println("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds."); 
		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));

	}

	@Test
	public void highVolumeGetRewards() {

		RewardsService rewardsService = new RewardsService();

		// Users should be incremented up to 100,000, and test finishes within 20 minutes

		//Create a stopWatch and start it
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		InternalTestService internalTestService = new InternalTestService();
		InternalTestHelper internalTestHelper = new InternalTestHelper();
		internalTestHelper.setInternalUserNumber(10);

		TourGuideService tourGuideService = new TourGuideService(rewardsService, internalTestService);

		GpsUtilWebClient gpsUtilWebClient = new GpsUtilWebClient();
		RewardsWebClient rewardsWebClient = new RewardsWebClient();


		tourGuideService.tracker.stopTracking();

		//Create a list of UserModel containing all users
		List<UserModel> allUsers = new ArrayList<>();
		allUsers = tourGuideService.getAllUsers();

		Attraction attraction = gpsUtilWebClient.getAllAttractionsWebClient().get(0);

		//Create an executor service with a thread pool of certain amount of threads
		try {
		ExecutorService executorService = Executors.newFixedThreadPool(100);

		//Execute the code as per in the method "trackUserLocation" in TourGuideService
		for (UserModel user: allUsers) {
			Runnable runnable = () -> {
				user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
				//tourGuideService.trackUserLocation(user);
				CopyOnWriteArrayList<VisitedLocation> userLocations = new CopyOnWriteArrayList<>();
				List<Attraction> attractions = new CopyOnWriteArrayList<>();

				userLocations.addAll(user.getVisitedLocations());
				attractions.addAll(gpsUtilWebClient.getAllAttractionsWebClient());

				userLocations.forEach(v -> {
					attractions.forEach(a -> {
						UUID attractionId = a.attractionId;
						UUID userId = user.getUserId();
						if (user.getUserRewards().stream().filter(r ->
								r.attraction.attractionName.equals(a.attractionName)).count() == 0) {
							if (rewardsService.nearAttraction(v, a)) {
								user.addUserReward(new UserRewardModel(v, a,
                                            rewardsWebClient.getRewardPointsWebClient(attractionId, userId)));
								}
						}
					});
				});
				assertTrue(user.getUserRewards().size() > 0);
			};
			executorService.execute(runnable);
		}
			executorService.shutdown();
			executorService.awaitTermination(15, TimeUnit.MINUTES);

		}
		catch (InterruptedException interruptedException) {
		}

		stopWatch.stop();

		//Asserting part that the time is as performant as wanted
		System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds."); 
		assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}
}