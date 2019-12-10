Feature: LoginFeature
  This Feature deals with the login functionality of the application

  Background:
    Given message response
    And <response> header Content-Type is "text/html"
    And <response> header citrus_http_method is "GET"
    And <response> header server is "Apache"
	And <response> header citrus_request_path is "http://www.executeautomation.com/demosite/Login.html"
	And <response> payload is
    """
<!doctype html>
<html lang=''>

<head>
	<meta charset='utf-8'>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="styles.css">
	<script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
	<script src="script.js"></script>
	<title>Execute Automation</title>
</head>

<body>

	<div id='cssmenu'>
		<ul>
			<li><a href='LoginDemo.html'><span>Login</span></a></li>
		</ul>
	</div>
	<h1>Execute Automation Selenium Test Site</h1>

	<H2> Login</H2>

	<form action="index.html" enctype="text/plain" id="userName" method="get" name="Login" target="_self">
		<p>UserName &nbsp;<input maxlength="10" name="UserName" type="text" /></p>

			<p>Password &nbsp; &nbsp;<input maxlength="10" name="Password" type="text" /></p>

				<p><input name="Login" type="submit" value="Login" /></p>
	</form>

	<p>&nbsp;</p>
</body>

</html>
    """
    @Test
    Scenario: Use Cucumber with Selenium
      Given variables
        | userName  | password  |
        | adminUser | adminPass |
      And <user> should receive html message <response>