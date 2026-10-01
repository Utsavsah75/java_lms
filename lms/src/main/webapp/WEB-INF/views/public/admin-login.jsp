<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:public title="Admin Login" hero="true" heading="LOGIN" sub="ADMIN">
  <form action="${ctx}/admin-login" method="post" autocomplete="off">
    <input type="hidden" name="_csrf" value="${csrfToken}">
    <div class="mb-3">
      <label class="form-label" for="email">Email</label>
      <input type="email" class="form-control" id="email" name="email" maxlength="255" value="<c:out value='${rememberedEmail}'/>" required>
    </div>
    <div class="mb-3">
      <label class="form-label" for="password">Password</label>
      <input type="password" class="form-control" id="password" name="pwd" maxlength="100" required>
    </div>
    <div class="form-check mb-3">
      <input type="checkbox" class="form-check-input" id="remember" name="rem" ${not empty rememberedEmail ? 'checked' : ''}>
      <label class="form-check-label" for="remember">Remember Me</label>
    </div>
    <div class="d-grid">
      <button type="submit" class="btn btn-primary" name="login">Login</button>
    </div>
    <c:if test="${registrationOpen}">
      <p class="text-center mt-3 mb-0">New Member? <a href="${ctx}/admin-register">Register Here</a></p>
    </c:if>
  </form>
</t:public>
