<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:student title="Issued" subtitle="Books" active="issued" tables="true">
  <div class="panel-box">
    <div class="panel-head">My Issued Books</div>
    <div class="panel-body table-responsive">
      <table class="table table-striped table-bordered table-hover data-table align-middle">
        <thead><tr><th>#</th><th>Book Name</th><th>ISBN</th><th>Issued On</th><th>Due Date</th><th>Returned On</th><th>Fine (Rs.)</th></tr></thead>
        <tbody>
        <c:forEach var="i" items="${issues}" varStatus="st">
          <tr>
            <td>${st.count}</td>
            <td><c:out value="${i.bookName}"/></td>
            <td><c:out value="${i.isbn}"/></td>
            <td><fmt:formatDate value="${i.issueDate}" pattern="yyyy-MM-dd HH:mm"/></td>
            <td><fmt:formatDate value="${i.dueDate}" pattern="yyyy-MM-dd"/>
              <c:if test="${not i.returned and i.overdueDays > 0}"><br><span class="badge bg-danger">${i.overdueDays} day(s) late</span></c:if></td>
            <td><c:choose><c:when test="${i.returned}"><fmt:formatDate value="${i.returnDate}" pattern="yyyy-MM-dd HH:mm"/></c:when><c:otherwise><span class="not-returned">Not Return Yet</span></c:otherwise></c:choose></td>
            <td><c:choose><c:when test="${i.returned}">${i.fine}</c:when><c:otherwise>-</c:otherwise></c:choose></td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
      <c:if test="${empty issues}"><p class="text-muted mb-0">You have not borrowed any book yet.</p></c:if>
    </div>
  </div>
</t:student>
