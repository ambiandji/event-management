Feature: Find artist by id

  Scenario: Find Artist with an existing name
    Given I am logged in with the scopes 'artist:read'
    When I fetch artist with name 'name'
    Then I should the following artist is returned from database
      | id                                   | name                       | city                       | phone                       |
      | d7a4c906-4efe-42eb-808e-a772aa8da6e7 | Artist_e2e_findById_name_1 | Artist_e2e_findById_city_1 | Artist_e2e_findById_phone_1 |

  Scenario: Find Artist with wrong name
    Given I am logged in with the scopes 'artist:read'
    Then The attempt to fetch an artist with the name 'Artist_e2e_findByName_nameeeeee_2' will return empty list