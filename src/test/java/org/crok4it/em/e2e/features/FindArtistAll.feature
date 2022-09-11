Feature: Find all artist

  Scenario: Find all artist
    Given I am logged in with the scopes 'artist:read'
    Then The attempt to fetch all artist from database will return a list of artist
