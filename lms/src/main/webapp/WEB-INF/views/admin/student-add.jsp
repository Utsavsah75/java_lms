<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:admin title="Add" subtitle="Student" active="student">
  <div class="row"><div class="col-md-8 offset-md-2">
    <div class="panel-box">
      <div class="panel-head">Student Info (the account is active immediately)</div>
      <div class="panel-body">
        <form method="post" action="${ctx}/admin/students/add" enctype="multipart/form-data" class="row g-3">
          <input type="hidden" name="_csrf" value="${csrfToken}">
          <div class="col-md-6"><label class="form-label" for="fullname">Full Name</label>
            <input class="form-control" type="text" id="fullname" name="fullname" maxlength="255" required value="<c:out value='${param.fullname}'/>"></div>
          <div class="col-md-6"><label class="form-label" for="email">Email</label>
            <input class="form-control" type="email" id="email" name="email" maxlength="255" required value="<c:out value='${param.email}'/>" data-check="email" data-target="#emailMsg">
            <div id="emailMsg" class="form-text"></div></div>
          <div class="col-md-6"><label class="form-label" for="phone">Mobile Number</label>
            <input class="form-control" type="text" id="phone" name="phone" maxlength="15" pattern="[0-9]{7,15}" title="Digits only" required value="<c:out value='${param.phone}'/>" data-check="phone" data-target="#phoneMsg">
            <div id="phoneMsg" class="form-text"></div></div>
          <div class="col-md-6"><label class="form-label" for="gender">Gender</label>
            <select class="form-select" id="gender" name="gender" required>
              <option value="">Select</option>
              <option value="male" ${param.gender == 'male' ? 'selected' : ''}>Male</option>
              <option value="female" ${param.gender == 'female' ? 'selected' : ''}>Female</option>
              <option value="other" ${param.gender == 'other' ? 'selected' : ''}>Other</option>
            </select></div>
          <div class="col-md-6"><label class="form-label" for="password">Password</label>
            <input class="form-control" type="password" id="password" name="password" minlength="6" maxlength="100" required></div>
          <div class="col-md-6"><label class="form-label" for="photo">Photo</label>
            <input class="form-control" type="file" id="photo" name="photo" accept="image/png,image/jpeg,image/gif"></div>
          <div class="col-12"><label class="form-label" for="address">Address</label>
            <textarea class="form-control" id="address" name="address" rows="2" maxlength="100" required><c:out value="${param.address}"/></textarea></div>
          <div class="col-12">
            <button type="submit" class="btn btn-info"><i class="fa fa-save"></i> Add Student</button>
            <a class="btn btn-outline-secondary" href="${ctx}/admin/students">Cancel</a>
          </div>
        </form>
      </div>
    </div>
  </div></div>
</t:admin>
