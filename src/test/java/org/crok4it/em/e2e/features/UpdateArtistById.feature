Feature: Update artist by id

  Scenario: Update Artist with an existing id
    Given I am logged in with the scopes 'artist:update'
    And The following artist is store in database
      | id                                   | name                         | city                         | phone                         |
      | ad7bbfa1-9bab-424f-a694-bdaab6990138 | Artist_e2e_updateById_name_1 | Artist_e2e_updateById_city_1 | Artist_e2e_updateById_phone_1 |
    When I update artist with id 'ad7bbfa1-9bab-424f-a694-bdaab6990138' with the following data
      | name                           | city                           | phone                           |
      | Artist_e2e_updateById_name_up1 | Artist_e2e_updateById_city_up1 | Artist_e2e_updateById_phone_up1 |
    Then I should the following artist is saved to database
      | id                                   | name                           | city                           | phone                           |
      | ad7bbfa1-9bab-424f-a694-bdaab6990138 | Artist_e2e_updateById_name_up1 | Artist_e2e_updateById_city_up1 | Artist_e2e_updateById_phone_up1 |

  Scenario: Update Artist with wrong id
    Given I am logged in with the scopes 'artist:read'
    Then The attempt to update an artist with the id '28cc6e6b-22f0-4c2d-9ea4-da9b42155054' and following data will fail with status error 'NOT_FOUND' and error code '404'
      | name                                | city                                | phone                                |
      | Artist_e2e_updateById_name_updated2 | Artist_e2e_updateById_city_updated2 | Artist_e2e_updateById_phone_updated2 |