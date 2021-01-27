package tourGuide.service;

import org.junit.Assert;
import org.junit.Test;
import tourGuide.dto.UserPreferencesDTO;
import tourGuide.exception.UUIDException;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.UserLocationModel;
import tourGuide.model.UserModel;
import tourGuide.model.UserNearestAttractionsModel;
import tourGuide.model.UserPreferencesModel;
import tourGuide.model.location.VisitedLocation;
import tourGuide.model.trip.Provider;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestTourGuideService {

	@Test
	public void getUserLocation() throws UUIDException {
		RewardsService rewardsService = new RewardsService();
		InternalTestHelper.setInternalUserNumber(0);
		InternalTestService internalTestService = new InternalTestService();
		TourGuideService tourGuideService = new TourGuideService(rewardsService, internalTestService);
		
		UserModel user = new UserModel(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);
		tourGuideService.tracker.stopTracking();
		assertTrue(visitedLocation.userId.equals(user.getUserId()));
	}
	
	@Test
	public void addUser() {
		RewardsService rewardsService = new RewardsService();
		InternalTestHelper.setInternalUserNumber(0);

		InternalTestService internalTestService = new InternalTestService();
		TourGuideService tourGuideService = new TourGuideService(rewardsService, internalTestService);

		UserModel user = new UserModel(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		UserModel user2 = new UserModel(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

		internalTestService.addUser(user);
		internalTestService.addUser(user2);
		
		UserModel retrievedUser = tourGuideService.getUser(user.getUserName());
		UserModel retrievedUser2 = tourGuideService.getUser(user2.getUserName());

		tourGuideService.tracker.stopTracking();
		
		assertEquals(user, retrievedUser);
		assertEquals(user2, retrievedUser2);
	}
	
	@Test
	public void getAllUsers() {
		RewardsService rewardsService = new RewardsService();
		InternalTestHelper.setInternalUserNumber(0);

		InternalTestService internalTestService = new InternalTestService();
		TourGuideService tourGuideService = new TourGuideService( rewardsService, internalTestService);

		UserModel user = new UserModel(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		UserModel user2 = new UserModel(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

		internalTestService.addUser(user);
		internalTestService.addUser(user2);
		
		List<UserModel> allUsers = tourGuideService.getAllUsers();

		tourGuideService.tracker.stopTracking();
		
		assertTrue(allUsers.contains(user));
		assertTrue(allUsers.contains(user2));
	}
	
	@Test
	public void trackUser() throws UUIDException {
		RewardsService rewardsService = new RewardsService();
		InternalTestHelper.setInternalUserNumber(0);

		InternalTestService internalTestService = new InternalTestService();
		TourGuideService tourGuideService = new TourGuideService(rewardsService, internalTestService);
		
		UserModel user = new UserModel(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);
		
		tourGuideService.tracker.stopTracking();
		
		assertEquals(user.getUserId(), visitedLocation.userId);
	}

	@Test
	public void getNearestAttractions() throws UUIDException {
		RewardsService rewardsService = new RewardsService();
		InternalTestHelper.setInternalUserNumber(1);

		InternalTestService internalTestService = new InternalTestService();
		TourGuideService tourGuideService = new TourGuideService(rewardsService, internalTestService);
		
		UserModel user = new UserModel(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);
		
		List<UserNearestAttractionsModel> attractions = tourGuideService.getNearestAttractions(visitedLocation, user);
		
		tourGuideService.tracker.stopTracking();
		
		assertEquals(5, attractions.size());
	}

	@Test
	public void getAllUserLocations() {
		RewardsService rewardsService = new RewardsService();
		InternalTestHelper.setInternalUserNumber(5);

		InternalTestService internalTestService = new InternalTestService();
		TourGuideService tourGuideService = new TourGuideService(rewardsService, internalTestService);

		List<UserLocationModel> listUserLocation = tourGuideService.getAllUsersLocation();

		tourGuideService.tracker.stopTracking();

		assertEquals(5, listUserLocation.size());
	}

	@Test
	public void getTripDeals() {
		RewardsService rewardsService = new RewardsService();
		InternalTestHelper.setInternalUserNumber(0);

		InternalTestService internalTestService = new InternalTestService();
		TourGuideService tourGuideService = new TourGuideService(rewardsService, internalTestService);
		
		UserModel user = new UserModel(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

		List<Provider> providers = tourGuideService.getTripDeals(user);
		
		tourGuideService.tracker.stopTracking();
		
		assertEquals(5, providers.size());
	}

	@Test
	public void userUpdatePreferences () {
		UserPreferencesDTO userPreferencesDTO = new UserPreferencesDTO();
		userPreferencesDTO.setNumberOfAdults(2);
		userPreferencesDTO.setTripDuration(3);
		userPreferencesDTO.setCurrency("USD");

		UUID userUUID = UUID.fromString("987b1312-768d-41e1-90c1-e62da7c93739");
		UserModel userModel = new UserModel(userUUID, "internalUser2", "1243456",
				"internalUser2@Gmail.com");
		Assert.assertEquals(userModel.getUserPreferences().getNumberOfAdults(), 1);
		Assert.assertNotEquals(userModel.getUserPreferences().getNumberOfAdults(),
				userPreferencesDTO.getNumberOfAdults());
		//UserModel user= getUser(userPreferencesDTO.getUsername());
		userModel.setUserPreferences(new UserPreferencesModel(userPreferencesDTO));
		Assert.assertEquals(userModel.getUserPreferences().getNumberOfAdults(),
				userPreferencesDTO.getNumberOfAdults());
	}
}