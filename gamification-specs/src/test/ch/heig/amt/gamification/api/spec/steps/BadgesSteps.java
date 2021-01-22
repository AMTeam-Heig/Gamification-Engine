package ch.heig.amt.gamification.api.spec.steps;


import ch.heig.amt.gamification.ApiException;
import ch.heig.amt.gamification.ApiResponse;
import ch.heig.amt.gamification.api.DefaultApi;
import ch.heig.amt.gamification.api.dto.Badge;
import ch.heig.amt.gamification.api.spec.helpers.BufferData;
import ch.heig.amt.gamification.api.spec.helpers.Environment;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
public class BadgesSteps{

    private Environment environment;
    private DefaultApi api;
    private Badge badge;
    private BufferData buffer;
    public BadgesSteps(Environment environment,BufferData buffer) {
        this.environment = environment;
        this.api = environment.getApi();
        this.buffer=buffer;
    }

    @Given("I have an badge payload")
    public void i_have_a_badge_payload() {
      badge = new Badge()
              .description("If you get this badge your are a noob")
              .name("Noobito")
              .obtainedOnDate(LocalDate.now());
    }

    @When("^I POST the badges payload to the \\/badges endpoint$")
    public void i_post_the_badge_payload_to_the_applications_endpoint(){
        try {
            ApiResponse ap=api.createBadgeWithHttpInfo(BufferData.getApp().getApiKey(),badge);
            environment.setLastApiResponse(ap);
            environment.setLastApiCallThrewException(false);
            environment.setLastApiException(null);
            environment.setLastStatusCode(environment.getLastApiResponse().getStatusCode());
        } catch (ApiException e) {
            environment.setLastApiCallThrewException(true);
            environment.setLastApiResponse(null);
            environment.setLastApiException(e);
            environment.setLastStatusCode(environment.getLastApiException().getCode());
        }
    }

}