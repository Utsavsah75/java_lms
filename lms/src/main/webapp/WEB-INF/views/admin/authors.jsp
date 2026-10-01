<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:admin title="Manage" subtitle="Author" active="author" tables="true">
  <div class="mb-3"><a class="btn btn-info" href="${ctx}/admin/authors/add"><i class="fa fa-plus"></i> Add Author</a></div>
  <div class="panel-box">
    <div class="panel-head">Author Listing</div>
    <div class="panel-body table-responsive">
      <table class="table table-striped table-bordered table-hover data-table align-middle">
        <thead><tr><th>#</th><th>Author</th><th>Books</th><th>Creation Date</th><th>Updation Date</th><th class="no-sort">Action</th></tr></thead>
        <tbody>
        <c:forEach var="a" items="${authors}" varStatus="st">
          <tr>
            <td>${st.count}</td>
            <td><c:out value="${a.name}"/></td>
            <td>${a.bookCount}</td>
            <td><fmt:formatDate value="${a.creationDate}" pattern="yyyy-MM-dd HH:mm"/></td>
            <td><fmt:formatDate value="${a.updationDate}" pattern="yyyy-MM-dd HH:mm"/></td>
            <td class="action-col">
              <a class="btn btn-primary btn-sm" href="${ctx}/admin/authors/edit?id=${a.id}"><i class="fa fa-edit"></i> Edit</a>
              <form method="post" action="${ctx}/admin/authors/delete" class="d-inline" data-confirm="Are you sure you want to delete this author?">
                <input type="hidden" name="_csrf" value="${csrfToken}"><input type="hidden" name="id" value="${a.id}">
                <button class="btn btn-danger btn-sm"><i class="fa fa-trash"></i> Delete</button>
              </form>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
      <c:if test="${empty authors}"><p class="text-muted mb-0">No record found.</p></c:if>
    </div>
  </div>
</t:admin>
