<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<jsp:include page="fragments/headTag.jsp"/>
<head>
    <jsp:include page="fragments/bodyHeader.jsp"/>
</head>
<body>
<section>
    <hr>
    <jsp:useBean id="user" type="ru.javawebinar.topjava.model.User" scope="request"/>
    <h2><spring:message code="user.${user.id == null ? 'create' : 'edit'}"/></h2>
    <form method="post">
        <input type="hidden" name="id" value="${user.id}">
        <dl>
            <dt><spring:message code="user.name"/>:</dt>
            <dd><input type="text" value="${user.name}" size="40" name="name" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="user.email"/>:</dt>
            <dd><input type="text" value="${user.email}" size="40" name="email" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="user.roles"/>:</dt>
            <dd>
                <%--                <form:radiobutton path="roles" value="rolesValue"--%>
                <%--                <form:checkboxes path="roles" items="${rolesValue}"/>--%>
            </dd>
        </dl>
        <dl>
            <dt><spring:message code="user.active"/>:</dt>
            <%--            <form:radiobuttons path="enabled" items="${user.enabled}"/>--%>
        </dl>
        <dl>
            <dt><spring:message code="user.registered"/>:</dt>
            <dd><input type="datetime-local" value="${user.registered}" name="date" required></dd>
        </dl>
        <button type="submit"><spring:message code="common.save"/></button>
        <button onclick="window.history.back()" type="button"><spring:message code="common.cancel"/></button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
