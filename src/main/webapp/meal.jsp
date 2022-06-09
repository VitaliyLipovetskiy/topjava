<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
    <title>Meal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>${param.action == 'add' ? 'New meal' : 'Edit meal'}</h2>
<%--<jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>--%>
<form action="meals" method="post">
    <input type="hidden" name="id" value="${meal.id}"/>
    <table>
        <tr>
            <td>DateTime:</td>
            <td><input type="datetime-local" name="dateTime" value="${meal.dateTime}" required/></td>
        </tr>
        <tr>
            <td>Description:</td>
            <td><input type="text" name="description" value="${meal.description}" required/></td>
        </tr>
        <tr>
            <td>Calories:</td>
            <td><input type="number" name="calories" value="${meal.calories}" required/></td>
        </tr>
        <tr>
            <td>
                <button type="submit">Save</button>
                <button onclick="window.history.back()" type="reset">Cansel</button>
            </td>
            <td></td>
        </tr>
    </table>

</form>
</body>
</html>