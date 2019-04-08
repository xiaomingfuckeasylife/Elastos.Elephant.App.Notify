<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE HTML>
<!--
	Eventually by HTML5 UP
	html5up.net | @ajlkn
	Free for personal and commercial use under the CCA 3.0 license (html5up.net/license)
-->
<html>
	<head>
		<title>Eventually by HTML5 UP</title>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
		<link rel="stylesheet" href="/assets/css/main.css" />
	</head>
	<body class="is-preload">

		<!-- Header -->
			<header id="header">
				<h1>Notification</h1>
				<p>Notify you when someone send you ELA Coin, <br />
				</p>
			</header>
			<!-- Signup Form -->
			<%--
			<form id="signup-form" method="post" action="${action}">
				<c:choose>
					<c:when test="${step == 1}">
						<input type="text" id="phone" name="phone" placeholder="Phone numbers" required>
						<input type="submit" value="Submit" />
					</c:when>
					<c:when test="${step == 2}">
						<input type="text" id="verifyPhone" name="verifyPhone" placeholder="verification code" required>
						<input type="submit" value="Submit" />
					</c:when>
					<c:when test="${step == 3}">
						<input type="email" name="email" id="email" placeholder="Email Address" required>
						<input type="submit" value="Submit" />
					</c:when>
					<c:when test="${step == 4}">
						We have sent a confirmation email to your registered email address. ${email}.
						Please follow the instructions in the email to continue.
					</c:when>
					<c:otherwise>
						<input type="text" id="address" name="address" placeholder="ELA Address" required>
					</c:otherwise>
				</c:choose>
			</form>
			--%>
			<input type="submit" onclick="submit()" value="subscribe" />
			<script src="/assets/js/main.js"></script>
	</body>
	<script type="application/javascript">
		function submit() {
            window.location.href = '${elephant_auth}';
        }
	</script>
</html>