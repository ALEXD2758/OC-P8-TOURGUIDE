package tourGuide.controller;

import com.jsoniter.output.JsonStream;
import tourGuide.model.location.VisitedLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tourGuide.exception.UserNameNotFoundException;
import tourGuide.model.UserModel;
import tourGuide.service.InternalTestService;
import tourGuide.service.TourGuideService;

@RestController
public class LocationController {

	@Autowired
	TourGuideService tourGuideService;

    @Autowired
    InternalTestService internalTestService;

    /** HTML GET request that returns a random location of the username bounded to the request
     *
     * @param userName string of the username (internalUserX)
     * @return a Json string of a user location in longitude and latitude
     */
    @GetMapping("/getLocation")
    public String getLocation(@RequestParam String userName) throws UserNameNotFoundException {
        if(!internalTestService.checkIfUserNameExists(userName)) {
            throw new UserNameNotFoundException(userName);
        }
         VisitedLocation visitedLocation =
                    tourGuideService.getUserVisitedLocation(tourGuideService.getUser(userName));

        return JsonStream.serialize(visitedLocation.location);
    }

    /** HTML GET request that returns the 5 closest attractions of the username bounded to the request
     *
     * @param userName string of the username (internalUserX)
     * @return a Json string of nearby attractions.
     */
    @GetMapping("/getNearbyAttractions")
    public String getNearbyAttractions(@RequestParam String userName) {
        if(!internalTestService.checkIfUserNameExists(userName)) {
            throw new UserNameNotFoundException(userName);
        }

        UserModel user = tourGuideService.getUser(userName);
    	VisitedLocation visitedLocation = tourGuideService.getUserVisitedLocation(user);
    	return JsonStream.serialize(tourGuideService.getNearestAttractions(visitedLocation, user));
    }

    /** HTML GET request that returns the current location of all users
     *
     * @return a Json string of current location of all users.
     */
    @GetMapping("/getAllCurrentLocations")
    public String getAllCurrentLocations() {
    	return JsonStream.serialize(tourGuideService.getAllUsersLocation());
    }
}