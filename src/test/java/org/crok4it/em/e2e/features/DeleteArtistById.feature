Feature: Delete artist by id

  Scenario: Delete artist with an existing id
    Given I am logged in with the scopes 'artist:deleted'
    When I delete artist with id 'b62af1c8-793a-4b0f-b191-810e96ab0de0'
    Then I should the see that the artist with id 'b62af1c8-793a-4b0f-b191-810e96ab0de0' is no longer in database and success message is 'Artist deleted successfully'

  Scenario: Delete artist with wrong id
    Given I am logged in with the scopes 'artist:deleted'
    Then The attempt to delete an artist with the id '16d3cd12-4ecc-4d81-84e8-5fcc0d975b3e' will fail with status error 'NOT_FOUND' and error code '404'
