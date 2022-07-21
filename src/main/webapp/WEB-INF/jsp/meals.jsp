<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<jsp:include page="fragments/headTag.jsp"/>
<head>
    <jsp:include page="fragments/bodyHeader.jsp"/>
    <%--    <title>Meals</title>--%>
    <%--    <link rel="stylesheet" href="css/style.css">--%>
</head>
<body>
<section>
    <h3><a href="index.jsp"><spring:message code="app.home"/></a></h3>
    <hr/>
    <h2><spring:message code="meal.title"/></h2>
    <form method="get" action="meals">
        <input type="hidden" name="action" value="filter">
        <dl>
            <dt>From Date (inclusive):</dt>
            <dd><input type="date" name="startDate" value="${param.startDate}"></dd>
        </dl>
        <dl>
            <dt>To Date (inclusive):</dt>
            <dd><input type="date" name="endDate" value="${param.endDate}"></dd>
        </dl>
        <dl>
            <dt>From Time (inclusive):</dt>
            <dd><input type="time" name="startTime" value="${param.startTime}"></dd>
        </dl>
        <dl>
            <dt>To Time (exclusive):</dt>
            <dd><input type="time" name="endTime" value="${param.endTime}"></dd>
        </dl>
        <button type="submit"><spring:message code="common.filter"/></button>
    </form>
    <hr/>
    <a href="meals/new"><spring:message code="meal.add"/></a>
    <br><br>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th><spring:message code="meal.datetime"/></th>
            <th><spring:message code="meal.description"/></th>
            <th><spring:message code="meal.calories"/></th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${requestScope.meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
            <tr data-meal-excess="${meal.excess}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td>
                    <a href="meals/${meal.id}" т><spring:message code="common.update"/></a>
                </td>
                <td>
                    <a href="meals/delete/${meal.id}"><spring:message code="common.delete"/></a>
                        <%--                        <form:hidden path="id" id="id" />--%>
                        <%--                    <input type="hidden" name="action" value="delete" required>--%>
                        <%--                    <form method="post" action="meals/${meal.id}">--%>
                        <%--                        <input type="hidden" name="_method" value="delete"/>--%>
                        <%--                        <button type="submit"><spring:message code="common.delete"/></button>--%>
                        <%--                    </form>--%>
                        <%--                    <form method="post" action=meals/${meal.id}">--%>
                        <%--                        <input type="hidden" name="_method" value="DELETE" />--%>
                        <%--                        <button type="submit"><spring:message code="common.delete"/></button>--%>
                        <%--                        <a href="meals/${meal.id}"><spring:message code="common.delete"/></a>--%>
                        <%--                    </form>--%>
                </td>
            </tr>
        </c:forEach>
    </table>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>