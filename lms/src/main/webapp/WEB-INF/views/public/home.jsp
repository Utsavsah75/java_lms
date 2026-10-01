<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:public title="Home" hero="true" heading="LOGIN" sub="STUDENT">
  <form action="${ctx}/login" method="post" autocomplete="off">
    <input type="hidden" name="_csrf" value="${csrfToken}">
    <div class="mb-3">
      <label class="form-label" for="email">Email</label>
      <input type="email" class="form-control" id="email" name="email" maxlength="255" required>
    </div>
    <div class="mb-3">
      <label class="form-label" for="pwd">Password</label>
      <input type="password" class="form-control" id="pwd" name="pwd" maxlength="100" required>
    </div>
    <div class="d-grid">
      <button type="submit" class="btn btn-primary" name="login">Login</button>
    </div>
    <p class="text-center mt-3 mb-0">New Member? <a href="${ctx}/register">Register Here</a></p>
  </form>
</t:public>
