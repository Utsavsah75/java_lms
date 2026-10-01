<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:public title="Student Registration" hero="false">
  <h2 class="text-center" style="color:#2c7ad6">STUDENT REGISTRATION</h2>
  <p class="text-center text-muted">Create your library account. A verification link is sent to your e-mail.</p>
  <form action="${ctx}/register" method="post" enctype="multipart/form-data" class="row g-3 mx-auto" style="max-width:820px" data-match="password,confirmpassword">
    <input type="hidden" name="_csrf" value="${csrfToken}">
    <div class="col-md-6"><label class="form-label" for="fullname">Enter Full Name</label>
      <input class="form-control" type="text" id="fullname" name="fullname" maxlength="255" value="<c:out value='${param.fullname}'/>" required></div>
    <div class="col-md-6"><label class="form-label" for="address">Enter Address</label>
      <input class="form-control" type="text" id="address" name="address" maxlength="100" value="<c:out value='${param.address}'/>" required></div>
    <div class="col-md-6"><label class="form-label" for="mobile">Mobile Number</label>
      <input class="form-control" type="text" id="mobile" name="phone" maxlength="15" pattern="[0-9]{7,15}" title="Digits only, 7 to 15 digits"
             value="<c:out value='${param.phone}'/>" data-check="phone" data-target="#phoneMsg" required>
      <div id="phoneMsg" class="form-text"></div></div>
    <div class="col-md-6"><label class="form-label d-block">Gender</label>
      <div class="form-check form-check-inline mt-2"><input class="form-check-input" type="radio" id="male" name="gender" value="male" ${param.gender == 'male' ? 'checked' : ''} required><label class="form-check-label" for="male">Male</label></div>
      <div class="form-check form-check-inline"><input class="form-check-input" type="radio" id="female" name="gender" value="female" ${param.gender == 'female' ? 'checked' : ''} required><label class="form-check-label" for="female">Female</label></div>
      <div class="form-check form-check-inline"><input class="form-check-input" type="radio" id="other" name="gender" value="other" ${param.gender == 'other' ? 'checked' : ''} required><label class="form-check-label" for="other">Other</label></div></div>
    <div class="col-md-6"><label class="form-label" for="email">Enter Email</label>
      <input class="form-control" type="email" id="email" name="email" maxlength="255" value="<c:out value='${param.email}'/>" data-check="email" data-target="#emailMsg" required>
      <div id="emailMsg" class="form-text"></div></div>
    <div class="col-md-6"><label class="form-label" for="photo">User Picture</label>
      <input class="form-control" type="file" id="photo" name="photo" accept="image/png,image/jpeg,image/gif">
      <div class="form-text">jpg, png or gif, up to 2 MB.</div></div>
    <div class="col-md-6"><label class="form-label" for="password">Enter Password</label>
      <input class="form-control" type="password" id="password" name="password" minlength="6" maxlength="100" required></div>
    <div class="col-md-6"><label class="form-label" for="confirmpassword">Confirm Password</label>
      <input class="form-control" type="password" id="confirmpassword" name="confirmpassword" minlength="6" maxlength="100" required></div>
    <div class="col-12 d-flex gap-2">
      <button type="submit" name="add" class="btn btn-primary"><i class="fa fa-user-plus"></i> Register Now</button>
      <a class="btn btn-link ms-auto" href="${ctx}/home">Already registered? Login</a>
    </div>
  </form>
</t:public>
