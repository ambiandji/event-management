package org.crok4it.em.e2e.steps;

import io.cucumber.java8.En;

public class SecuritySteps implements En{
    public SecuritySteps() {
        Given("I am logged in with the scopes {string}",
                (String commaSeparatedScopes) -> {
        });
    }
}
