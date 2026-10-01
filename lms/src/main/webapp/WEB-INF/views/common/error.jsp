<%@ page contentType="text/html;charset=UTF-8" isErrorPage="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:public title="Error" hero="false">
  <div class="text-center py-4">
    <h1 class="display-4 text-danger"><c:out value="${empty errorStatus ? 'Oops' : errorStatus}"/></h1>
    <p class="lead"><c:out value="${empty errorMessage ? 'Something went wrong. Please try again.' : errorMessage}"/></p>
    <a class="btn btn-primary" href="${ctx}/home"><i class="fa fa-home"></i> Back to home</a>
    <a class="btn btn-outline-secondary" href="javascript:history.back()">Go back</a>
  </div>
</t:public>
