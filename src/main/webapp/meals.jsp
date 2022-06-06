<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>--%>
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
<h4><a href="meals?action=add">Add Meal</a></h4>
<table border cellpadding="8">
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th></th>
        <th></th>
    </tr>
    <c:forEach var="meal" items="${meals}">
        <c:choose>
            <c:when test="${meal.excess}">
                <tr style="color: red">
            </c:when>
            <c:otherwise>
                <tr style="color: forestgreen">
            </c:otherwise>
        </c:choose>
        <td>${meal.dateTime}</td>
        <%--        <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${mealTo.dateTime}" /></td>--%>
        <td>${meal.description}</td>
        <td>${meal.calories}</td>
        <td><a href="meals?action=edit">Update</a></td>
        <td><a href="meals?action=delete">Delete</a></td>
        </tr>
    </c:forEach>
</table>

</body>
</html>