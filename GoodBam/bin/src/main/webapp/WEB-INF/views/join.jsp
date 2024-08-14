<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>회원가입 페이지</h1>
	<form action="api/member/join" method="post">
		<input type="text" placeholder="이메일" name="email"><br>
		<input type="password" placeholder="패스워스" name="password"><br>
		<input type="text" placeholder="이름" name="name"><br>
		<input type="submit" value="회원가입">
	</form>
</body>
</html>