package tourGuide.service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tourGuide.dto.UserPreferencesDTO;
import tourGuide.model.*;
import tourGuide.tracker.Tracker;
import tourGuide.webclient.GpsUtilWebClient;
import tripPricer.Provider;
import tripPricer.TripPricer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class TourGuideService {
	private Logger logger = LoggerFactory.getLogger(TourGuideService.class);
	private final GpsUtil gpsUtil;
	private final RewardsService rewardsService;
	private GpsUtilWebClient gpsUtilWebClient = new GpsUtilWebClient();
	private final InternalTestService internalTestService;
	private final TripPricer tripPricer = new TripPricer();
	public final Tracker tracker;
	boolean testMode = true;
	private final int nbNearestAttractions = 5;

	/**
	 * Constructor of the class TourGuideService for initializing users
	 * if testMode (default value = true) then initializeInternalUsers based on internalUserNumber value in
	 * InternalTestHelper
	 * Initialize Tracker
	 * Ensure that the thread Tracker shuts down by calling addShutDownHook before closing the JVM
	 * @param gpsUtil
	 * @param rewardsService
	 */
	public TourGuideService(GpsUtil gpsUtil, RewardsService rewardsService, InternalTestService internalTestService) {
		this.gpsUtil = gpsUtil;
		this.rewardsService = rewardsService;
		this.internalTestService = internalTestService;
		
		if(testMode) {
			logger.info("TestMode enabled");
			logger.debug("Initializing users");
			internalTestService.initializeInternalUsers();
			logger.debug("Finished initializing users");
		}
		tracker = new Tracker(this, rewardsService);
		addShutDownHook();
	}

	/**
	 * Get a single user from InternalUserMap
	 * @param userName
	 * @return a user
	 */
	public UserModel getUser(String userName) {
		return internalTestService.internalUserMap.get(userName);
	}

	/**
	 * Get a list of all users from the InternalUserMap
	 * @return a list of users
	 */
	public List<UserModel> getAllUsers() {
		return internalTestService.internalUserMap.values().stream().collect(Collectors.toList());
	}

	/**
	 * Get the UserRewards of the concerned user
	 * @param user
	 * @return a list of UserRewards
	 */
	public List<UserRewardModel> getUserRewards(UserModel user) {
		return user.getUserRewards();
	}

	/**
	 * Get the VisitedLocation of the concerned user
	 * If user.getVisitedLocations size is greater than 0 then get the lastVisitedLocation
	 * Else trackUserLocation
	 * @param user
	 * @return a visitedLocation
	 */
	public VisitedLocation getUserVisitedLocation(UserModel user) {
		VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ?
			user.getLastVisitedLocation() :
			trackUserLocation(user);
		return visitedLocation;
	}

	/**
	 * Get a list of Trip Deals in a form of a list of Providers according to the user preferences
	 * @param user the concerned user
	 * @return list of Provider
	 */
	public List<Provider> getTripDeals(UserModel user) {
		int cumulativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();

		List<Provider> providers = tripPricer.getPrice(internalTestService.tripPricerApiKey, user.getUserId(),
				user.getUserPreferences().getNumberOfAdults(),
				user.getUserPreferences().getNumberOfChildren(),
				user.getUserPreferences().getTripDuration(), cumulativeRewardPoints);
		user.setTripDeals(providers);
		return providers;
	}

	/**
	 * Get the UserLocation from GpsUtil, add it to the visitedLocation and calculate the Rewards
	 * @param user
	 * @return the visited location of the random location of user
	 */
	public VisitedLocation trackUserLocation(UserModel user) {
		//VisitedLocation visitedLocation = gpsUtil.getUserLocation(user.getUserId());
		UUID userId = user.getUserId();
		VisitedLocation visitedLocation = gpsUtilWebClient.getUserLocationWebClient(userId);
		user.addToVisitedLocations(visitedLocation);
		rewardsService.calculateRewards(user);
		return visitedLocation;
	}

	/**
	 * Create an ExecutorService thread pool in which a runnable of trackUserLocation is executed
	 * @param userList the list containing all users
	 * @throws InterruptedException
	 */
	public void trackListUserLocation(List<UserModel> userList) throws InterruptedException {
		ExecutorService executorService = Executors.newFixedThreadPool(42);

		for (UserModel user: userList) {
			Runnable runnable = () -> {
				trackUserLocation(user);
			};
			executorService.execute(runnable);
		}
		executorService.shutdown();
		executorService.awaitTermination(15, TimeUnit.MINUTES);

	}

	/**
	 * Get a list of all user Locations from the existent GpsUtil list (tracking locations every X second)
	 * @return a list of UserLocationModel containing all user ID's and location
	 */
	public List<UserLocationModel> getAllUsersLocation() {
		List<UserModel> userList = getAllUsers();
		List<UserLocationModel> userLocationList = new ArrayList<>();

		userList.forEach(u -> {
			userLocationList.add(new UserLocationModel(u.getLastVisitedLocation().location, u.getUserId().toString()));
		});

		return userLocationList;
	}

	/**
	 * Get the closest 5 attractions of the user
	 * @param visitedLocation
	 * @return a list of attractions
	 */
	public List<UserNearestAttractionsModel> getNearestAttractions(VisitedLocation visitedLocation, UserModel user) {

		ExecutorService executorService = Executors.newFixedThreadPool(32);
		List<Attraction> attractions = gpsUtilWebClient.getAllAttractionsWebClient();
		List<UserNearestAttractionsModel> nearestAttractions = new ArrayList<>();

		List<Future> futuresList = new ArrayList<>();
		for (Attraction attraction : attractions) {
			Callable changeUserNearest = () -> new UserNearestAttractionsModel(attraction.attractionName,
					attraction.longitude, attraction.latitude,
					visitedLocation.location, rewardsService.getDistance(attraction, visitedLocation.location),
					rewardsService.getRewardPoints(attraction, user));
			Future mapUserNearestAttractions = executorService.submit(changeUserNearest);
			futuresList.add(mapUserNearestAttractions);
		};

		for (Future future: futuresList) {
			UserNearestAttractionsModel at = null;
			try {
				at = (UserNearestAttractionsModel) future.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			nearestAttractions.add(at);
		}

		List<UserNearestAttractionsModel> listAttractionsSorted = nearestAttractions
				.stream()
				.sorted(Comparator.comparing(UserNearestAttractionsModel::getAttractionProximityRangeMiles)).limit(nbNearestAttractions)
				.collect(Collectors.toList());

		return listAttractionsSorted;
	}

	/**
	 * Add a shut down hook for stopping the Tracker thread before shutting down the JVM
	 */
	private void addShutDownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() { 
		      public void run() {
		        tracker.stopTracking();
		      } 
		    }); 
	}

	/**
	 * Sets the user preferences with the new user preferences from UserPreferencesDTO
	 *
	 * @param userPreferencesDTO
	 * @return
	 */
	public UserPreferencesModel userUpdatePreferences (UserPreferencesDTO userPreferencesDTO) {
		UserModel user= getUser(userPreferencesDTO.getUsername());
		user.setUserPreferences(new UserPreferencesModel(userPreferencesDTO));
		return user.getUserPreferences();
	}
}