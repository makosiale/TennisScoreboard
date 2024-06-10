<%@ page import="models.Match" %>
<% Match match = (Match) request.getAttribute("match"); %>
<%@ page import="services.OngoingMatchesService" %><%--
  Created by IntelliJ IDEA.
  User: User
  Date: 12.05.2024
  Time: 17:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Матч №<%=request.getParameter("uuid")%></title>
    <style>
        <%@include file="../css/match-score.css"%>
    </style><%--
    <script>
        function checkWinner(){
            var winner = '<%= match.getWinner()!=null ? match.getWinner().getName() : ""%>';
            if (winner) {
                alert("Победитель: " + winner);
            }
        }
        window.onload=checkWinner();
    </script>--%>
</head>
<body>
<% if (match.getWinner()==null){ %>
<div class="table">
    <table>
        <caption>Сводная таблица счёта</caption>
        <tr>
            <td>Имя игрока</td>
            <td>Счёт</td>
            <td>Счёт гейма</td>
            <td>Счёт по сетам</td>
        </tr>
        <tr>
            <th><%=match.getPlayer1().getName()%></th>
            <th>
                <% if (match.getScore1() == 50) { %>
                AD
                <% } else { %>
                <%=match.getScore1()%>
                <% } %>
            </th>
            <th><%=match.getScoreByGame1()%></th>
            <th><%=match.getScoreBySet1()%></th>
        </tr>
        <tr>
            <th><%=match.getPlayer2().getName()%></th>
            <th>
                <% if (match.getScore2() == 50) { %>
                AD
                <% } else { %>
                <%=match.getScore2()%>
                <% } %>
            </th>
            <th><%=match.getScoreByGame2()%></th>
            <th><%=match.getScoreBySet2()%></th>
        </tr>
    </table>
</div>
<form method="post" action="/match-score">
    <input type="hidden" name="uuid" value="<%=request.getParameter("uuid")%>">
    <div class="button-block">
        <span></span><button class="button" type="submit" name="submit" value="player1">Первый</button>
        <button class="button" type="submit" name="submit" value="player2">Второй</button>
    </div>
</form>
<%} else { %>
<div class = "win-state">
    <p class="p-win-state">Победитель:</p><br>
    <p class="p-win-state"><%=match.getWinner().getName()%></p>
</div>
<div class="button-container">
    <button class="button" onclick="window.location.href='../index.jsp'">В главное меню</button>
    <button class="button" onclick="window.location.href='/matches'">К списку завершенных матчей</button>
</div>
<% } %>
</body>
</html>
