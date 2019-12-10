Feature: LoginFeature
  This Feature deals with the login functionality of the application

  Background:
    Given message response
    And <response> header Content-Type is "text/html"
    And <response> header citrus_http_method is "GET"
	And <response> header citrus_request_path is "/demosite/index.html?UserName=asf&Password=aw&Login=Login"
	And <response> header citrus_request_url is "http://www.executeautomation.com"
    @Test
    Scenario: Use Cucumber with Selenium
	  Given <client> should receive html message <response>