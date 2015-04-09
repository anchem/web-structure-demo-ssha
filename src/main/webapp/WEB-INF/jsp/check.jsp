<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML>
<html lang="zh-CN">
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>works</title>
</head>
<body>
	<p>The status of system : ${isWork }</p>
	<p>
		Students:
		<c:forEach items="${stuList }" var="student" >
			${student["id"] } : ${student["name"] } <br>
		</c:forEach>
	</p>
	<p>Process testing : <input type="submit" value="StartProcess" onclick="start()" /></p>
	Status:
	<div id="messages"></div>
	<script type="text/javascript">
		var webSocket = 
			new WebSocket('ws://localhost:8080/HelloWorld/websocket');
		webSocket.onerror = function(event) {
			onError(event)
		};

		webSocket.onopen = function(event) {
			onOpen(event)
		};

		webSocket.onmessage = function(event) {
			onMessage(event)
		};

		function onMessage(event) {
			document.getElementById('messages').innerHTML 
				+= '<br />' + event.data;
		}

		function onOpen(event) {
			document.getElementById('messages').innerHTML 
				= 'Connection established';
		}

		function onError(event) {
			alert(event.data);
		}

		function start() {
			webSocket.send('hello server');
			return false;
		}
	</script>
</body>
</html>