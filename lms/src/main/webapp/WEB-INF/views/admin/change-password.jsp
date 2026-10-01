<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:admin title="Change" subtitle="Password" active="password">
  <div class="row"><div class="col-md-6 offset-md-3">
    <div class="panel-box">
      <div class="panel-head">Change Password</div>
      <div class="panel-body">
        <form method="post" action="${ctx}/admin/change-password" data-match="newpassword,confirmpassword">
          <input type="hidden" name="_csrf" value="${csrfToken}">
          <div class="mb-3"><label class="form-label" for="password">Current Password</label>
            <input class="form-control" type="password" id="password" name="password" maxlength="100" autocomplete="current-password" required></div>
          <div class="mb-3"><label class="form-label" for="newpassword">New Password</label>
            <input class="form-control" type="password" id="newpassword" name="newpassword" minlength="6" maxlength="100" autocomplete="new-password" required></div>
          <div class="mb-3"><label class="form-label" for="confirmpassword">Confirm Password</label>
            <input class="form-control" type="password" id="confirmpassword" name="confirmpassword" minlength="6" maxlength="100" autocomplete="new-password" required></div>
          <button type="submit" name="change" class="btn btn-info"><i class="fa fa-key"></i> Change Password</button>
        </form>
      </div>
    </div>
  </div></div>
</t:admin>
