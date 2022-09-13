Feature: Find venue by name

  Scenario: Find venue with an existing name
    Given I am logged in with the scopes 'venue:read'
    When I fetch venue with name 'name'
    Then I should the following venue is returned from database
      | id                                   | name                       | city                       | phone                       |
      | d7a4c906-4efe-42eb-808e-a772aa8da6e7 | Venue_e2e_findById_name_1 | Venue_e2e_findById_city_1 | Venue_e2e_findById_phone_1 |

  Scenario: Find Venue with wrong name
    Given I am logged in with the scopes 'venue:read'
    Then The attempt to fetch an venue with the name 'Venue_e2e_findByName_nameeeeee_2' will return empty list