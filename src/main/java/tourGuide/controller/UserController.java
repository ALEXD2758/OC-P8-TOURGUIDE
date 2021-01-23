package tourGuide.controller;

import com.jsoniter.output.JsonStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tourGuide.dto.UserPreferencesDTO;
import tourGuide.exception.UserNameNotFoundException;
import tourGuide.exception.UserPreferencesNotFoundException;
import tourGuide.service.TourGuideService;

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