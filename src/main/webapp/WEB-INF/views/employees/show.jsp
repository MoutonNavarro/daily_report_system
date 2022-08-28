<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.ForwardConst" %>
<%@ page import="constants.AttributeConst" %>

<c:set var="actEmp" value="${ForwardConst.ACT_EMP.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commEdit" value="${ForwardConst.CMD_EDIT.getValue()}" />

<c:import url="../layout/app.jsp">
    <c:param name="content">
        <H2>ID: ${employee.id}'s employee information detail page</H2>

        <table>
            <tbody>
                <tr>
                    <th>Employee number</th>
                    <td><c:out value="${employee.code}" /></td>
                </tr>
                <tr>
                    <th>Name</th>
                    <td><c:out value="${employee.name}" /></td>
                </tr>
                <tr>
                    <th>Rights</th>
                    <td><c:choose>
                            <c:when test="${employee.adminFlag == AttributeConst.ROLE_ADMIN.getIntegerValue()}">Administrator</c:when>
                            <c:otherwise>General</c:otherwise>
                        </c:choose></td>
                </tr>
                <tr>
                    <th>Date registered</th>
                    <fmt:parseDate value="${employee.createdAt}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="createDay" type="date" />
                    <td><fmt:formatDate value="${createDay}" pattern="MM/dd/yyyy HH:mm:ss" /></td>
                </tr>
                <tr>
                    <th>Date updated</th>
                    <fmt:parseDate value="${employee.updatedAt}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="updateDay" type="date" />
                    <td><fmt:formatDate value="${updateDay}" pattern="MM/dd/yyyy HH:mm:ss" /></td>
                </tr>
            </tbody>
        </table>

        <p>
            <a href="<c:url value='?action=${actEmp}&command=${commEdit}&id=${employee.id}' />">Edit this employee information</a>
        </p>

        <p>
            <a href="<c:url value='?action=${actEmp}&command=${commIdx}' />">Back to list</a>
        </p>
    </c:param>
</c:import>
