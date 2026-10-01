<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:public title="Admin Registration" hero="false">
  <h2 class="text-center" style="color:#2c7ad6">ADMIN REGISTRATION</h2>
  <form action="${ctx}/admin-register" method="post" class="mx-auto mt-3" style="max-width:520px" data-match="password,confirmpassword">
    <input type="hidden" name="_csrf" value="${csrfToken}">
    <div class="mb-3"><label class="form-label" for="name">Full name</label>
      <input type="text" class="form-control" id="name" name="name" maxlength="50" value="<c:out value='${param.name}'/>" required></div>
    <div class="mb-3"><label class="form-label" for="email">Email</label>
      <input type="email" class="form-control" id="email" name="email" maxlength="255" value="<c:out value='${param.email}'/>" required></div>
    <div class="mb-3"><label class="form-label" for="gender">Gender</label>
      <select class="form-select" id="gender" name="gender" required>
        <option value="">Select</option>
        <option value="male" ${param.gender == 'male' ? 'selected' : ''}>Male</option>
        <option value="female" ${param.gender == 'female' ? 'selected' : ''}>Female</option>
        <option value="other" ${param.gender == 'other' ? 'selected' : ''}>Other</option>
      </select></div>
    <div class="mb-3"><label class="form-label" for="phone">Contact number</label>
      <input type="tel" class="form-control" id="phone" name="phone" maxlength="15" pattern="[0-9]{7,15}" title="7 to 15 digits" value="<c:out value='${param.phone}'/>" required></div>
    <div class="mb-3"><label class="form-label" for="address">Address</label>
      <input type="text" class="form-control" id="address" name="address" maxlength="50" value="<c:out value='${param.address}'/>" required></div>
    <div class="mb-3"><label class="form-label" for="password">Password</label>
      <input type="password" class="form-control" id="password" name="password" minlength="6" maxlength="100" required></div>
    <div class="mb-3"><label class="form-label" for="confirmpassword">Confirm password</label>
      <input type="password" class="form-control" id="confirmpassword" name="confirmpassword" minlength="6" maxlength="100" required></div>
    <div class="d-flex gap-2">
      <button type="submit" class="btn btn-primary" name="register">Register</button>
      <button type="reset" class="btn btn-outline-secondary">Reset</button>
      <a class="btn btn-link ms-auto" href="${ctx}/admin-login">Back to login</a>
    </div>
  </form>
</t:public>
