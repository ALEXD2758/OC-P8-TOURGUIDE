package tourGuide.controller;

import com.jsoniter.output.JsonStream;
import gpsUtil.location.VisitedLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tourGuide.dto.UserPreferencesDTO;
import tourGuide.exception.UserNameNotFoundException;
import tourGuide.exception.UserPreferencesNotFoundException;
import tourGuide.model.UserModel;
import tourGuide.service.TourGuideService;
import tripPricer.Provider;

import java.util.List;

@RestController
public class UserController {

	@Autowired
	TourGuideService tourGuideService;

    /** HTML PUT request that updates the trip preferences of a specific user
     *
     * @param userPreferencesDTO the RequestBody of UserPreferencesDTO containing the new preferences
     * @return a string of UserPreferences for the specific user
     */
    @PutMapping("/update/Preferences")
    public String updatePreferences(@RequestBody UserPreferencesDTO userPreferencesDTO) throws UserNameNotFoundException {
        if(userPreferencesDTO == null){
            throw new UserPreferencesNotFoundException();
        }
        return JsonStream.serialize(new UserPreferencesDTO(userPreferencesDTO.getUsername(),
                tourGuideService.userUpdatePreferences(userPreferencesDTO)));
    }
}