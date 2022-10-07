<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html lang="ru">
<head>
    <title>Meals</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
</head>
<body>

<form method="post" action="meals" enctype="application/x-www-form-urlencoded">
    <dl>
        <dt>Дата/Время:</dt>
        <dd><input type="datetime-local" name="localDateTime" value="${meal.dateTime}"></dd>
    </dl>

    <dl>
        <dt>Описание:</dt>
        <dd><input type="text" name="description" value="${meal.description}"></dd>
    </dl>

    <dl>
        <dt>Калории:</dt>
        <dd><input type="text" name="calories" value="${meal.calories}"></dd>
    </dl>
    <input type="hidden" name="id" value="${meal.id}">
    <hr>
    <button type="submit">Сохранить/Изменить</button>
    <button type="reset" onclick="window.history.back()">Отменить</button>
</form>

</body>
</html>