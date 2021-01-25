package tourGuide.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.UserModel;
import tourGuide.model.location.Location;
import tourGuide.model.location.VisitedLocation;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.IntStream;

@Service
public class InternalTestService {
	private Logger logger = LoggerFactory.getLogger(InternalTestService.class);

	/**********************************************************************************
	 * 
	 * Methods Below: For Internal Testing
	 * 
	 **********************************************************************************/
	public static final String tripPricerApiKey = "test-server-api-key";
	// Database connection will be used for external users, but for testing purposes internal users are provided and stored in memory
	public final Map<String, UserModel> internalUserMap = new HashMap<>();

	public void initializeInternalUsers() {
		IntStream.range(0, InternalTestHelper.getInternalUserNumber()).forEach(i -> {
			String userName = "internalUser" + i;
			String phone = "000";
			String email = userName + "@tourGuide.com";
			UserModel user = new UserModel(UUID.randomUUID(), userName, phone, email);
			generateUserLocationHistory(user);

			addUser(user);
			internalUserMap.put(userName, user);
		});
		logger.debug("Created " + InternalTestHelper.getInternalUserNumber() + " internal test users.");
	}

	/**
	 * Add a user to the InternalUserMap if does not contain already the userName
	 * @param user
	 */
	public void addUser(UserModel user) {
		if (!internalUserMap.containsKey(user.getUserName())) {
			internalUserMap.put(user.getUserName(), user);
		}
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

	/**
	 * Check if the InternalUserMap contains already the userName
	 *
	 * @param userName the string of the username
	 * @return the boolean of the check
	 */
	public boolean checkIfUserNameExists(String userName) {
		return internalUserMap.containsKey(userName) ? true : false;
	}
}