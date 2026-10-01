<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:admin title="Manage" subtitle="Category" active="category" tables="true">
  <div class="mb-3"><a class="btn btn-info" href="${ctx}/admin/categories/add"><i class="fa fa-plus"></i> Add Category</a></div>
  <div class="panel-box">
    <div class="panel-head">Category Listing</div>
    <div class="panel-body table-responsive">
      <table class="table table-striped table-bordered table-hover data-table align-middle">
        <thead><tr><th>#</th><th>Category</th><th>Status</th><th>Books</th><th>Creation Date</th><th>Updation Date</th><th class="no-sort">Action</th></tr></thead>
        <tbody>
        <c:forEach var="cat" items="${categories}" varStatus="st">
          <tr>
            <td>${st.count}</td>
            <td><c:out value="${cat.name}"/></td>
            <td>
              <form method="post" action="${ctx}/admin/categories/status" class="d-inline">
                <input type="hidden" name="_csrf" value="${csrfToken}">
                <input type="hidden" name="id" value="${cat.id}">
                <input type="hidden" name="action" value="${cat.active ? 'deactivate' : 'activate'}">
                <c:choose>
                  <c:when test="${cat.active}"><button class="btn btn-success btn-sm" title="Click to deactivate"><i class="fa fa-check"></i> Active</button></c:when>
                  <c:otherwise><button class="btn btn-danger btn-sm" title="Click to activate"><i class="fa fa-times"></i> Inactive</button></c:otherwise>
                </c:choose>
              </form>
            </td>
            <td>${cat.bookCount}</td>
            <td><fmt:formatDate value="${cat.creationDate}" pattern="yyyy-MM-dd HH:mm"/></td>
            <td><fmt:formatDate value="${cat.updationDate}" pattern="yyyy-MM-dd HH:mm"/></td>
            <td class="action-col">
              <a class="btn btn-primary btn-sm" href="${ctx}/admin/categories/edit?id=${cat.id}"><i class="fa fa-edit"></i> Edit</a>
              <form method="post" action="${ctx}/admin/categories/delete" class="d-inline" data-confirm="Are you sure you want to delete this category?">
                <input type="hidden" name="_csrf" value="${csrfToken}"><input type="hidden" name="id" value="${cat.id}">
                <button class="btn btn-danger btn-sm"><i class="fa fa-trash"></i> Delete</button>
              </form>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
      <c:if test="${empty categories}"><p class="text-muted mb-0">No record found.</p></c:if>
    </div>
  </div>
</t:admin>
