<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:admin title="Manage" subtitle="Issued Books" active="issue" tables="true">
  <form method="get" action="${ctx}/admin/issues" class="row g-2 align-items-end mb-3">
    <div class="col-auto"><label class="form-label mb-0" for="from">From</label>
      <input type="date" class="form-control" id="from" name="from" value="<c:out value='${from}'/>"></div>
    <div class="col-auto"><label class="form-label mb-0" for="to">To</label>
      <input type="date" class="form-control" id="to" name="to" value="<c:out value='${to}'/>"></div>
    <div class="col-auto">
      <button class="btn btn-primary" type="submit"><i class="fa fa-filter"></i> Apply Filter</button>
      <a class="btn btn-outline-secondary" href="${ctx}/admin/issues">Clear</a>
    </div>
    <div class="col-auto ms-auto"><a class="btn btn-info" href="${ctx}/admin/issue"><i class="fa fa-plus"></i> Issue Book</a></div>
  </form>
  <div class="panel-box">
    <div class="panel-head">Issued Books Listing</div>
    <div class="panel-body table-responsive">
      <table class="table table-striped table-bordered table-hover data-table align-middle">
        <thead><tr><th>#</th><th>Student</th><th>Book Name</th><th>ISBN</th><th>Issued On</th><th>Due Date</th><th>Returned On</th><th>Fine (Rs.)</th><th class="no-sort">Action</th></tr></thead>
        <tbody>
        <c:forEach var="i" items="${issues}" varStatus="st">
          <tr>
            <td>${st.count}</td>
            <td><a href="${ctx}/admin/students/history?code=<c:out value='${i.studentCode}'/>"><c:out value="${i.studentName}"/></a><br><small class="text-muted"><c:out value="${i.studentCode}"/></small></td>
            <td><c:out value="${i.bookName}"/></td>
            <td><c:out value="${i.isbn}"/></td>
            <td><fmt:formatDate value="${i.issueDate}" pattern="yyyy-MM-dd HH:mm"/></td>
            <td><fmt:formatDate value="${i.dueDate}" pattern="yyyy-MM-dd"/>
              <c:if test="${not i.returned and i.overdueDays > 0}"><br><span class="badge bg-danger">${i.overdueDays} day(s) late</span></c:if></td>
            <c:choose>
              <c:when test="${i.returned}">
                <td><fmt:formatDate value="${i.returnDate}" pattern="yyyy-MM-dd HH:mm"/></td>
                <td>${i.fine}</td>
                <td><span class="badge bg-success">Returned</span></td>
              </c:when>
              <c:otherwise>
                <td class="not-returned">Not Return Yet</td>
                <td>-</td>
                <td><a class="btn btn-warning btn-sm" href="${ctx}/admin/issues/return?id=${i.id}"><i class="fa fa-undo"></i> Return</a></td>
              </c:otherwise>
            </c:choose>
          </tr>
        </c:forEach>
        </tbody>
      </table>
      <c:if test="${empty issues}"><p class="text-muted mb-0">No record found.</p></c:if>
    </div>
  </div>
</t:admin>
