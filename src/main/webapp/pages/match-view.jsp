<%@ page import="models.Match" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: User
  Date: 09.06.2024
  Time: 23:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<Match> allMatches = (List<Match>) request.getAttribute("allMatches");
    int pageNumber = 1;
    String pageParam = request.getParameter("page");
    if (pageParam != null && !pageParam.isEmpty()) {
        pageNumber = Integer.parseInt(pageParam);
    }
%>
<html>
<head>
    <title>Матчи</title>
    <style>
        <%@include file="../css/match-view.css"%>
    </style>
</head>
<body>
<div class="container">
    <h1>Матчи</h1>
    <div class="table">
        <table>
            <tr>
                <th>Player1</th>
                <th>Player2</th>
                <th>Winner</th>
            </tr>
            <%
                for (Match match : allMatches) {
            %>
            <tr>
                <td><%=match.getPlayer1().getName()%></td>
                <td><%=match.getPlayer2().getName()%></td>
                <td><%=match.getWinner().getName()%></td>
            </tr>
            <%
                }
            %>
        </table>
    </div>
    <div class="button-block">
        <%
            if (pageNumber > 1) {
        %>
        <button class="button" onclick="window.location.href='?page=<%= pageNumber - 1 %>'">Предыдущая</button>
        <%
            }
            if (allMatches.size() == 5) {
        %>
        <button class="button" onclick="window.location.href='?page=<%=pageNumber + 1%>'">Следующая</button>
        <%
            }
        %>
    </div>

    <button class="button-exit" onclick="window.location.href='index.jsp'">Перейти на главную страницу</button>
</div>
</body>
</html>
