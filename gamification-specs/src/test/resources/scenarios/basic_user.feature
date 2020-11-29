Feature: Users

  Background:
    Given there is a Gamification server
    Given I have a Gamification client

  Scenario: create a user
    When I POST the user payload : "1995-08-11" 0 "noob" 0 "zoubaidas" to the /users endpoint
    Then I receive a 201 status code
    When I send a GET to the /users endpoint with username 'zoubaidas'
    Then I receive a 200 status code
    And I receive a payload of user 'zoubaidas'

  Scenario: get the users
    When I send a GET to the /users endpoint
    Then I receive a 200 status code

