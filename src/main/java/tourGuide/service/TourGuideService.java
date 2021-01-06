package tourGuide.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import tourGuide.dto.UserPreferencesDTO;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.*;
import tourGuide.tracker.Tracker;
import tripPricer.Provider;
import tripPricer.TripPricer;

@Service
public class TourGuideService {
	private Logger logger = LoggerFactory.getLogger(TourGuideService.class);
	private final GpsUtil gpsUtil;
	private final RewardsService rewardsService;
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
	public TourGuideService(GpsUtil gpsUtil, RewardsService rewardsService) {
		this.gpsUtil = gpsUtil;
		this.rewardsService = rewardsService;
		
		if(testMode) {
			logger.info("TestMode enabled");
			logger.debug("Initializing users");
			initializeInternalUsers();
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
		return internalUserMap.get(userName);
	}

	/**
	 * Get a list of all users from the InternalUserMap
	 * @return a list of users
	 */
	public List<UserModel> getAllUsers() {
		return internalUserMap.values().stream().collect(Collectors.toList());
	}

	/**
	 * Add a user to the InternalUserMap if does not contain already the userName
	 * @param user
	 */
	public void addUser(UserModel user) {
		if(!internalUserMap.containsKey(user.getUserName())) {
			internalUserMap.put(user.getUserName(), user);
		}
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
	 * If user.getVisitedLocations size is greather than 0 then get the lastVisitedLocation
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

		List<Provider> providers = tripPricer.getPrice(tripPricerApiKey, user.getUserId(),
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
		VisitedLocation visitedLocation = gpsUtil.getUserLocation(user.getUserId());
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

		return;
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
		List<Attraction> attractions = gpsUtil.getAttractions();
		List<UserNearestAttractionsModel> nearestAttractions = new ArrayList<>();

		StopWatch stopWatch = new StopWatch();

		stopWatch.start();
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
		stopWatch.stop();
		System.out.println("It required : " + stopWatch.getTime() + " milliseconds");

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

	public UserPreferencesModel userUpdatePreferences (UserPreferencesDTO userPreferencesDTO) {
		UserModel user= getUser(userPreferencesDTO.getUsername());
		user.setUserPreferences(new UserPreferencesModel(userPreferencesDTO));
		return user.getUserPreferences();
	}
	
	/**********************************************************************************
	 * 
	 * Methods Below: For Internal Testing
	 * 
	 **********************************************************************************/
	private static final String tripPricerApiKey = "test-server-api-key";
	// Database connection will be used for external users, but for testing purposes internal users are provided and stored in memory
	private final Map<String, UserModel> internalUserMap = new HashMap<>();
	private void initializeInternalUsers() {
		IntStream.range(0, InternalTestHelper.getInternalUserNumber()).forEach(i -> {
			String userName = "internalUser" + i;
			String phone = "000";
			String email = userName + "@tourGuide.com";
			UserModel user = new UserModel(UUID.randomUUID(), userName, phone, email);
			generateUserLocationHistory(user);
			
			internalUserMap.put(userName, user);
		});
		logger.debug("Created " + InternalTestHelper.getInternalUserNumber() + " internal test users.");
	}

	/**
	 * Generate a user location history of 3 visited locations for the current user
	 * @param user
	 */
	private void generateUserLocationHistory(UserModel user) {
		IntStream.range(0, 3).forEach(i-> {
			user.addToVisitedLocations(new VisitedLocation(user.getUserId(), new Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
		});
	}

	/**
	 * Generate a random Longitude
	 * @return double of longitude
	 */
	private double generateRandomLongitude() {
		double leftLimit = -180;
	    double rightLimit = 180;
	    return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}
	/**
	 * Generate a random latitude
	 * @return double of latitude
	 */
	private double generateRandomLatitude() {
		double leftLimit = -85.05112878;
	    double rightLimit = 85.05112878;
	    return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	/**
	 * Generate a random LocalDateTime with java.time, in UTC time
	 * @return Date of a random time
	 */
	private Date getRandomTime() {
		LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
	    return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
	}
}