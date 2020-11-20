package ch.heig.amt.gamification.api.spec.steps;

import ch.heig.amt.gamification.ApiException;
import ch.heig.amt.gamification.ApiResponse;
import ch.heig.amt.gamification.api.DefaultApi;
import ch.heig.amt.gamification.api.dto.Application;
import ch.heig.amt.gamification.api.dto.Badge;
import ch.heig.amt.gamification.api.dto.InlineObject;
import ch.heig.amt.gamification.api.spec.helpers.Environment;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class BasicSteps {

    private Environment environment;
    private DefaultApi api;

    private ApiResponse lastApiResponse;
    private ApiException lastApiException;
    private boolean lastApiCallThrewException;
    private int lastStatusCode;

    private String lastReceivedLocationHeader;

    private String lastReceivedApiKey = "";

    Badge badge;
    InlineObject application;

    private Badge lastReceivedBadge;
    private Application lastReceivedApplication;

    public BasicSteps(Environment environment) {
        this.environment = environment;
        this.api = environment.getApi();
    }


    @Given("I have a badge payload")
    public void i_have_a_badge_payload() throws Throwable {
        badge = new ch.heig.amt.gamification.api.dto.Badge()
                .level("Master")
                .description("Post 100 answers to questions")
                .obtainedOnDate(LocalDate.now());
    }

    @When("^I POST the badge payload to the /badges endpoint$")
    public void i_POST_the_badge_payload_to_the_badges_endpoint() throws Throwable {
        try {
            lastApiResponse = api.createBadgeWithHttpInfo(lastReceivedApiKey, badge);
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @When("^I send a GET to the /badges endpoint$")
    public void iSendAGETToTheBadgesEndpoint() {
        try {
            lastApiResponse = api.getBadgesWithHttpInfo(lastReceivedApiKey);
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @And("I receive a payload that is the same as the badge payload")
    public void iReceiveAPayloadThatIsTheSameAsTheBadgePayload() {
        assertEquals(badge, lastReceivedBadge);
    }

    @Given("I have an application payload")
    public void i_have_an_application_payload() {
        application = new ch.heig.amt.gamification.api.dto.InlineObject().name("New Application");
    }

    @When("^I POST the application payload to the /application endpoint$")
    public void i_POST_the_application_payload_to_the_application_endpoint() throws Throwable {
        try {
            lastApiResponse = api.newApplicationWithHttpInfo(application);
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @When("^I send a GET to the /application endpoint$")
    public void iSendAGETToTheApplicationEndpoint() {
        try {
            lastApiResponse = api.getApplicationsWithHttpInfo(lastReceivedApiKey);
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @And("I receive a payload that is the same as the event payload")
    public void iReceiveAPayloadThatIsTheSameAsTheApplicationPayload() {
        assertEquals(application, lastReceivedApplication);
    }

    @And("I receive an event payload that is null")
    public void iReceiveAnApplicationPayloadThatIsNull() {
        assertNull(lastReceivedApplication);
    }

    @And("I receive a payload that has the same name as the application payload")
    public void iReceiveAPayloadThatHasTheSameNameAsTheApplicationPayload() {
        assertEquals(application.getName(), lastReceivedApplication.getName());
    }

    @Given("there is a Gamification server")
    public void there_is_a_Gamification_server() throws Throwable {
        assertNotNull(api);
    }

    @Then("I receive a {int} status code")
    public void i_receive_a_status_code(int expectedStatusCode) throws Throwable {
        assertEquals(expectedStatusCode, lastStatusCode);
    }

    @Then("I receive a {int} status code with a location header")
    public void iReceiveAStatusCodeWithALocationHeader(int arg0) {
    }

    @When("I send a GET to the URL in the badge location header")
    public void iSendAGETToTheURLInTheBadgeLocationHeader() {
        Integer id = Integer.parseInt(lastReceivedLocationHeader.substring(lastReceivedLocationHeader.lastIndexOf('/') + 1));
        try {
            lastApiResponse = api.getBadgeWithHttpInfo(lastReceivedApiKey, id);
            processApiResponse(lastApiResponse);
            lastReceivedBadge = (Badge)lastApiResponse.getData();
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @And("I receive a payload that has an api key")
    public void iReceiveAPayloadThatHasAnApiKey() {
    }

    @When("I send a GET to the URL in the application location header")
    public void iSendAGETToTheURLInTheApplicationLocationHeader() {
        Integer id = Integer.parseInt(lastReceivedLocationHeader.substring(lastReceivedLocationHeader.lastIndexOf('/') + 1));
        try {
            lastApiResponse = api.getApplicationsWithHttpInfo(lastReceivedApiKey);
            processApiResponse(lastApiResponse);
            lastReceivedApplication = (Application) lastApiResponse.getData();
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    protected void processApiResponse(ApiResponse apiResponse) {
        lastApiResponse = apiResponse;
        lastApiCallThrewException = false;
        lastApiException = null;
        lastReceivedApiKey = (String) apiResponse.getHeaders().get("apiKey");
        lastStatusCode = lastApiResponse.getStatusCode();
        List<String> locationHeaderValues = (List<String>)lastApiResponse.getHeaders().get("Location");
        lastReceivedLocationHeader = locationHeaderValues != null ? locationHeaderValues.get(0) : null;
    }

    protected void processApiException(ApiException apiException) {
        lastApiCallThrewException = true;
        lastApiResponse = null;
        lastApiException = apiException;
        lastStatusCode = lastApiException.getCode();
    }

}
