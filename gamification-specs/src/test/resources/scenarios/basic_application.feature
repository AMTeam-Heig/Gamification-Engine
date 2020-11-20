Feature: Basic operations on Applications

  Background:
    Given there is a Gamification server

  Scenario: create an application
    Given I have an application payload
    When I POST the application payload to the /applications endpoint
    Then I receive a 201 status code
    And I receive a payload that has an api key
    And I receive a payload that has the same name as the application payload

  Scenario: get the list of applications
    When I send a GET to the /applications endpoint
    Then I receive a 200 status code
