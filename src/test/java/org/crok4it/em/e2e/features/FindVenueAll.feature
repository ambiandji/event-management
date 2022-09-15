Feature: Find all venue

  Scenario: Find all venue
    Given I am logged in with the scopes 'venue:read'
    Then The attempt to fetch all venue from database will return a list of venue
