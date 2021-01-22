package ch.heig.amt.gamification.api.spec.steps;


import ch.heig.amt.gamification.ApiException;
import ch.heig.amt.gamification.ApiResponse;
import ch.heig.amt.gamification.api.DefaultApi;
import ch.heig.amt.gamification.api.dto.Application;
import ch.heig.amt.gamification.api.dto.NewApplication;
import ch.heig.amt.gamification.api.spec.helpers.BufferData;
import ch.heig.amt.gamification.api.spec.helpers.Environment;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.nio.charset.Charset;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BasicSteps {

    private Environment environment;
    private DefaultApi api;
    private NewApplication application;
    private BufferData buffer;

    public BasicSteps(Environment environment,BufferData buffer) {
        this.environment = environment;
        this.api = environment.getApi();
        this.buffer=buffer;
    }

    @Given("there is an Gamification server")
    public void there_is_an_gamification_server() {
        assertNotNull(api);
    }
    @Given("I have an application payload")
    public void i_have_an_application_payload()  {
        byte[] array = new byte[50]; // length is bounded by 7
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));
        application = new NewApplication()
                .name(generatedString);
    }

    @Given("^I have an application payload with (\\S+)$")
    public void i_have_an_application_payload_with(String nameApp) {
        application = new NewApplication()
                .name(nameApp);
    }

    @Given("^I have an exist application payload")
    public void i_have_an_exist_application_payload() throws ApiException {
        buffer.setTempApp(new NewApplication().name("AMTeamS"));
        environment.setLastApiResponse(api.createApplicationWithHttpInfo(buffer.getTempApp()));
    }

    @When("^I POST the application payload to the \\/applications endpoint")
    public void i_post_the_application_payload_to_the_applications_endpoint(){
        try {
            ApiResponse ap=api.createApplicationWithHttpInfo(application);
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
    @When("^I GET the application payload to the /applications/(\\S+) endpoint$")
    public void i_get_the_application_payload_to_the_applications_endpoint(String url) {
        try {

            ApiResponse appp=api.getApplicationWithHttpInfo(url);
            environment.setLastApiResponse(appp);
            BufferData.setApp((Application) environment.getLastApiResponse().getData());
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
    @Then("^I receive a (\\d+) status code$")
    public void i_receive_a_status_code(int code){
        assertEquals(code,environment.getLastStatusCode());
    }
}