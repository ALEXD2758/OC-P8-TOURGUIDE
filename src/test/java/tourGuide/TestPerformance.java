package tourGuide;

import gpsUtil.GpsUtil;
import gpsUtil.location.VisitedLocation;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;
import rewardCentral.RewardCentral;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.UserModel;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;

import java.util.ArrayList;
import java.util.List;
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
	public void highVolumeTrackLocation() {
		GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		// Users should be incremented up to 100,000, and test finishes within 15 minutes
		InternalTestHelper.setInternalUserNumber(100);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);
		tourGuideService.tracker.stopTracking();

		//Create a list of UserModel containing all users
		List<UserModel> allUsers = new ArrayList<>();
		allUsers = tourGuideService.getAllUsers();

		//Create a stopWatch and start it
	    StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		//Create an executor service with a thread pool of certain amount of threads
		try {
		ExecutorService executorService = Executors.newFixedThreadPool(42);

		//Execute the code as per in the method "trackListUserLocations" in TourGuideService
		//but without the calculation of rewards
		for (UserModel user: allUsers) {
			Runnable runnable = () -> {
				VisitedLocation visitedLocation = gpsUtil.getUserLocation(user.getUserId());
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
		assertTrue(TimeUnit.MINUTES.toMinutes(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));

	}

	@Test
	public void highVolumeGetRewards() {
		GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());

		// Users should be incremented up to 100,000, and test finishes within 20 minutes
		InternalTestHelper.setInternalUserNumber(100);
		//Create a stopWatch and start it
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);
		tourGuideService.tracker.stopTracking();

		//Create a list of UserModel containing all users
		List<UserModel> allUsers = new ArrayList<>();
		allUsers = tourGuideService.getAllUsers();

		//Create an executor service with a thread pool of certain amount of threads
		try {
		ExecutorService executorService = Executors.newFixedThreadPool(42);

		//Execute the code as per in the method "trackUserLocation" in TourGuideService
		for (UserModel user: allUsers) {
			Runnable runnable = () -> {
				tourGuideService.trackUserLocation(user);
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
		assertTrue(TimeUnit.MINUTES.toMinutes(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}
}