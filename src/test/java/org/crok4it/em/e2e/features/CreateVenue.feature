Feature: Create Venue

  Scenario: Create new venue with valid data
    Given I am logged in with the scopes 'venue:create'
    When I create a new venue with the following data
      | name                   | city                   | phone                   |
      | Venue_e2e_create_name | Venue_e2e_create_city | Venue_e2e_create_phone |
    Then I should see that the current venue identifier has the following data
      | name                   | city                   | phone                   |
      | Venue_e2e_create_name | Venue_e2e_create_city | Venue_e2e_create_phone |

  Scenario: Create a new venue without required data
    Given I am logged in with the scopes 'venue:create'
    Then I should see that attempt to create a new venue with the following data will fail with status error 'BAD_REQUEST' and error code '400'
      | name                   | city | phone |
      | Venue_e2e_create_name |      |       |

  Scenario: Create a new venue with duplicate phone number
    Given I am logged in with the scopes 'venue:create'
    And Following venue is in database
      | id                                   | name                     | city                     | phone                     |
      | b0097ecc-af81-4152-90d6-dbd7ae0865b7 | Venue_e2e_create_name_1 | Venue_e2e_create_city_1 | Venue_e2e_create_phone_1 |
    Then I should see that attempt to create a new venue with the following data will fail with status error 'CONFLICT' and error code '409'
      | name                     | city                     | phone                     |
      | Venue_e2e_create_name_2 | Venue_e2e_create_city_2 | Venue_e2e_create_phone_1 |

# TODO enable this when security will be implemented
#  Scenario: Create a new venue without credentials
#    Given I am logged in with the scopes ''
#    Then I should see that attempt to create a new venue will fail with status error 'UNAUTHORIZED' and error code '401'
#      | name                   | city                   | phone                   |
#      | Venue_e2e_create_name_3 | Venue_e2e_create_city_3 | Venue_e2e_create_phone_3 |
