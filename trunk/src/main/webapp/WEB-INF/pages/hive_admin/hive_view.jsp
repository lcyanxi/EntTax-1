<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<html>
<head>
<title>Hive Web Interface-Create a Hive Session</title>
</head>
<body>
	<table>
		<tr>
			<td valign="top">
				<h2>select hive table to file.</h2>
				<form action="hiveSelect.jsp">
					<table border="1">
						<tr>
							<td>Session Name</td>
							<td><input type="text" name="table" value="table"></td>
						</tr>
						<tr>
							<td colSpan="2"><input type="submit"></td>
						</tr>
					</table>
				</form>
			</td>
		</tr>
	</table>
</body>
</html>

</html>
