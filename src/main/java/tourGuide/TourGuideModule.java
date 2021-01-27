package tourGuide;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//import rewardCentral.RewardCentral;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;
import tourGuide.service.RewardsService;
import tourGuide.webclient.GpsUtilWebClient;
import tourGuide.webclient.RewardsWebClient;
import tourGuide.webclient.TripPricerWebClient;

@Configuration
public class TourGuideModule {
	//ASK TO JULIAN THIS:
//	@Bean
//	public GpsUtil getGpsUtil() {
//		return new GpsUtil();
//	}

    @Bean
    @Profile({"prod", "test"})
    public GpsUtilWebClient gpsUtilWebClient() {
        return new GpsUtilWebClient();
    }

    @Bean
    @Profile({"prod", "test"})
    public RewardsWebClient rewardsWebClient() {
        return new RewardsWebClient();
    }

    @Bean
    @Profile({"prod", "test"})
    public TripPricerWebClient tripPricerWebClient() {
        return new TripPricerWebClient();
    }
//	@Bean
//	public RewardsService getRewardsService() {
//		return new RewardsService(getRewardCentral());
//	}
	
//	@Bean
//	public RewardCentral getRewardCentral() {
//		return new RewardCentral();
//	}
}