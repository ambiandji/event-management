Feature: Update venue by id

  Scenario: Update venue with an existing id
    Given I am logged in with the scopes 'venue:update'
    And The following venue is store in database
      | id                                   | name                         | city                         | phone                         |
      | ad7bbfa1-9bab-424f-a694-bdaab6990138 | Venue_e2e_updateById_name_1 | Venue_e2e_updateById_city_1 | Venue_e2e_updateById_phone_1 |
    When I update venue with id 'ad7bbfa1-9bab-424f-a694-bdaab6990138' with the following data
      | name                           | city                           | phone                           |
      | Venue_e2e_updateById_name_up1 | Venue_e2e_updateById_city_up1 | Venue_e2e_updateById_phone_up1 |
    Then I should the following venue is saved to database
      | id                                   | name                           | city                           | phone                           |
      | ad7bbfa1-9bab-424f-a694-bdaab6990138 | Venue_e2e_updateById_name_up1 | Venue_e2e_updateById_city_up1 | Venue_e2e_updateById_phone_up1 |

  Scenario: Update venue with wrong id
    Given I am logged in with the scopes 'venue:read'
    Then The attempt to update an venue with the id '28cc6e6b-22f0-4c2d-9ea4-da9b42155054' and following data will fail with status error 'NOT_FOUND' and error code '404'
      | name                                | city                                | phone                                |
      | Venue_e2e_updateById_name_updated2 | Venue_e2e_updateById_city_updated2 | Venue_e2e_updateById_phone_updated2 |