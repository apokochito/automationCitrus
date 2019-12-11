Feature: LoginFeature

  Background:
    Given message response
    And <response> header Content-Type is "text/html"
    And <response> header citrus_http_method is "GET"
    And <response> header citrus_http_status is "200"

  @Test
  Scenario: Use Cucumber with Selenium
	Given <client> should receive text/html message <response>