<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:admin title="Manage" subtitle="Books" active="book" tables="true">
  <div class="mb-3"><a class="btn btn-info" href="${ctx}/admin/books/add"><i class="fa fa-plus"></i> Add Book</a></div>
  <div class="panel-box">
    <div class="panel-head">Books Listing</div>
    <div class="panel-body table-responsive">
      <table class="table table-striped table-bordered table-hover data-table align-middle">
        <thead><tr><th>#</th><th class="no-sort">Cover</th><th>Book Name</th><th>ISBN</th><th>Category</th><th>Author</th><th>Price</th><th>Available</th><th class="no-sort">Action</th></tr></thead>
        <tbody>
        <c:forEach var="b" items="${books}" varStatus="st">
          <tr>
            <td>${st.count}</td>
            <td><img class="book-thumb" src="${ctx}/uploads/<c:out value='${b.image}'/>" alt="" onerror="this.style.visibility='hidden'"></td>
            <td><c:out value="${b.name}"/></td>
            <td><c:out value="${b.isbn}"/></td>
            <td><c:out value="${b.categoryName}"/></td>
            <td><c:out value="${b.authorName}"/></td>
            <td><fmt:formatNumber value="${b.price}" minFractionDigits="2" maxFractionDigits="2"/></td>
            <td>
              <span class="badge ${b.copiesAvailable > 0 ? 'bg-success' : 'bg-danger'}">${b.copiesAvailable} / ${b.quantity}</span>
            </td>
            <td class="action-col">
              <a class="btn btn-primary btn-sm" href="${ctx}/admin/books/edit?id=${b.id}"><i class="fa fa-edit"></i> Edit</a>
              <a class="btn btn-secondary btn-sm" href="${ctx}/admin/books/image?id=${b.id}"><i class="fa fa-image"></i> Image</a>
              <form method="post" action="${ctx}/admin/books/delete" class="d-inline" data-confirm="Are you sure you want to delete this book?">
                <input type="hidden" name="_csrf" value="${csrfToken}"><input type="hidden" name="id" value="${b.id}">
                <button class="btn btn-danger btn-sm"><i class="fa fa-trash"></i> Delete</button>
              </form>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
      <c:if test="${empty books}"><p class="text-muted mb-0">No record found.</p></c:if>
    </div>
  </div>
</t:admin>
