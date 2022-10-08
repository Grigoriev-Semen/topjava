<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html lang="ru">
<head>
    <title>Meals</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<a href='meals?&action=add'>Добавить</a>
<h2>Список еды</h2>
<table>
    <tr>
        <th>Дата/Время</th>
        <th>Описание</th>
        <th>Калории</th>
    </tr>

    <c:forEach var="meal" items="${meals}">
        <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/>
        <tr style="background-color:${meal.excess ? 'red' : 'green'}">
            <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both"/>
            <fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${parsedDateTime}" var="date"/>
            <td>${date}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            <td><a href="meals?action=edit&id=${meal.id}">Edit</a></td>
        </tr>
    </c:forEach>
</table>
<hr>
<h3><a href="index.html">Home</a></h3>
</body>
</html>