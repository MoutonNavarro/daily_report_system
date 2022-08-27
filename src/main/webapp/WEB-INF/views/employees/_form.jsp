<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="constants.AttributeConst" %>
<%@ page import="constants.ForwardConst" %>

<c:set var="action" value="${ForwardConst.ACT_EMP.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />

<c:if test="${errors != null}">
    <div id="flush_error">
        Error at the input content.<br />
        <c:forEach var="error" items="${errors}">
            #<c:out value="${error}" /><br />
        </c:forEach>
    </div>
</c:if>
<label for="${AttributeConst.EMP_CODE.getValue()}">Employee number</label><br />
<input type="text" name="${AttributeConst.EMP_CODE.getValue()}" id="${AttributeConst.EMP_CODE.getValue()}" value="${employee.code}" />
<br /><br />

<label for="${AttributeConst.EMP_NAME.getValue()}">Name</label><br />
<input type="text" name="${AttributeConst.EMP_NAME.getValue()}" id="${AttributeConst.EMP_NAME.getValue()}" value="${employee.name}" />
<br /><br />

<label for="${AttributeConst.EMP_PASS.getValue()}">Password</label><br />
<input type="password" name="${AttributeConst.EMP_PASS.getValue()}" id="${AttributeConst.EMP_PASS.getValue()}" />
<br /><br />

<label for="${AttributeConst.EMP_ADMIN_FLAG.getValue()}">Rights</label><br />
<select name="${AttributeConst.EMP_ADMIN_FLAG.getValue()}" id="${AttributeConst.EMP_ADMIN_FLAG.getValue()}">
    <option value="${AttributeConst.ROLE_GENERAL.getIntegerValue()}"<c:if test="${employee.adminFlag == AttributeConst.ROLE_GENERAL.getIntegerValue()}"> selected</c:if>>General</option>
    <option value="${AttributeConst.ROLE_ADMIN.getIntegerValue()}"<c:if test="${employee.adminFlag == AttributeConst.ROLE_ADMIN.getIntegerValue()}"> selected</c:if>>Administrator</option>
</select>
<br /><br />
<input type="hidden" name="${AttributeConst.EMP_ID.getValue()}" value="${employee.id}" />
<input type="hidden" name="${AttributeConst.TOKEN.getValue()}" value="${_token}" />
<button type="submit">Submit</button>