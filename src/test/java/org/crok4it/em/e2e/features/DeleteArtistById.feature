Feature: Delete artist by id

  Scenario: Delete Artist with an existing id
    Given I am logged in with the scopes 'artist:read'
    When I delete artist with id 'd7a4c906-4efe-42eb-808e-a772aa8da6e7'
    Then I should the see that the artist with id 'd7a4c906-4efe-42eb-808e-a772aa8da6e7' is no longer in database and success message is 'Artiste deleted successfully'

  Scenario: Find Artist with wrong id
    Given I am logged in with the scopes 'artist:read'
    Then The attempt to delete an artist with the id '16d3cd12-4ecc-4d81-84e8-5fcc0d975b3e' will fail with status error 'NOT_FOUND' and error code '404'