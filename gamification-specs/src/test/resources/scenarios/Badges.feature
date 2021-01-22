Feature: Basic operations for Badges
  Background:
    Given there is an Gamification server

  Scenario: Create a badge
    Given I have an badge payload
    When I POST the badges payload to the /badges endpoint
    Then I receive a 201 status code
