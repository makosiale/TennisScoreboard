<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 03.05.2024
  Time: 17:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <style>
        <%@include file="../css/new-match.css"%>
    </style>
</head>
<body>
<form class ="form-new-matches" method="post" action="/new-match" id="form-new-matches">
    <label class="form-name" for="form-new-matches">Форма создания матча:</label><br>
    <label for="player1">Имя первого игрока</label>
    <input type="text" name="player1" required id="player1"><br>
    <label for="player2">Имя второго игрока</label>
    <input type="text" name="player2" required id="player2"><br>
    <button class="c-button" type="submit">Отправить</button>
</form>
</form>
</body>
</html>
