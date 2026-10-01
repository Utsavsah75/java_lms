<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:admin title="Return" subtitle="Book" active="issue">
  <div class="row"><div class="col-md-8 offset-md-2">
    <div class="panel-box">
      <div class="panel-head">Issued Book Details</div>
      <div class="panel-body">
        <div class="row g-3">
          <div class="col-md-4 text-center">
            <img class="book-cover" src="${ctx}/uploads/<c:out value='${issue.bookImage}'/>" alt="Book cover" onerror="this.style.visibility='hidden'">
            <h6 class="mt-2"><c:out value="${issue.bookName}"/></h6><small class="text-muted">ISBN <c:out value="${issue.isbn}"/></small>
          </div>
          <div class="col-md-8">
            <table class="table table-sm">
              <tr><th>Student</th><td><c:out value="${issue.studentName}"/> (<c:out value="${issue.studentCode}"/>)</td></tr>
              <tr><th>E-mail</th><td><c:out value="${issue.studentEmail}"/></td></tr>
              <tr><th>Mobile</th><td><c:out value="${issue.studentPhone}"/></td></tr>
              <tr><th>Issued on</th><td><fmt:formatDate value="${issue.issueDate}" pattern="yyyy-MM-dd HH:mm"/></td></tr>
              <tr><th>Due date</th><td><fmt:formatDate value="${issue.dueDate}" pattern="yyyy-MM-dd"/></td></tr>
              <tr><th>Returned on</th><td><c:choose><c:when test="${issue.returned}"><fmt:formatDate value="${issue.returnDate}" pattern="yyyy-MM-dd HH:mm"/></c:when><c:otherwise><span class="not-returned">Not Return Yet</span></c:otherwise></c:choose></td></tr>
            </table>
            <c:choose>
              <c:when test="${issue.returned}">
                <div class="alert alert-success mb-0">Returned. Fine charged: <strong>Rs. ${issue.fine}</strong></div>
              </c:when>
              <c:otherwise>
                <c:if test="${issue.overdueDays > 0}">
                  <div class="alert alert-warning">This book is <strong>${issue.overdueDays}</strong> day(s) overdue. Suggested fine: <strong>Rs. ${issue.suggestedFine}</strong>.</div>
                </c:if>
                <form method="post" action="${ctx}/admin/issues/return" class="row g-2 align-items-end">
                  <input type="hidden" name="_csrf" value="${csrfToken}">
                  <input type="hidden" name="id" value="${issue.id}">
                  <div class="col-sm-6"><label class="form-label" for="fine">Fine (Rs.)</label>
                    <input class="form-control" type="number" min="0" step="1" id="fine" name="fine" required
                           value="<c:out value='${empty param.fine ? issue.suggestedFine : param.fine}'/>"></div>
                  <div class="col-auto">
                    <button type="submit" name="return" class="btn btn-info"><i class="fa fa-undo"></i> Return Book</button>
                    <a class="btn btn-outline-secondary" href="${ctx}/admin/issues">Cancel</a>
                  </div>
                </form>
              </c:otherwise>
            </c:choose>
          </div>
        </div>
      </div>
    </div>
  </div></div>
</t:admin>
