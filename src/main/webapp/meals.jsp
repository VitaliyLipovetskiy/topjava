<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .excess {
            color: red;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <hr/>
    <h2>Meals</h2>
    <form method="GET" action="meals">
        <table border="0" cellpadding="8" cellspacing="0">
            <tr>
                <td>От даты (включая)</td>
                <td>До даты (включая)</td>
                <td></td>
                <td>От времени (включая)</td>
                <td>До времени (включая)</td>
            </tr>
            <tr>
                <td><input type="date" value="${filter.get('startDate')}" name="startDate"></td>
                <td><input type="date" value="${filter.get('endDate')}" name="endDate"></td>
                <td></td>
                <td><input type="time" value="${filter.get('startTime')}" name="startTime"></td>
                <td><input type="time" value="${filter.get('endTime')}" name="endTime"></td>
            </tr>
            <tr>
                <%--                <td></td>--%>
                <%--                <td></td>--%>
                <%--                <td></td>--%>
                <td>
                    <button type="submit">Отфильтровать</button>
                </td>
                <td>
                    <button type="submit" name="cansel" value="true"> Отменить</button>
                </td>
            </tr>
        </table>
    </form>


    <br><br>
    <a href="meals?action=create">Add Meal</a>

    <br><br>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th style="width: 120px">Date</th>
            <th style="width: 200px">Description</th>
            <th style="width: 100px">Calories</th>
            <th style="width: 50px"></th>
            <th style="width: 50px"></th>
        </tr>
        </thead>
        <c:forEach items="${requestScope.meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
            <tr class="${meal.excess ? 'excess' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td style="text-align: center">${meal.calories}</td>
                <td style="text-align: center"><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td style="text-align: center"><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>