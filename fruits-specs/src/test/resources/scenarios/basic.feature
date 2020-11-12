Feature: Basic operations on fruits

  Background:
    Given there is a Fruits server

  Scenario: create a fruit
    Given I have a fruit payload
    When I POST the fruit payload to the /fruits endpoint
    Then I receive a 201 status code

  Scenario: get the list of fruits
    When I send a GET to the /fruits endpoint
    Then I receive a 200 status code