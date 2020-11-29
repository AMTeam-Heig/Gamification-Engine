package ch.heig.amt.gamification.api.spec.steps;

import ch.heig.amt.gamification.ApiException;
import ch.heig.amt.gamification.ApiResponse;
import ch.heig.amt.gamification.api.DefaultApi;
import ch.heig.amt.gamification.api.dto.*;
import ch.heig.amt.gamification.api.spec.helpers.Environment;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BasicSteps {

    private Environment environment;
    private DefaultApi api;

    private NewApplication newApplication;
    private Application application;
    private ApiResponse lastApiResponse;
    private ApiException lastApiException;
    private boolean lastApiCallThrewException;
    private int lastStatusCode;

    private String lastReceivedLocationHeader;

    private String lastReceivedApiKey = "";


    private Event event;

    private String testAppApiKey;


    Badge badge;
    private Badge lastReceivedBadge;


    public BasicSteps(Environment environment) {
        this.environment = environment;
        this.api = environment.getApi();
    }

    @Given("there is a Gamification server")
    public void there_is_a_Gamification_server() throws Throwable {
        assertNotNull(api);
    }

    @Given("I have a Gamification client")
    public void iHaveAGamificationClient() {
        String generatedName = RandomStringUtils.randomAlphanumeric(10);
        NewApplication testApp = new ch.heig.amt.gamification.api.dto.NewApplication().name(generatedName);
        try {
            api.createApplication(testApp);
            testAppApiKey = api.getApplication(generatedName).getApiKey();
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @Then("I receive a {int} status code")
    public void i_receive_a_status_code(int expectedStatusCode) throws Throwable {
        assertEquals(expectedStatusCode, lastStatusCode);
    }


    // ===================================== Application =========================================
    // ==========================================================================================
    @Given("I have an application payload {string}")
    public void i_have_an_application_payload(String name) {
        newApplication = new ch.heig.amt.gamification.api.dto.NewApplication().name(name);
    }

    @When("^I POST the application payload to the /applications endpoint$")
    public void i_POST_the_application_payload_to_the_application_endpoint() throws Throwable {
        try {
            lastApiResponse = api.createApplicationWithHttpInfo(newApplication);
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @When("^I send a GET to the /applications endpoint$")
    public void i_send_a_get_to_the_applications_endpoint() {
        try {
            lastApiResponse = api.getApplicationsWithHttpInfo();
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @And("I see my new application {string} in the list")
    public void iSeeMyNewApplicationInTheList(String name) {
        boolean exists = false;
        ArrayList<Application> list = (ArrayList) lastApiResponse.getData();
        for (Application a : list)
            if (a.getName().equals(name)) {
                exists = true;
                break;
            }
        assertTrue(exists);
    }


    @When("I send a GET to the /applications endpoint with application {string} name")
    public void iSendAGETToTheApplicationsEndpointWithApplicationName(String name) {
        try {
            lastApiResponse = api.getApplicationWithHttpInfo(name);
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @Then("I receive a payload of the application {string}")
    public void i_receive_a_payload_of_the_application(String name) {
        Application responseApplication = (Application) lastApiResponse.getData();
        assertEquals(responseApplication.getName(), name);
    }
    // ==========================================================================================
    // ==========================================================================================

    // ========================================= USER ===========================================
    // ========================================================================================
    @When("I POST the user payload : {string} {int} {string} {int} {string} to the \\/users endpoint")
    public void i_post_the_user_payload_to_the_users_endpoint(String birthdate, Integer points, String reputation, Integer role, String username) {
        User user = new User().birthdate(LocalDate.parse(birthdate))
                .points(points)
                .reputation(reputation)
                .role(role)
                .username(username);

        try {
            lastApiResponse = api.createUserWithHttpInfo(testAppApiKey, user);
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }
    @When("I send a GET to the \\/users endpoint")
    public void iSendAGETToTheUsersEndpoint() {
        try {
            lastApiResponse = api.getUsersWithHttpInfo(testAppApiKey);
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @When("I send a GET to the \\/users endpoint with username {string}")
    public void iSendAGETToTheUsersEndpointWithUsername(String username) {
        try {
            lastApiResponse = api.getUserWithHttpInfo(testAppApiKey, username);
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @And("I receive a payload of user {string}")
    public void iReceiveAPayloadOfUser(String username) {
        // verification seulement du username
        
        User responseUser = (User)lastApiResponse.getData();
        assertEquals(responseUser.getUsername(), username);
    }
    // ==========================================================================================
    // ==========================================================================================
    protected void processApiResponse(ApiResponse apiResponse) {
        lastApiResponse = apiResponse;
        lastApiCallThrewException = false;
        lastApiException = null;
        lastStatusCode = lastApiResponse.getStatusCode();
        List<String> locationHeaderValues = (List<String>) lastApiResponse.getHeaders().get("Location");
        lastReceivedLocationHeader = locationHeaderValues != null ? locationHeaderValues.get(0) : null;
    }

    protected void processApiException(ApiException apiException) {
        lastApiCallThrewException = true;
        lastApiResponse = null;
        lastApiException = apiException;
        lastStatusCode = lastApiException.getCode();
    }

    @Given("I have an event payload with name {string} and {int} number of points for user {string}")
    public void iHaveAnEventPayload(String name, int points, String username) {
        event = new Event().name(name)
                .points(points)
                .username(username);
    }

    @When("I POST the event payload to the \\/events endpoint")
    public void iPOSTTheEventPayloadToTheEventsEndpoint() {
        try {
            lastApiResponse = api.createEventWithHttpInfo("cpicpi", event);
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @When("I send a GET to the \\/events endpoint")
    public void iSendAGETToTheEventsEndpoint() {
    }

    @Given("I have a badge payload")
    public void i_have_a_badge_payload() throws Throwable {
        badge = new ch.heig.amt.gamification.api.dto.Badge()
                .name("Master")
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
























/*



    @And("I receive an event payload that is null")
    public void iReceiveAnApplicationPayloadThatIsNull() {
        assertNull(lastReceivedApplication);
    }

    @And("I receive a payload that has the same name as the application payload")
    public void iReceiveAPayloadThatHasTheSameNameAsTheApplicationPayload() {
        assertEquals(application.getName(), lastReceivedApplication.getName());
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



     */

}