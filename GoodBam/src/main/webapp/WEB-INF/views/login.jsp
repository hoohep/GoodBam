<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>로그인 페이지</h1>
	<form action="/login" method="post">
		<input type="text" placeholder="이메일" name="username"><br>
		<input type="password" placeholder="패스워스" name="password"><br>
		<input type="submit" value="로그인">
	</form>
	<button onclick="location.href='join'">회원가입 페이지</button>
	<button onclick="location.href='admin'">관리자 페이지</button>
</body>
</html>


