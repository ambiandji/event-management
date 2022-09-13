Feature: Find venue by id

  Scenario: Find Venue with an existing id
    Given I am logged in with the scopes 'venue:read'
    When I fetch venue with id 'd7a4c906-4efe-42eb-808e-a772aa8da6e7'
    Then I should the following venue is returned from database
      | id                                   | name                       | city                       | phone                       |
      | d7a4c906-4efe-42eb-808e-a772aa8da6e7 | Venue_e2e_findById_name_1 | Venue_e2e_findById_city_1 | Venue_e2e_findById_phone_1 |

  Scenario: Find Venue with wrong id
    Given I am logged in with the scopes 'artist:read'
    Then The attempt to fetch an venue with the id '16d3cd12-4ecc-4d81-84e8-5fcc0d975b3e' will fail with status error 'NOT_FOUND' and error code '404'