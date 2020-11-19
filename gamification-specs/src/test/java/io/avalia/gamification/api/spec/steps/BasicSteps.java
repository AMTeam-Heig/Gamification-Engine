package io.avalia.gamification.api.spec.steps;

import io.avalia.gamification.ApiException;
import io.avalia.gamification.ApiResponse;
import io.avalia.gamification.api.DefaultApi;
import io.avalia.gamification.api.dto.Badge;
import io.avalia.gamification.api.dto.Event;
import io.avalia.gamification.api.spec.helpers.Environment;
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

    Event event;
    Badge badge;

    private Badge lastReceivedBadge;
    private Event lastReceivedEvent;

    public BasicSteps(Environment environment) {
        this.environment = environment;
        this.api = environment.getApi();
    }


    @Given("I have a badge payload")
    public void i_have_a_badge_payload() throws Throwable {
        badge = new io.avalia.gamification.api.dto.Badge()
                .level("Master")
                .description("Post 100 answers to questions")
                .obtainedOnDate(LocalDate.now());
    }

    @When("^I POST the badge payload to the /badges endpoint$")
    public void i_POST_the_badge_payload_to_the_badges_endpoint() throws Throwable {
        try {
            lastApiResponse = api.createBadgeWithHttpInfo(badge);
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @When("^I send a GET to the /badges endpoint$")
    public void iSendAGETToTheBadgesEndpoint() {
        try {
            lastApiResponse = api.getBadgesWithHttpInfo();
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @And("I receive a payload that is the same as the badge payload")
    public void iReceiveAPayloadThatIsTheSameAsTheBadgePayload() {
        assertEquals(badge, lastReceivedBadge);
    }

    @Given("I have an event payload")
    public void i_have_an_event_payload() {
        event = new io.avalia.gamification.api.dto.Event().id((long) 1234);
    }

    @When("^I POST the event payload to the /events endpoint$")
    public void i_POST_the_event_payload_to_the_events_endpoint() throws Throwable {
        try {
            lastApiResponse = api.createEventWithHttpInfo(event);
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @When("^I send a GET to the /events endpoint$")
    public void iSendAGETToTheEventsEndpoint() {
        try {
            lastApiResponse = api.getEventsWithHttpInfo();
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @And("I receive a payload that is the same as the event payload")
    public void iReceiveAPayloadThatIsTheSameAsTheEventPayload() {
        assertEquals(event, lastReceivedEvent);
    }

    @And("I receive an event payload that is null")
    public void iReceiveAnEventPayloadThatIsNull() {
        assertNull(lastReceivedEvent);
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
            lastApiResponse = api.getBadgeWithHttpInfo(id);
            processApiResponse(lastApiResponse);
            lastReceivedBadge = (Badge)lastApiResponse.getData();
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @When("I send a GET to the URL in the event location header")
    public void iSendAGETToTheURLInTheEventLocationHeader() {
        Integer id = Integer.parseInt(lastReceivedLocationHeader.substring(lastReceivedLocationHeader.lastIndexOf('/') + 1));
        try {
            lastApiResponse = api.getEventWithHttpInfo(id);
            processApiResponse(lastApiResponse);
            lastReceivedEvent = (Event)lastApiResponse.getData();
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    protected void processApiResponse(ApiResponse apiResponse) {
        lastApiResponse = apiResponse;
        lastApiCallThrewException = false;
        lastApiException = null;
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
