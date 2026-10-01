<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:student title="Book" subtitle="List" active="books" tables="true">
  <div class="panel-box">
    <div class="panel-head">Books in the Library</div>
    <div class="panel-body table-responsive">
      <table class="table table-striped table-bordered table-hover data-table align-middle">
        <thead><tr><th>#</th><th class="no-sort">Cover</th><th>Book Name</th><th>ISBN</th><th>Category</th><th>Author</th><th>Status</th></tr></thead>
        <tbody>
        <c:forEach var="b" items="${books}" varStatus="st">
          <tr>
            <td>${st.count}</td>
            <td><img class="book-thumb" src="${ctx}/uploads/<c:out value='${b.image}'/>" alt="" onerror="this.style.visibility='hidden'"></td>
            <td><c:out value="${b.name}"/></td>
            <td><c:out value="${b.isbn}"/></td>
            <td><c:out value="${b.categoryName}"/></td>
            <td><c:out value="${b.authorName}"/></td>
            <td><c:choose>
              <c:when test="${b.copiesAvailable > 0}"><span class="badge bg-success">Available (${b.copiesAvailable})</span></c:when>
              <c:otherwise><span class="badge bg-danger">Issued</span></c:otherwise>
            </c:choose></td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </div>
  </div>
</t:student>
