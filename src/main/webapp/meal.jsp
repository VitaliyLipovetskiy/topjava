<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
    <title>Meal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Edit meal</h2>

<form action="meals" method="post">
    <input name="id" value="${meal.id}"/>
    <table>
        <tr>
            <td>DateTime:</td>
            <td><input type="datetime-local" name="dateTime" value="${meal.dateTime}"/></td>
        </tr>
        <tr>
            <td>Description:</td>
            <td><input formenctype="text/plain" type="text" name="description" value="${meal.description}"/></td>
        </tr>
        <tr>
            <td>Calories:</td>
            <td><input name="calories" value="${meal.calories}"/></td>
        </tr>
        <tr>
            <td>
                <button type="submit">Save</button>
                <button type="reset">Cansel</button>
            </td>
            <td></td>
        </tr>
    </table>

</form>
</body>
</html>