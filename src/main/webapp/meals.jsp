<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--<%--%>
<%--    Object mealTo = request.getAttribute("mealTo");--%>
<%--%>--%>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<h4><a href="add_meal.html">Add Meal</a></h4>
<%--<c:out value="${12+56*2}" />--%>
<%--<c:set var="mealsTo" value='${requestScope["mealsTo"]}'/>--%>
<table border cellpadding="8">
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th></th>
        <th></th>
    </tr>
    <c:forEach var="mealTo" items="${mealsTo}">
        <c:if test="${mealTo.excess}">
            <tr style="color: red">
        </c:if>
        <c:if test="${!mealTo.excess}">
            <tr style="color: forestgreen">
        </c:if>
        <td>${mealTo.dateTime}</td>
        <td>${mealTo.description}</td>
        <td>${mealTo.calories}</td>
        <td><a href="update.html">Update</a></td>
        <td><a href="update.html">Delete</a></td>
        </tr>
    </c:forEach>
</table>

</body>
</html>