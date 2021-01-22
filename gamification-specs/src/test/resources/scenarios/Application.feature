
Feature: Basic operations for applications
  Background:
    Given there is an Gamification server

  Scenario: create an application
    Given I have an application payload
    When I POST the application payload to the /applications endpoint
    Then I receive a 201 status code

  Scenario: create an existing  application
    Given I have an application payload with thisAppExist
    When I POST the application payload to the /applications endpoint
    Given I have an application payload with thisAppExist
    When I POST the application payload to the /applications endpoint
    Then I receive a 403 status code