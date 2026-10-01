<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="editing" value="${not empty author}"/>
<t:admin title="${editing ? 'Update' : 'Add'}" subtitle="Author" active="author">
  <div class="row"><div class="col-md-6 offset-md-3">
    <div class="panel-box">
      <div class="panel-head">Author Info</div>
      <div class="panel-body">
        <form method="post" action="${ctx}/admin/authors/${editing ? 'edit' : 'add'}">
          <input type="hidden" name="_csrf" value="${csrfToken}">
          <c:if test="${editing}"><input type="hidden" name="id" value="${author.id}"></c:if>
          <div class="mb-3">
            <label class="form-label" for="author">Author Name</label>
            <input class="form-control" type="text" id="author" name="author" maxlength="100" autocomplete="off" required
                   value="<c:out value='${empty param.author ? author.name : param.author}'/>">
          </div>
          <button type="submit" class="btn btn-info"><i class="fa fa-save"></i> ${editing ? 'Update' : 'Create'}</button>
          <a class="btn btn-outline-secondary" href="${ctx}/admin/authors">Cancel</a>
        </form>
      </div>
    </div>
  </div></div>
</t:admin>
