Feature: Create Artist

  Scenario: Create new Artist with valid data
    Given I am logged in with the scopes 'artist:create'
    When I create a new artist with the following data
      | name                   | city                   | phone                   |
      | Artist_e2e_create_name | Artist_e2e_create_city | Artist_e2e_create_phone |
    Then I should see that the current artist identifier has the following data
      | name                   | city                   | phone                   |
      | Artist_e2e_create_name | Artist_e2e_create_city | Artist_e2e_create_phone |

  Scenario: Create a new artist without required data
    Given I am logged in with the scopes 'artist:create'
    Then I should see that attempt to create a new artist with the following data will fail with status error 'BAD_REQUEST' and error code '400'
      | name                   | city | phone |
      | Artist_e2e_create_name |      |       |

  Scenario: Create a new artist with duplicate phone number
    Given I am logged in with the scopes 'artist:create'
    And Following artist is in database
      | id                                   | name                     | city                     | phone                     |
      | b0097ecc-af81-4152-90d6-dbd7ae0865b7 | Artist_e2e_create_name_1 | Artist_e2e_create_city_1 | Artist_e2e_create_phone_1 |
    Then I should see that attempt to create a new artist with the following data will fail with status error 'CONFLICT' and error code '409'
      | name                     | city                     | phone                     |
      | Artist_e2e_create_name_2 | Artist_e2e_create_city_2 | Artist_e2e_create_phone_1 |

# TODO enable this when security will be implemented
#  Scenario: Create a new artist without credentials
#    Given I am logged in with the scopes ''
#    Then I should see that attempt to create a new artist will fail with status error 'UNAUTHORIZED' and error code '401'
#      | name                   | city                   | phone                   |
#      | Artist_e2e_create_name_3 | Artist_e2e_create_city_3 | Artist_e2e_create_phone_3 |
