<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:admin title="Update" subtitle="Student" active="student">
  <div class="row"><div class="col-md-8 offset-md-2">
    <div class="panel-box">
      <div class="panel-head">Student Info - <c:out value="${student.studentCode}"/></div>
      <div class="panel-body">
        <form method="post" action="${ctx}/admin/students/edit" class="row g-3">
          <input type="hidden" name="_csrf" value="${csrfToken}">
          <input type="hidden" name="id" value="${student.id}">
          <div class="col-md-6"><label class="form-label" for="fullname">Full Name</label>
            <input class="form-control" type="text" id="fullname" name="fullname" maxlength="255" required value="<c:out value='${empty param.fullname ? student.name : param.fullname}'/>"></div>
          <div class="col-md-6"><label class="form-label" for="email">Email</label>
            <input class="form-control" type="email" id="email" name="email" maxlength="255" required value="<c:out value='${empty param.email ? student.email : param.email}'/>"></div>
          <div class="col-md-6"><label class="form-label" for="phone">Mobile Number</label>
            <input class="form-control" type="text" id="phone" name="phone" maxlength="15" pattern="[0-9]{7,15}" required value="<c:out value='${empty param.phone ? student.phone : param.phone}'/>"></div>
          <c:set var="g" value="${fn:toLowerCase(empty param.gender ? student.gender : param.gender)}"/>
          <div class="col-md-3"><label class="form-label" for="gender">Gender</label>
            <select class="form-select" id="gender" name="gender" required>
              <option value="male" ${g == 'male' ? 'selected' : ''}>Male</option>
              <option value="female" ${g == 'female' ? 'selected' : ''}>Female</option>
              <option value="other" ${g == 'other' ? 'selected' : ''}>Other</option>
            </select></div>
          <div class="col-md-3"><label class="form-label" for="status">Status</label>
            <select class="form-select" id="status" name="status">
              <option value="1" ${student.verified == 1 ? 'selected' : ''}>Active</option>
              <option value="0" ${student.verified == 0 ? 'selected' : ''}>Blocked</option>
            </select></div>
          <div class="col-12"><label class="form-label" for="address">Address</label>
            <input class="form-control" type="text" id="address" name="address" maxlength="100" required value="<c:out value='${empty param.address ? student.address : param.address}'/>"></div>
          <div class="col-12">
            <button type="submit" name="update" class="btn btn-info"><i class="fa fa-save"></i> Update</button>
            <a class="btn btn-outline-secondary" href="${ctx}/admin/students">Cancel</a>
          </div>
        </form>
      </div>
    </div>
  </div></div>
</t:admin>
