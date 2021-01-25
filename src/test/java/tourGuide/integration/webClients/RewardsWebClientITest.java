package tourGuide.integration.webClients;

import org.apache.commons.lang3.math.NumberUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import tourGuide.exception.UUIDException;
import tourGuide.service.TourGuideService;
import tourGuide.webclient.RewardsWebClient;

import java.util.UUID;
import java.util.regex.Pattern;

import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class RewardsWebClientITest {

    @Autowired
    private RewardsWebClient rewardsWebClient;

    @Autowired
    private TourGuideService tourGuideService;

    @Test
    public void getRewardPointsWebClientShouldReturnFieldsWithValues() throws UUIDException {

        UUID attractionId = new UUID(4872158, 1875147);
        UUID userId = new UUID(41872158, 18175147);
        int rewardPoints = rewardsWebClient.getRewardPointsWebClient(attractionId, userId);
        String rewardPointsString = String.valueOf(rewardPoints);

        Assertions.assertThat(rewardPoints)
                .isNotNull();
        Assertions.assertThat(NumberUtils.isNumber(rewardPointsString)).isTrue();
    }

    @Test(expected = UUIDException.class)
    public void getRewardPointsWebClientShouldReturnUuidException() throws UUIDException {
        RewardsWebClient rewardsWebClients = mock(RewardsWebClient.class);
        UUID attractionId = UUID.fromString("1");
        UUID userId = UUID.fromString("1");
        rewardsWebClients.getRewardPointsWebClient(attractionId, userId);
    }
}