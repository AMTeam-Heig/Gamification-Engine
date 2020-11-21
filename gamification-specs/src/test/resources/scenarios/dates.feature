Feature: Validation of date formats

  Background:
    Given there is a Gamification server

  Scenario: the badge is created correctly
    Given I have a badge payload
    When I POST the badge payload to the /badges endpoint
    Then I receive a 201 status code with a location header
    When I send a GET to the URL in the badge location header
    Then I receive a 200 status code
    And I receive a payload that is the same as the badge payload

#  Scenario: the event is created correctly
#    Given I have an event payload
#    When I POST the event payload to the /events endpoint
#    Then I receive a 201 status code with a location header
#    When I send a GET to the URL in the event location header
#    Then I receive a 404 status code
#    And I receive an event payload that is null

