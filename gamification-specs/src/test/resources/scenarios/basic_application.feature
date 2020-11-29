Feature: Basic operations on Applications

  Background:
    Given there is a Gamification server

  Scenario: create an application
    Given I have an application payload 'New Application'
    When I POST the application payload to the /applications endpoint
    Then I receive a 201 status code


  Scenario: create the same application
    Given I have an application payload 'New Application'
    When I POST the application payload to the /applications endpoint
    Then I receive a 403 status code


  Scenario: get the applications
    When I send a GET to the /applications endpoint
    Then I receive a 200 status code
    And I see my new application "New Application" in the list

  Scenario: get the application by name
    When I send a GET to the /applications endpoint with application 'New Application' name
    Then I receive a 200 status code
    And I receive a payload of the application 'New Application'