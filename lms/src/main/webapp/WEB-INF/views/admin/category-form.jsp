<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="editing" value="${not empty category}"/>
<t:admin title="${editing ? 'Update' : 'Add'}" subtitle="Category" active="category">
  <div class="row"><div class="col-md-6 offset-md-3">
    <div class="panel-box">
      <div class="panel-head">Category Info</div>
      <div class="panel-body">
        <form method="post" action="${ctx}/admin/categories/${editing ? 'edit' : 'add'}">
          <input type="hidden" name="_csrf" value="${csrfToken}">
          <c:if test="${editing}"><input type="hidden" name="id" value="${category.id}"></c:if>
          <div class="mb-3">
            <label class="form-label" for="category">Category Name</label>
            <input class="form-control" type="text" id="category" name="category" maxlength="100" autocomplete="off" required
                   value="<c:out value='${empty param.category ? category.name : param.category}'/>">
          </div>
          <c:set var="st" value="${empty param.status ? (editing ? category.status : 1) : param.status}"/>
          <div class="mb-3">
            <label class="form-label d-block">Status</label>
            <div class="form-check form-check-inline"><input class="form-check-input" type="radio" name="status" id="stActive" value="1" ${st == 1 ? 'checked' : ''}><label class="form-check-label" for="stActive">Active</label></div>
            <div class="form-check form-check-inline"><input class="form-check-input" type="radio" name="status" id="stInactive" value="0" ${st == 0 ? 'checked' : ''}><label class="form-check-label" for="stInactive">Inactive</label></div>
          </div>
          <button type="submit" class="btn btn-info"><i class="fa fa-save"></i> ${editing ? 'Update' : 'Create'}</button>
          <a class="btn btn-outline-secondary" href="${ctx}/admin/categories">Cancel</a>
        </form>
      </div>
    </div>
  </div></div>
</t:admin>
