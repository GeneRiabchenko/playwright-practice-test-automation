# Playwright Tool Shop Tests

[![Build and API Tests](https://github.com/GeneRiabchenko/playwright-practice-test-automation/actions/workflows/playwright-tests.yml/badge.svg)](https://github.com/GeneRiabchenko/playwright-practice-test-automation/actions/workflows/playwright-tests.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
![Java 17](https://img.shields.io/badge/Java-17-blue)

UI and API test automation for [practicesoftwaretesting.com](https://practicesoftwaretesting.com/) (a demo e-commerce "Tool Shop") and its API, written in Java with Playwright.

**Sample Serenity report:** https://generiabchenko.github.io/playwright-practice-test-automation/

The project started as a follow-along for the Udemy course
[Mastering Modern Test Automation with Playwright in Java](https://www.udemy.com/course/mastering-modern-test-automation-with-playwright-in-java/).
Everything beyond the course basics is my own work: Serenity BDD reporting, Cucumber scenarios, the registration/cart/login suites, the `UserBuilder` test-data builder, the cart workflow layer and the CI/report pipeline.

## What is covered

- **UI flows:** product catalog and search, filtering and sorting, product details, shopping cart (add, update quantity, remove, checkout redirect), registration with field validation, login, contact form.
- **API:** user registration through the REST API, plus UI tests against mocked API responses (`page.route`).
- **Two styles side by side:** plain JUnit 5 tests and Gherkin scenarios (Cucumber), sharing the same page objects.

## Tech stack

- **Playwright for Java** - browser automation and API requests
- **JUnit 5** - test engine, parallel execution (4 threads), `@Nested` grouping
- **Cucumber** - Gherkin feature tests (`src/test/resources/features`)
- **Serenity BDD** - reporting with `@Feature` / `@Story` grouping and `@Step` annotations
- **AssertJ / JUnit assertions**, **Datafaker** for random test data
- **GitHub Actions** - compiles the project and runs the API-only tests on every push/PR
- **TeamCity** - runs the full suite (UI + Cucumber) and keeps the Serenity report and failure traces as build artifacts

## Design notes

- **Page Object Model** - every page extends `BasePage`; actions and checks are annotated with `@Step`, so they show up in the Serenity report. Locators use `data-test` attributes (`getByTestId`), never CSS/XPath.
- **Workflow layer** (`workflows/`) - `CartWorkflow` composes several page objects into business actions (e.g. "add a product and open the cart"). It was introduced only once the same multi-page sequence was repeated across tests.
- **Test data** - `UserBuilder` (fluent builder with Datafaker defaults); `Address` has `with*` methods to override a single field. Country defaults to a value that exists in the registration dropdown.
- **Fixtures** - base runners own the Playwright lifecycle, record a trace for every test and keep it only when the test fails.

## Project structure

- `src/test/java/com/playwright/toolshop/pages` - page objects
- `src/test/java/com/playwright/toolshop/workflows` - multi-page business actions
- `src/test/java/com/playwright/toolshop/tests` - JUnit 5 tests, grouped by feature
- `src/test/java/com/playwright/toolshop/cucumber` - Cucumber runner and step definitions
- `src/test/resources/features` - Gherkin feature files
- `src/test/java/com/playwright/toolshop/fixtures` - base runners, tracing, test watcher
- `src/test/java/com/playwright/toolshop/utils` - data models, builders, API client, request mocking

## Getting started

**Prerequisites:** JDK 17+ and Maven 3.9+. Playwright downloads the browsers on the first run (Node.js is only needed on a fresh Linux machine for `npx playwright install-deps`).

```bash
git clone https://github.com/GeneRiabchenko/playwright-practice-test-automation.git
cd playwright-practice-test-automation
mvn verify
```

Tests run **headless** by default. To watch the browser locally:

```bash
mvn verify -Dheadless=false
```

Run a single class or method, or filter Cucumber scenarios by tag:

```bash
mvn verify -Dit.test=LoginPageTests
mvn verify -Dit.test=LoginPageTests#invalidUserShouldNotBeLoggedIn
mvn verify -Dtags="@smoke"
```

## Reports and artifacts

- Serenity HTML report: `target/site/serenity/index.html`
- Cucumber HTML report: `target/cucumber-reports/cucumber.html`
- Playwright traces of failed tests: `target/traces/*.zip` (open with `npx playwright show-trace <file>`)

## Known limitations

- The tests run against a **public demo site**, so they depend on its availability and data. Occasionally the site changes its data between requests, which can cause a rare flaky failure (e.g. an "invalid product id" response while adding to the cart).
- The demo site sits behind Cloudflare, which serves a challenge (HTTP 403) to GitHub-hosted runners. That is why the GitHub Actions workflow only builds the project and runs the API tests, and the UI/Cucumber suite runs from a regular IP (locally or in TeamCity).
- This is a learning project, not a production framework.

## License

[MIT](LICENSE)
