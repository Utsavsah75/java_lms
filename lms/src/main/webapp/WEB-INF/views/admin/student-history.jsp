<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:admin title="Student" subtitle="Details" active="student" tables="true">
  <div class="panel-box mb-4">
    <div class="panel-head">Student Information</div>
    <div class="panel-body">
      <div class="row g-3 align-items-center">
        <div class="col-auto"><c:if test="${not empty student.photo}"><img class="book-cover" style="max-width:110px" src="${ctx}/uploads/<c:out value='${student.photo}'/>" alt="" onerror="this.style.visibility='hidden'"></c:if></div>
        <div class="col">
          <h5 class="mb-1"><c:out value="${student.name}"/> <small class="text-muted">(<c:out value="${student.studentCode}"/>)</small></h5>
          <div><i class="fa fa-envelope"></i> <c:out value="${student.email}"/> &nbsp; <i class="fa fa-phone"></i> <c:out value="${student.phone}"/></div>
          <div><i class="fa fa-map-marker"></i> <c:out value="${student.address}"/> &nbsp; <span class="badge ${student.active ? 'bg-success' : 'bg-danger'}">${student.active ? 'Active' : 'Blocked'}</span></div>
        </div>
      </div>
    </div>
  </div>
  <div class="panel-box">
    <div class="panel-head">Borrowing History</div>
    <div class="panel-body table-responsive">
      <table class="table table-striped table-bordered table-hover data-table align-middle">
        <thead><tr><th>#</th><th>Book</th><th>ISBN</th><th>Issued On</th><th>Due Date</th><th>Returned On</th><th>Fine (Rs.)</th></tr></thead>
        <tbody>
        <c:forEach var="i" items="${issues}" varStatus="st">
          <tr>
            <td>${st.count}</td>
            <td><c:out value="${i.bookName}"/></td>
            <td><c:out value="${i.isbn}"/></td>
            <td><fmt:formatDate value="${i.issueDate}" pattern="yyyy-MM-dd HH:mm"/></td>
            <td><fmt:formatDate value="${i.dueDate}" pattern="yyyy-MM-dd"/></td>
            <td><c:choose><c:when test="${i.returned}"><fmt:formatDate value="${i.returnDate}" pattern="yyyy-MM-dd HH:mm"/></c:when><c:otherwise><span class="not-returned">Not returned yet</span></c:otherwise></c:choose></td>
            <td><c:choose><c:when test="${i.returned}">${i.fine}</c:when><c:otherwise>-</c:otherwise></c:choose></td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
      <c:if test="${empty issues}"><p class="text-muted mb-0">This student has not borrowed any book yet.</p></c:if>
    </div>
  </div>
  <a class="btn btn-outline-secondary mt-3" href="${ctx}/admin/students">Back</a>
</t:admin>
