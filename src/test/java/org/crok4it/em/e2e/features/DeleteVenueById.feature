Feature: Delete venue by id

  Scenario: Delete venue with an existing id
    Given I am logged in with the scopes 'venue:deleted'
    When I delete venue with id 'b62af1c8-793a-4b0f-b191-810e96ab0de0'
    Then I should the see that the venue with id 'b62af1c8-793a-4b0f-b191-810e96ab0de0' is no longer in database and success message is 'Venue deleted successfully'

  Scenario: Delete venue with wrong id
    Given I am logged in with the scopes 'venue:deleted'
    Then The attempt to delete an venue with the id '16d3cd12-4ecc-4d81-84e8-5fcc0d975b3e' will fail with status error 'NOT_FOUND' and error code '404'
