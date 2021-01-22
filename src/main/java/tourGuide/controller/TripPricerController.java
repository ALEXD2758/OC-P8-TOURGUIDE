package tourGuide.controller;

import com.jsoniter.output.JsonStream;
import gpsUtil.location.VisitedLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tourGuide.dto.UserPreferencesDTO;
import tourGuide.exception.UserNameNotFoundException;
import tourGuide.exception.UserPreferencesNotFoundException;
import tourGuide.model.UserModel;
import tourGuide.service.InternalTestService;
import tourGuide.service.TourGuideService;
import tripPricer.Provider;

import java.util.List;

@RestController
public class TripPricerController {

	@Autowired
	TourGuideService tourGuideService;

    @Autowired
    InternalTestService internalTestService;

    /** HTML GET request that returns 5 random trip deals of the username bounded to the request
     *
     * @param userName string of the username (internalUserX)
     * @return a string of a list of Provider in a random way
     */
    @GetMapping("/getTripDeals")
    public String getTripDeals(@RequestParam String userName) {
        if(!internalTestService.checkIfUserNameExists(userName)) {
            throw new UserNameNotFoundException(userName);
        }

    	List<Provider> providers = tourGuideService.getTripDeals(tourGuideService.getUser(userName));
    	return JsonStream.serialize(providers);
    }
}